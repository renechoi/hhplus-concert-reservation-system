package io.paymentservice.api.balance.business.service.impl;

import org.springframework.stereotype.Service;

import io.paymentservice.api.balance.business.dto.inport.BalanceChargeCommand;
import io.paymentservice.api.balance.business.dto.inport.BalanceUseCommand;
import io.paymentservice.api.balance.business.dto.outport.BalanceChargeInfo;
import io.paymentservice.api.balance.business.dto.outport.BalanceSearchInfo;
import io.paymentservice.api.balance.business.dto.outport.BalanceTransactionInfos;
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

	/**
	 * 동시 요청을 전부 수용하는 경우, 적은 요청에 한해 optimistic locking + retry 로직을 적용할 수 있다.
	 * @param command
	 * @return
	 */
	// @Retryable(
	// 	value = { ObjectOptimisticLockingFailureException.class },
	// 	maxAttempts = 10,
	// 	backoff = @Backoff(delay = 1000)
	// )
	public BalanceChargeInfo charge(BalanceChargeCommand command) {
		return charger.charge(command);
	}

	public BalanceSearchInfo search(BalanceChargeCommand command) {
		return reader.search(command);
	}

	/**
	 * 동시 요청을 전부 수용하는 경우, 적은 요청에 한해 optimistic locking + retry 로직을 적용할 수 있다.
	 * @param command
	 * @return
	 */
	// @Retryable(
	// 	value = { ObjectOptimisticLockingFailureException.class },
	// 	maxAttempts = 10,
	// 	backoff = @Backoff(delay = 1000)
	// )
	public BalanceUseInfo use(BalanceUseCommand command) {
		return balanceUseManager.use(command);
	}

	public BalanceTransactionInfos getHistories(long userId) {
		return historyFetcher.getHistories(userId);
	}
}
