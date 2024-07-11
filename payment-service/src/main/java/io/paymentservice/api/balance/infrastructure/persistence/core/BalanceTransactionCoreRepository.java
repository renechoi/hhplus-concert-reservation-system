package io.paymentservice.api.balance.infrastructure.persistence.core;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.paymentservice.api.balance.business.domainentity.BalanceTransaction;
import io.paymentservice.api.balance.business.persistence.BalanceTransactionRepository;
import io.paymentservice.api.balance.infrastructure.persistence.orm.BalanceTransactionJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
@Repository
@RequiredArgsConstructor
public class BalanceTransactionCoreRepository implements BalanceTransactionRepository {
	private final BalanceTransactionJpaRepository balanceTransactionJpaRepository;

	@Override
	public List<BalanceTransaction> findListByUserId(Long userId) {
		return balanceTransactionJpaRepository.findByUserId(userId);
	}

	@Override
	public BalanceTransaction save(BalanceTransaction balanceTransaction) {
		return balanceTransactionJpaRepository.save(balanceTransaction);
	}
}
