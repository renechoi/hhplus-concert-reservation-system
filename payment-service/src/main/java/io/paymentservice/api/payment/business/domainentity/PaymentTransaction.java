package io.paymentservice.api.payment.business.domainentity;

import static io.paymentservice.api.payment.business.domainentity.PaymentStatus.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class PaymentTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;

	private Long userId;

	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	private PaymentStatus paymentStatus;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	public PaymentTransaction withCompleted() {
		this.paymentStatus = COMPLETE;
		return this;
	}

	public PaymentTransaction doCancel() {
		this.paymentStatus = PaymentStatus.CANCELLED;
		return this;
	}
}