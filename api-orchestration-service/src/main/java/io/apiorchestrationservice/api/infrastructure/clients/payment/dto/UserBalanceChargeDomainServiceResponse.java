package io.apiorchestrationservice.api.infrastructure.clients.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.BalanceChargeInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record UserBalanceChargeDomainServiceResponse(
    Long userBalanceId,
    Long userId,
    BigDecimal amount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
	public BalanceChargeInfo toBalanceChargeInfo() {
		return ObjectMapperBasedVoMapper.convert(this, BalanceChargeInfo.class);
	}
}
