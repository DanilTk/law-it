package pl.lawit.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.LegalCaseEntity;
import pl.lawit.data.entity.PaymentOrderEntity;
import pl.lawit.domain.command.PaymentOrderCommand.CreatePaymentOrder;
import pl.lawit.domain.model.PaymentOrder;

@Mapper(componentModel = "spring")
public abstract class PaymentOrderMapper extends BaseMapper {

	@Mapping(target = "uuid", source = "uuid")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", expression = "java(ofOption(entity.getUpdatedAt()))")
	@Mapping(target = "createdBy", expression = "java(extractUserUuid(entity.getCreatedBy()))")
	@Mapping(target = "updatedBy", expression = "java(ofOption(entity.getUpdatedBy()).map($->$.getUuid()))")
	@Mapping(target = "orderId", expression = "java(entity.getOrderId())")
	@Mapping(target = "amount", expression = "java(ofMoneyAmount(entity.getAmount()))")
	@Mapping(target = "currencyCode", expression = "java(entity.getCurrencyCode())")
	@Mapping(target = "legalCaseUuid", expression = "java(entity.getLegalCase().getUuid())")
	@Mapping(target = "paymentType", expression = "java(entity.getPaymentType())")
	@Mapping(target = "paymentLink", expression = "java(ofUrl(entity.getPaymentLink()))")
	@Mapping(target = "paymentStatus", expression = "java(entity.getPaymentStatus())")
	public abstract PaymentOrder mapToDomain(PaymentOrderEntity entity);

	@Mapping(target = "uuid", expression = "java(uuidProvider.getUuid())")
	@Mapping(target = "createdAt", expression = "java(timeProvider.getInstant())")
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", source = "userEntity")
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "paymentStatus", source = "command.paymentStatus")
	@Mapping(target = "orderId", source = "command.orderId")
	@Mapping(target = "currencyCode", source = "command.currencyCode")
	@Mapping(target = "legalCase", source = "legalCaseEntity")
	@Mapping(target = "paymentType", source = "command.paymentType")
	@Mapping(target = "paymentLink", expression = "java(command.paymentLink().toString())")
	@Mapping(target = "amount", source = "command.purchasePrice.value")
	public abstract PaymentOrderEntity mapToEntity(CreatePaymentOrder command, LegalCaseEntity legalCaseEntity,
												   ApplicationUserEntity userEntity);

}
