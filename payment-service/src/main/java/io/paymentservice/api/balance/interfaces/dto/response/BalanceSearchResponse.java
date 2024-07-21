package io.paymentservice.api.balance.interfaces.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.paymentservice.api.balance.business.dto.outport.BalanceSearchInfo;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public record BalanceSearchResponse(
	Long balanceId,
	Long userId,
	BigDecimal amount,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static BalanceSearchResponse from(BalanceSearchInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, BalanceSearchResponse.class);
	}
}
