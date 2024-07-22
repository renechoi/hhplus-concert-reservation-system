package io.paymentservice.api.balance.business.persistence;

import java.util.List;

import io.paymentservice.api.balance.business.entity.BalanceTransaction;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */

public interface BalanceTransactionRepository  {
	List<BalanceTransaction> findListByUserId(Long userId);

	BalanceTransaction save(BalanceTransaction transaction);
}