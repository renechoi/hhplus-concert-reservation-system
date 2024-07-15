package io.paymentservice.api.balance.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.paymentservice.api.balance.business.domainentity.TransactionType;
import io.paymentservice.api.balance.business.dto.outport.BalanceTransactionInfo;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public record BalanceTransactionResponse(
	Long transactionId,
	Long userId,
	BigDecimal amount,
	TransactionType transactionType,
	LocalDateTime createdAt

) {
	public static BalanceTransactionResponse from(BalanceTransactionInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, BalanceTransactionResponse.class);
	}
}
