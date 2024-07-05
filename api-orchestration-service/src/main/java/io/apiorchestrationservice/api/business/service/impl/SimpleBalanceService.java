package io.apiorchestrationservice.api.business.service.impl;

import org.springframework.stereotype.Service;

import io.apiorchestrationservice.api.business.client.BalanceServiceClientAdapter;
import io.apiorchestrationservice.api.business.persistence.BalanceChargeRepository;
import io.apiorchestrationservice.api.business.service.BalanceService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Service
@RequiredArgsConstructor
public class SimpleBalanceService implements BalanceService {
	private final BalanceChargeRepository balanceChargeRepository;
	private final BalanceServiceClientAdapter balanceServiceClientAdapter;
}
