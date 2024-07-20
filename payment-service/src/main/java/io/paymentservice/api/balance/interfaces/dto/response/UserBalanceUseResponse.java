package io.paymentservice.api.balance.interfaces.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.paymentservice.api.balance.business.dto.outport.UserBalanceUseInfo;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public record UserBalanceUseResponse(
	Long userBalanceId,
	Long userId,
	BigDecimal amount,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static UserBalanceUseResponse from(UserBalanceUseInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, UserBalanceUseResponse.class);
	}
}
