package io.paymentservice.api.balance.business.dto.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.paymentservice.api.balance.business.domainentity.TransactionReason;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public record BalanceChargeEvent(
	Long balanceId,
	Long userId,
	BigDecimal amount,
	TransactionReason transactionReason,

	LocalDateTime createdAt,
	LocalDateTime updatedAt

) {
	public BalanceChargeEvent with(TransactionReason transactionReason) {
		return new BalanceChargeEvent(this.balanceId,this.userId,this.amount, transactionReason, this.createdAt, this.updatedAt);
	}
}
