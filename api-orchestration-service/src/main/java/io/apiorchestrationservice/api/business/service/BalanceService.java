package io.apiorchestrationservice.api.business.service;

import io.apiorchestrationservice.api.business.dto.inport.UserBalanceChargeCommand;
import io.apiorchestrationservice.api.business.dto.outport.BalanceChargeInfo;
import io.apiorchestrationservice.api.business.dto.outport.BalanceSearchInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface BalanceService {
	BalanceChargeInfo charge(UserBalanceChargeCommand command);

	BalanceSearchInfo retrieveBalance(Long userId);

}
