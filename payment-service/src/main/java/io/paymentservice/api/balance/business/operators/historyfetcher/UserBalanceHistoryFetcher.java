package io.paymentservice.api.balance.business.operators.historyfetcher;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.paymentservice.api.balance.business.dto.outport.BalanceTransactionInfos;
import io.paymentservice.api.balance.business.persistence.BalanceTransactionRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

@Component
@RequiredArgsConstructor
public class UserBalanceHistoryFetcher {

	private final BalanceTransactionRepository transactionRepository;

	@Transactional(readOnly = true)
	public BalanceTransactionInfos getHistories(long userId) {
		return BalanceTransactionInfos.from(transactionRepository.findListByUserId(userId));
	}
}
