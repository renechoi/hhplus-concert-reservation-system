package io.paymentservice.api.balance.business.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.paymentservice.api.balance.business.dto.event.BalanceChargeEvent;
import io.paymentservice.api.balance.business.dto.event.BalanceUseEvent;
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
 * @since : 2024/07/09
 */

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners({AuditingEntityListener.class})
public class BalanceTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;

	private Long userId;

	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;

	@Enumerated(EnumType.STRING)
	private TransactionReason transactionReason;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	public static BalanceTransaction createChargedEvent(BalanceChargeEvent event) {
		return BalanceTransaction
			.builder()
			.userId(event.userId())
			.amount(event.amount())
			.transactionType(TransactionType.CHARGE)
			.transactionReason(event.transactionReason())
			.build();
	}

	public static BalanceTransaction createUsedEvent(BalanceUseEvent event) {
		return BalanceTransaction.builder()
			.userId(event.userId())
			.amount(event.amount())
			.transactionType(TransactionType.USE)
			.transactionReason(event.transactionReason())
			.build();
	}
}