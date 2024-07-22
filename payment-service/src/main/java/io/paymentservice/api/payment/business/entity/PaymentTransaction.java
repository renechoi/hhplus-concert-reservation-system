package io.paymentservice.api.payment.business.entity;

import static io.paymentservice.api.payment.business.entity.PaymentStatus.*;
import static io.paymentservice.common.model.GlobalResponseCode.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.paymentservice.common.exception.definitions.PaymentCancelUnAvailableException;
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

	public boolean isCancelled() {
		return this.paymentStatus == PaymentStatus.CANCELLED;
	}

	public PaymentTransaction assertNotCanceled() {
		if (this.isCancelled()) {
			throw new PaymentCancelUnAvailableException(PAYMENT_ALREADY_CANCELED, transactionId);
		}
		return this;
	}
}