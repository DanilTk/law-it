package pl.lawit.data.jpa;

import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lawit.data.entity.PaymentOrderEntity;
import pl.lawit.domain.model.PaymentOrder;
import pl.lawit.kernel.exception.ObjectNotFoundException;

import java.util.UUID;

public interface PaymentOrderRepositoryJpa extends JpaRepository<PaymentOrderEntity, UUID> {

	default PaymentOrderEntity getReferenceByUuid(UUID uuid) {
		return findById(uuid).orElseThrow(() -> ObjectNotFoundException.byUUID(uuid, PaymentOrder.class));
	}

	Option<PaymentOrderEntity> findByOrderId(String id);

}
