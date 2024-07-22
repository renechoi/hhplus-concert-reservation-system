package io.paymentservice.api.balance.business.entity;

import static io.paymentservice.common.mapper.ObjectMapperBasedVoMapper.*;
import static java.math.BigDecimal.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.paymentservice.api.balance.business.dto.event.BalanceChargeEvent;
import io.paymentservice.api.balance.business.dto.event.BalanceUseEvent;
import io.paymentservice.common.exception.definitions.BalanceUseUnAvailableException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Balance extends AbstractAggregateRoot<Balance> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long balanceId;

	private Long userId;

	private BigDecimal amount;

	@CreatedDate
	@Column(updatable = false)
	@Setter
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Setter
	private LocalDateTime updatedAt;

	public static Balance createDefaultNewBalance(Long userId) {
		return Balance.builder().userId(userId).amount(ZERO).build();
	}


	public Balance charge(BigDecimal amount, TransactionReason transactionReason) {
		this.amount = this.amount.add(amount != null ? amount : ZERO);
		this.registerEvent(convert(this, BalanceChargeEvent.class).with(identifyTransactionReason(transactionReason)));
		return this;
	}

	public Balance use(BigDecimal amount, TransactionReason transactionReason) {

		if(this.amount.compareTo(amount) < 0){
			throw new BalanceUseUnAvailableException();
		}

		this.amount = this.amount.subtract(amount != null ? amount : ZERO);
		this.registerEvent(convert(this, BalanceUseEvent.class).with(identifyTransactionReason(transactionReason)));
		return this;
	}

	private TransactionReason identifyTransactionReason(TransactionReason transactionReason){
		return transactionReason == null ? TransactionReason.NORMAL : transactionReason;
	}
}
