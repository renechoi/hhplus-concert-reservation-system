package io.apiorchestrationservice.api.business.service.impl;

import org.springframework.stereotype.Service;

import io.apiorchestrationservice.api.business.client.PaymentServiceClientAdapter;
import io.apiorchestrationservice.api.business.dto.inport.UserBalanceChargeCommand;
import io.apiorchestrationservice.api.business.dto.outport.BalanceChargeInfo;
import io.apiorchestrationservice.api.business.dto.outport.BalanceSearchInfo;
import io.apiorchestrationservice.api.business.service.BalanceService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Service
@RequiredArgsConstructor
public class SimpleBalanceService implements BalanceService {
	private final PaymentServiceClientAdapter paymentServiceClientAdapter;

	@Override
	public BalanceChargeInfo charge(UserBalanceChargeCommand command) {
		return paymentServiceClientAdapter.charge(command.getUserId(), command);
	}

	@Override
	public BalanceSearchInfo retrieveBalance(Long userId) {
		return paymentServiceClientAdapter.search(userId);
	}
}
