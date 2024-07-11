package io.paymentservice.api.balance.business.operators.balanceusemanager;

import io.paymentservice.api.balance.business.dto.inport.UserBalanceUseCommand;
import io.paymentservice.api.balance.business.dto.outport.UserBalanceUseInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public interface UserBalanceUseManager {
	UserBalanceUseInfo use(UserBalanceUseCommand command);
}
