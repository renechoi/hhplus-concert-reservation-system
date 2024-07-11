package io.paymentservice.api.balance.business.dto.outport;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.paymentservice.api.balance.business.domainentity.UserBalance;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public record UserBalanceSearchInfo(
	Long userBalanceId,
	Long userId,
	BigDecimal amount,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static UserBalanceSearchInfo from(UserBalance userBalance) {
		return ObjectMapperBasedVoMapper.convert(userBalance, UserBalanceSearchInfo.class);
	}
}