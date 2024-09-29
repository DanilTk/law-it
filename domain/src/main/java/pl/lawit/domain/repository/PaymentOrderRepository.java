package pl.lawit.domain.repository;

import pl.lawit.domain.model.PaymentOrder;
import pl.lawit.kernel.repository.BaseRepository;

import static pl.lawit.domain.command.PaymentOrderCommand.CreatePaymentOrder;

public interface PaymentOrderRepository extends BaseRepository<PaymentOrder> {

	PaymentOrder create(CreatePaymentOrder command);

	PaymentOrder getByOrderId(String id);

}
