package pl.lawit.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.lawit.domain.model.CurrencyCode;
import pl.lawit.domain.model.PaymentStatus;
import pl.lawit.domain.model.PaymentType;

import java.math.BigDecimal;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED, force = true)
@SuperBuilder
@Entity
@Table(name = "payment_order")
public class PaymentOrderEntity extends BaseEntity {

	@Size(max = 100)
	@NonNull
	@Column(name = "order_id", nullable = false, length = 100)
	private String orderId;

	@NonNull
	@Enumerated(STRING)
	@Column(name = "payment_status", nullable = false, length = 10)
	private PaymentStatus paymentStatus;

	@NonNull
	@Column(name = "amount", nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	@NonNull
	@Enumerated(STRING)
	@Column(name = "currency_code", nullable = false, length = 3)
	private CurrencyCode currencyCode;

	@NonNull
	@Column(name = "payment_link", nullable = false)
	private String paymentLink;

	@NonNull
	@Enumerated(STRING)
	@Column(name = "payment_type", nullable = false, length = 10)
	private PaymentType paymentType;

	@NonNull
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "case_uuid", nullable = false)
	private LegalCaseEntity legalCase;

}