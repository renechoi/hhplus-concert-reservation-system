package io.apiorchestrationservice.api.application.facade;

import io.apiorchestrationservice.api.business.service.BalanceService;
import io.apiorchestrationservice.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Facade
@RequiredArgsConstructor
public class BalanceChargeFacade {
	private final BalanceService balanceService;
}