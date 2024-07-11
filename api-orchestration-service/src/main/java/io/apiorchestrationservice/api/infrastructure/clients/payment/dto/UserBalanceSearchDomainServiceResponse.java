package io.apiorchestrationservice.api.infrastructure.clients.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.BalanceSearchInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record UserBalanceSearchDomainServiceResponse(
	Long userBalanceId,
	Long userId,
	BigDecimal amount,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public BalanceSearchInfo toBalanceSearchInfo() {
		return ObjectMapperBasedVoMapper.convert(this, BalanceSearchInfo.class);
	}
}