package io.paymentservice.api.balance.business.service.impl;

import org.springframework.stereotype.Service;

import io.paymentservice.api.balance.business.dto.inport.UserBalanceChargeCommand;
import io.paymentservice.api.balance.business.dto.inport.UserBalanceUseCommand;
import io.paymentservice.api.balance.business.dto.outport.BalanceTransactionInfos;
import io.paymentservice.api.balance.business.dto.outport.UserBalanceChargeInfo;
import io.paymentservice.api.balance.business.dto.outport.UserBalanceSearchInfo;
import io.paymentservice.api.balance.business.dto.outport.UserBalanceUseInfo;
import io.paymentservice.api.balance.business.operators.balancecharger.UserBalanceCharger;
import io.paymentservice.api.balance.business.operators.balanceusemanager.UserBalanceUseManager;
import io.paymentservice.api.balance.business.operators.historyfetcher.UserBalanceHistoryFetcher;
import io.paymentservice.api.balance.business.operators.reader.UserBalanceReader;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
@Service
@RequiredArgsConstructor
public class UserBalanceService  {

	private final UserBalanceReader reader;
	private final UserBalanceCharger charger;
	private final UserBalanceUseManager userBalanceUseManager;
	private final UserBalanceHistoryFetcher historyFetcher;

	public UserBalanceChargeInfo charge(UserBalanceChargeCommand command) {
		return charger.charge(command);
	}

	public UserBalanceSearchInfo search(UserBalanceChargeCommand command) {
		return reader.search(command);
	}

	public UserBalanceUseInfo use(UserBalanceUseCommand command) {
		return userBalanceUseManager.use(command);
	}

	public BalanceTransactionInfos getHistories(long userId) {
		return historyFetcher.getHistories(userId);
	}
}
