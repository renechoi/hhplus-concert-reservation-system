package io.paymentservice.api.balance.business.dto.outport;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.paymentservice.api.balance.business.entity.Balance;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public record BalanceSearchInfo(
	Long balanceId,
	Long userId,
	BigDecimal amount,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static BalanceSearchInfo from(Balance balance) {
		return ObjectMapperBasedVoMapper.convert(balance, BalanceSearchInfo.class);
	}
}
