package io.apiorchestrationservice.api.application.dto.response;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.dto.outport.BalanceSearchInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public record UserBalanceSearchResponse(
	Long userId,
	BigDecimal amount
) {
	public static UserBalanceSearchResponse from(BalanceSearchInfo charge) {
		return ObjectMapperBasedVoMapper.convert(charge, UserBalanceSearchResponse.class);
	}
}