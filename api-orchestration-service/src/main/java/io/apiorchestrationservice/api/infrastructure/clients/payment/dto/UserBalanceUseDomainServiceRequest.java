package io.apiorchestrationservice.api.infrastructure.clients.payment.dto;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.dto.inport.UserBalanceChargeCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record UserBalanceUseDomainServiceRequest(
    BigDecimal amount
) {
	public static UserBalanceUseDomainServiceRequest from(UserBalanceChargeCommand command) {
		return new UserBalanceUseDomainServiceRequest(command.getAmount());
	}
}
