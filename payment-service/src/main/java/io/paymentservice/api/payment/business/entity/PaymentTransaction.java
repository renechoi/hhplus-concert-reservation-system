package io.paymentservice.api.payment.business.entity;

import static io.paymentservice.api.payment.business.entity.PaymentStatus.*;
import static io.paymentservice.common.model.GlobalResponseCode.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.paymentservice.api.common.cache.business.dto.event.LocalCacheEvictEvent;
import io.paymentservice.common.exception.definitions.PaymentCancelUnAvailableException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
// @Table(name = "PaymentTransaction", uniqueConstraints = {
// 	@UniqueConstraint(columnNames = {"targetId", "userId", "amount", "paymentMethod"})
// })
@Table(
	indexes = {
		@Index(name = "idx_user_target", columnList = "userId, targetId"),
	}
)
public class PaymentTransaction extends AbstractAggregateRoot<PaymentTransaction> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;

	private Long userId;

	private String targetId; // 예약, 상품, 서비스 등 다양한 분야를 포괄할 수 있는 식별자

	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@PostPersist
	@PostUpdate
	@PostRemove
	private void publishSeatChangedEvent() {
		registerEvent(new LocalCacheEvictEvent("paymentTransaction", String.valueOf(userId)));
	}


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