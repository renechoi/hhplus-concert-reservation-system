package io.apiorchestrationservice.api.application.dto.response;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.dto.outport.BalanceChargeInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public record UserBalanceChargeResponse(
	Long userId,
	BigDecimal amount
) {
	public static UserBalanceChargeResponse from(BalanceChargeInfo charge) {
		return ObjectMapperBasedVoMapper.convert(charge, UserBalanceChargeResponse.class);
	}
}