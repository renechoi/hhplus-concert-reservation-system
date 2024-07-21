package io.paymentservice.api.balance.business.service.impl;

import org.springframework.stereotype.Service;

import io.paymentservice.api.balance.business.dto.inport.BalanceChargeCommand;
import io.paymentservice.api.balance.business.dto.inport.BalanceUseCommand;
import io.paymentservice.api.balance.business.dto.outport.BalanceTransactionInfos;
import io.paymentservice.api.balance.business.dto.outport.BalanceChargeInfo;
import io.paymentservice.api.balance.business.dto.outport.BalanceSearchInfo;
import io.paymentservice.api.balance.business.dto.outport.BalanceUseInfo;
import io.paymentservice.api.balance.business.operators.balancecharger.BalanceCharger;
import io.paymentservice.api.balance.business.operators.balanceusemanager.BalanceUseManager;
import io.paymentservice.api.balance.business.operators.historyfetcher.BalanceHistoryFetcher;
import io.paymentservice.api.balance.business.operators.reader.BalanceReader;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
@Service
@RequiredArgsConstructor
public class BalanceService {

	private final BalanceReader reader;
	private final BalanceCharger charger;
	private final BalanceUseManager balanceUseManager;
	private final BalanceHistoryFetcher historyFetcher;

	public BalanceChargeInfo charge(BalanceChargeCommand command) {
		return charger.charge(command);
	}

	public BalanceSearchInfo search(BalanceChargeCommand command) {
		return reader.search(command);
	}

	public BalanceUseInfo use(BalanceUseCommand command) {
		return balanceUseManager.use(command);
	}

	public BalanceTransactionInfos getHistories(long userId) {
		return historyFetcher.getHistories(userId);
	}
}
