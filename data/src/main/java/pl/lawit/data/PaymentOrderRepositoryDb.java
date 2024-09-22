package pl.lawit.data;

import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.LegalCaseEntity;
import pl.lawit.data.entity.PaymentOrderEntity;
import pl.lawit.data.jpa.ApplicationUserRepositoryJpa;
import pl.lawit.data.jpa.LegalCaseRepositoryJpa;
import pl.lawit.data.jpa.PaymentOrderRepositoryJpa;
import pl.lawit.data.mapper.PaymentOrderMapper;
import pl.lawit.domain.model.PaymentOrder;
import pl.lawit.domain.repository.PaymentOrderRepository;
import pl.lawit.kernel.exception.ObjectNotFoundException;

import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;
import static pl.lawit.domain.command.PaymentOrderCommand.CreatePaymentOrder;

@Repository
@RequiredArgsConstructor
public class PaymentOrderRepositoryDb implements PaymentOrderRepository {

	private final PaymentOrderRepositoryJpa paymentOrderRepositoryJpa;

	private final ApplicationUserRepositoryJpa applicationUserRepositoryJpa;

	private final LegalCaseRepositoryJpa legalCaseRepositoryJpa;

	private final PaymentOrderMapper paymentOrderMapper;

	@Override
	@Transactional(propagation = MANDATORY)
	public PaymentOrder create(CreatePaymentOrder command) {
		ApplicationUserEntity userEntity = applicationUserRepositoryJpa
			.getReferenceByUuid(command.authenticatedUser().userUuid());
		LegalCaseEntity legalCaseEntity = legalCaseRepositoryJpa.getReferenceByUuid(command.legalCaseUuid());
		PaymentOrderEntity entity = paymentOrderMapper.mapToEntity(command, legalCaseEntity, userEntity);

		entity = paymentOrderRepositoryJpa.save(entity);

		return paymentOrderMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PaymentOrder getByOrderId(String id) {
		return paymentOrderRepositoryJpa.findByOrderId(id)
			.map(paymentOrderMapper::mapToDomain)
			.getOrElseThrow(() -> ObjectNotFoundException.byUid(id, PaymentOrder.class));
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PaymentOrder getByUuid(UUID uuid) {
		PaymentOrderEntity entity = paymentOrderRepositoryJpa.getReferenceByUuid(uuid);
		return paymentOrderMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Option<PaymentOrder> findByUuid(UUID uuid) {
		return Option.ofOptional(paymentOrderRepositoryJpa.findById(uuid)
			.map(paymentOrderMapper::mapToDomain));
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public List<PaymentOrder> findAll() {
		return List.ofAll(paymentOrderRepositoryJpa.findAll())
			.map(paymentOrderMapper::mapToDomain);
	}
}
