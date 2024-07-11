package io.paymentservice.api.balance.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.paymentservice.api.balance.business.dto.outport.UserBalanceChargeInfo;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public record UserBalanceChargeResponse(
	Long userBalanceId,
	Long userId,
	BigDecimal amount,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static UserBalanceChargeResponse from(UserBalanceChargeInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, UserBalanceChargeResponse.class);
	}
}
