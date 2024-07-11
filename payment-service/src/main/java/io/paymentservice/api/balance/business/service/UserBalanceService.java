package io.paymentservice.api.balance.business.service;

import io.paymentservice.api.balance.business.dto.inport.UserBalanceChargeCommand;
import io.paymentservice.api.balance.business.dto.inport.UserBalanceUseCommand;
import io.paymentservice.api.balance.business.dto.outport.BalanceTransactionInfos;
import io.paymentservice.api.balance.business.dto.outport.UserBalanceChargeInfo;
import io.paymentservice.api.balance.business.dto.outport.UserBalanceSearchInfo;
import io.paymentservice.api.balance.business.dto.outport.UserBalanceUseInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public interface UserBalanceService {

	UserBalanceChargeInfo charge(UserBalanceChargeCommand command);
	UserBalanceSearchInfo search(UserBalanceChargeCommand userBalanceChargeCommand);

	UserBalanceUseInfo use(UserBalanceUseCommand command);
	BalanceTransactionInfos getHistories(long userId);
}
