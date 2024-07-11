package io.paymentservice.api.balance.business.dto.outport;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.paymentservice.api.balance.business.domainentity.BalanceTransaction;
import io.paymentservice.api.balance.business.domainentity.TransactionType;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public record BalanceTransactionInfo(
	Long transactionId,
	Long userId,
	BigDecimal amount,
	TransactionType transactionType,
	LocalDateTime createdAt
) {
	public static BalanceTransactionInfo from(BalanceTransaction balanceTransactions) {
		return ObjectMapperBasedVoMapper.convert(balanceTransactions, BalanceTransactionInfo.class);
	}
}
