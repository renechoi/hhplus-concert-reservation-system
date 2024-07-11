package io.apiorchestrationservice.api.infrastructure.clients.payment.dto;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.dto.inport.UserBalanceChargeCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record UserBalanceChargeDomainServiceRequest(
	Long userId,
    BigDecimal amount
) {
	public static UserBalanceChargeDomainServiceRequest from(UserBalanceChargeCommand command) {
		return new UserBalanceChargeDomainServiceRequest(command.getUserId(), command.getAmount());
	}
}
