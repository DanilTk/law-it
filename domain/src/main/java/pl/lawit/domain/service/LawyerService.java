package pl.lawit.domain.service;

import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.domain.model.Lawyer;
import pl.lawit.domain.provider.IdpProvider;
import pl.lawit.domain.repository.LawyerRepository;
import pl.lawit.kernel.exception.BusinessException;
import pl.lawit.kernel.exception.LawyerAlreadyExistsException;
import pl.lawit.kernel.exception.PeselAlreadyExistsException;
import pl.lawit.kernel.model.ApplicationUserRole;
import pl.lawit.kernel.model.PageResult;

import java.util.UUID;

import static pl.lawit.domain.command.LawyerCommand.CreateLawyer;
import static pl.lawit.kernel.model.ApplicationUserRole.ADMIN_USER;
import static pl.lawit.kernel.model.ApplicationUserRole.LAWYER_USER;

@Service
@RequiredArgsConstructor
public class LawyerService {

	private final UserService userService;

	private final IdpProvider idpProvider;

	private final LawyerRepository lawyerRepository;

	@Transactional
	public Lawyer createLawyer(CreateLawyer command) {
		if (lawyerRepository.existsByPesel(command.pesel())) {
			throw new PeselAlreadyExistsException();
		}

		if (lawyerRepository.existsByEmail(command.userEmail())) {
			throw new LawyerAlreadyExistsException();
		}

		Option<ApplicationUser> idpUser = idpProvider.findByEmail(command.userEmail());
		if (idpUser.isDefined()) {
			Set<ApplicationUserRole> userRoles = idpProvider.getUserRoles(idpUser.get().idpUid());

			if (userRoles.contains(ADMIN_USER)) {
				throw new BusinessException("Cannot assign admin as lawyer");
			}
		} else {
			ApplicationUser applicationUser = userService.migrateApplicationUser(command.userEmail());
			idpProvider.addUserRole(applicationUser.idpUid(), LAWYER_USER);
		}

		return lawyerRepository.create(command);
	}

	@Transactional
	public Lawyer getLawyer(UUID uuid) {
		return lawyerRepository.getByUuid(uuid);
	}

	@Transactional
	public PageResult<Lawyer> findLawyersPage(PageCommandQuery commandQuery) {
		return lawyerRepository.findPage(commandQuery);
	}
}
