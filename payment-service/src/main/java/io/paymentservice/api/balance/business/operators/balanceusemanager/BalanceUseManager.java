package io.paymentservice.api.balance.business.operators.balanceusemanager;

import io.paymentservice.api.balance.business.dto.inport.BalanceUseCommand;
import io.paymentservice.api.balance.business.dto.outport.BalanceUseInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public interface BalanceUseManager {
	BalanceUseInfo use(BalanceUseCommand command);
}
