package pl.lawit.domain.service;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.Lawyer;
import pl.lawit.domain.model.LegalCase;
import pl.lawit.domain.model.LegalCaseInfo;
import pl.lawit.domain.model.PaymentOrder;
import pl.lawit.domain.processor.EmailProcessor;
import pl.lawit.domain.processor.JobScheduleProcessor;
import pl.lawit.domain.repository.LawyerRepository;
import pl.lawit.domain.repository.LegalCaseRepository;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.exception.BusinessException;
import pl.lawit.kernel.exception.NoPermissionException;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.kernel.util.TimeProvider;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;
import static pl.lawit.domain.command.CaseCommand.AcceptLegalCase;
import static pl.lawit.domain.command.CaseCommand.CreateCase;
import static pl.lawit.domain.command.CaseCommand.CreateCaseHistory;
import static pl.lawit.domain.command.CaseCommand.CreateFileAttachments;
import static pl.lawit.domain.command.CaseCommand.SubmitLegalCase;
import static pl.lawit.domain.model.CaseCategory.BASIC;
import static pl.lawit.domain.model.CaseStatus.DRAFT;
import static pl.lawit.domain.model.CaseStatus.IN_PROGRESS;
import static pl.lawit.domain.model.CaseStatus.SUBMITTED_FOR_WORK;

@Service
@RequiredArgsConstructor
public class LegalCaseService {

	private final LegalCaseDurationProperties legalCaseDurationProperties;

	private final TimeProvider timeProvider;

	private final JobScheduleProcessor jobScheduleProcessor;

	private final EmailProcessor emailProcessor;

	private final PaymentOrderService paymentOrderService;

	private final LawyerRepository lawyerRepository;

	private final LegalCaseRepository legalCaseRepository;

	@Transactional
	public LegalCaseInfo createLegalCase(CreateCase command)  {
		LegalCase legalCase = legalCaseRepository.create(command);

		if (!command.fileUuids().isEmpty()) {
			CreateFileAttachments repositoryCommand = CreateFileAttachments.builder()
				.fileUuids(command.fileUuids())
				.legalCaseUuid(legalCase.uuid())
				.authenticatedUser(command.authenticatedUser())
				.build();
			legalCaseRepository.saveCaseAttachments(repositoryCommand);
		}

		createCaseHistory(legalCase, command.authenticatedUser());

		PaymentOrder paymentOrder = paymentOrderService.createOrder(
			legalCase,
			command.authenticatedUser(),
			command.ipAddress());

		emailProcessor.sendEmail();

		return LegalCaseInfo.builder()
			.legalCase(legalCase)
			.paymentOrder(paymentOrder)
			.build();
	}

	@Transactional(propagation = MANDATORY)
	public void submitLegalCase(UUID uuid, AuthenticatedUser systemUser) {
		LegalCase legalCase = legalCaseRepository.getByUuid(uuid);

		if (legalCase.caseStatus() != DRAFT) {
			throw new BusinessException("Case cannot be submitted for work");
		}

		Instant acceptanceDeadline = timeProvider.getInstant()
			.plus(Duration.ofDays(legalCaseDurationProperties.getAcceptanceDeadlineHours()));
		SubmitLegalCase command = SubmitLegalCase.builder()
			.legalCaseUuid(legalCase.uuid())
			.caseStatus(SUBMITTED_FOR_WORK)
			.acceptanceDeadline(acceptanceDeadline)
			.authenticatedUser(systemUser)
			.build();
		legalCase = legalCaseRepository.submit(command);

		createCaseHistory(legalCase, command.authenticatedUser());

		jobScheduleProcessor.scheduleLegalCaseWithdrawal(legalCase.uuid(), acceptanceDeadline);

		emailProcessor.sendEmail();
	}

	@Transactional
	public LegalCase acceptCase(UUID uuid, AuthenticatedUser authenticatedUser) {
		LegalCase legalCase = legalCaseRepository.getByUuid(uuid);

		if (legalCase.caseStatus() != SUBMITTED_FOR_WORK) {
			throw new BusinessException("Case cannot be accepted");
		}

		Lawyer authenticatedLawyer = lawyerRepository.getByUserUuid(authenticatedUser.userUuid());
		Instant completionDeadline = timeProvider.getInstant()
			.plus(Duration.ofDays(legalCaseDurationProperties.getCompletionDeadlineHours()));

		AcceptLegalCase repositoryCommand = AcceptLegalCase.builder()
			.legalCaseUuid(legalCase.uuid())
			.caseStatus(IN_PROGRESS)
			.lawyerUuid(authenticatedLawyer.uuid())
			.completionDeadline(completionDeadline)
			.authenticatedUser(authenticatedUser)
			.build();
		legalCase = legalCaseRepository.accept(repositoryCommand);

		createCaseHistory(legalCase, authenticatedUser);

		jobScheduleProcessor.scheduleLegalCaseCompletion(legalCase.uuid(), completionDeadline);

		emailProcessor.sendEmail();

		return legalCase;
	}

	@Transactional
	public LegalCase withdrawCase(UUID uuid, AuthenticatedUser authenticatedUser) {
		LegalCase legalCase = legalCaseRepository.getByUuid(uuid);
		Lawyer authenticatedLawyer = lawyerRepository.getByUserUuid(authenticatedUser.userUuid());

		if (legalCase.caseStatus() != IN_PROGRESS) {
			throw new BusinessException("Case cannot be withdrawn");
		}

		if (!legalCase.lawyerUuid().get().equals(authenticatedLawyer.uuid())) {
			throw new NoPermissionException("You are not allowed to withdraw this case");
		}

		legalCase = legalCaseRepository.withdraw(uuid, authenticatedUser);
		createCaseHistory(legalCase, authenticatedUser);

		jobScheduleProcessor.unscheduleLegalCaseJobs(legalCase.uuid());

		paymentOrderService.refundOrder(legalCase.uuid());

		emailProcessor.sendEmail();

		return legalCase;
	}

	@Transactional
	public String getLegalCaseMessage(UUID uuid, AuthenticatedUser authenticatedUser) {
		LegalCase legalCase = legalCaseRepository.getByUuid(uuid);

		if (authenticatedUser.isLawyer()) {
			validateIsLawyerAllowedToAccessCase(legalCase, authenticatedUser);
		} else {
			validateIsClientAllowedToAccessCase(legalCase, authenticatedUser);
		}

		return legalCaseRepository.getCaseDescriptionByCaseUuid(uuid);
	}

	@Transactional
	public Set<UUID> findLegalCaseFiles(UUID uuid, AuthenticatedUser authenticatedUser) {
		LegalCase legalCase = legalCaseRepository.getByUuid(uuid);

		if (authenticatedUser.isLawyer()) {
			validateIsLawyerAllowedToAccessCase(legalCase, authenticatedUser);
		} else {
			validateIsClientAllowedToAccessCase(legalCase, authenticatedUser);
		}

		if (legalCase.caseCategory() == BASIC) {
			return HashSet.empty();
		} else {
			return legalCaseRepository.getCaseAttachments(uuid);
		}

	}

	@Transactional
	public PageResult<LegalCase> findLegalCasesPage(PageCommandQuery commandQuery,
													AuthenticatedUser authenticatedUser) {
		if (authenticatedUser.isLawyer()) {
			return legalCaseRepository.findLawyerCases(authenticatedUser.userUuid(), commandQuery);
		} else {
			return legalCaseRepository.findByUserUuid(authenticatedUser.userUuid(), commandQuery);
		}
	}

	@Transactional
	public LegalCase getCase(UUID uuid, AuthenticatedUser authenticatedUser) {
		LegalCase legalCase = legalCaseRepository.getByUuid(uuid);

		if (authenticatedUser.isLawyer()) {
			validateIsLawyerAllowedToAccessCase(legalCase, authenticatedUser);
		} else {
			validateIsClientAllowedToAccessCase(legalCase, authenticatedUser);
		}

		return legalCase;
	}

	private void validateIsLawyerAllowedToAccessCase(LegalCase legalCase, AuthenticatedUser authenticatedUser) {
		if (!legalCase.lawyerUuid().isDefined()) {
			throw new NoPermissionException("You are not allowed to access this case");
		}

		Lawyer lawyer = lawyerRepository.getByUserUuid(authenticatedUser.userUuid());
		if (!legalCase.lawyerUuid().get().equals(lawyer.uuid())) {
			throw new NoPermissionException("You are not allowed to access this case");
		}
	}

	private void validateIsClientAllowedToAccessCase(LegalCase legalCase, AuthenticatedUser authenticatedUser) {
		if (!legalCase.createdBy().equals(authenticatedUser.userUuid())) {
			throw new NoPermissionException("You are not allowed to access this case");
		}
	}

	private void createCaseHistory(LegalCase legalCase, AuthenticatedUser authenticatedUser) {
		CreateCaseHistory createCaseHistory = CreateCaseHistory.builder()
			.legalCaseUuid(legalCase.uuid())
			.authenticatedUser(authenticatedUser)
			.build();

		legalCaseRepository.createCaseHistory(createCaseHistory);
	}

}

@Data
@Configuration
@ConfigurationProperties("application.legal-case-state-duration")
class LegalCaseDurationProperties {

	private int acceptanceDeadlineHours;

	private int completionDeadlineHours;

}



