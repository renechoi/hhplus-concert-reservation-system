package io.paymentservice.api.balance.business.dto.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.paymentservice.api.balance.business.domainentity.TransactionReason;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public record UserBalanceChargeEvent(
	Long userBalanceId,
	Long userId,
	BigDecimal amount,
	TransactionReason transactionReason,

	LocalDateTime createdAt,
	LocalDateTime updatedAt

) {
	public UserBalanceChargeEvent with(TransactionReason transactionReason) {
		return new UserBalanceChargeEvent(this.userBalanceId,this.userId,this.amount, transactionReason, this.createdAt, this.updatedAt);
	}
}