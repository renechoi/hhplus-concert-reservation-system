package io.paymentservice.api.balance.business.domainentity;

import static io.paymentservice.common.mapper.ObjectMapperBasedVoMapper.*;
import static java.math.BigDecimal.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.paymentservice.api.balance.business.dto.event.UserBalanceChargeEvent;
import io.paymentservice.api.balance.business.dto.event.UserBalanceUseEvent;
import io.paymentservice.common.exception.definitions.UserBalanceUseUnAvailableException;
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
public class UserBalance extends AbstractAggregateRoot<UserBalance> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userBalanceId;

	private Long userId;

	private BigDecimal amount;

	@CreatedDate
	@Column(updatable = false)
	@Setter
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Setter
	private LocalDateTime updatedAt;

	public static UserBalance createDefaultNewUserBalance(Long userId) {
		return UserBalance.builder().userId(userId).amount(ZERO).build();
	}


	public UserBalance addAmount(BigDecimal amount, TransactionReason transactionReason) {
		this.amount = this.amount.add(amount != null ? amount : ZERO);
		this.registerEvent(convert(this, UserBalanceChargeEvent.class).with(identifyTransactionReason(transactionReason)));
		return this;
	}

	public UserBalance use(BigDecimal amount, TransactionReason transactionReason) {

		if(this.amount.compareTo(amount) < 0){
			throw new UserBalanceUseUnAvailableException();
		}

		this.amount = this.amount.subtract(amount != null ? amount : ZERO);
		this.registerEvent(convert(this, UserBalanceUseEvent.class).with(identifyTransactionReason(transactionReason)));
		return this;
	}

	private TransactionReason identifyTransactionReason(TransactionReason transactionReason){
		return transactionReason == null ? TransactionReason.NORMAL : transactionReason;
	}
}
