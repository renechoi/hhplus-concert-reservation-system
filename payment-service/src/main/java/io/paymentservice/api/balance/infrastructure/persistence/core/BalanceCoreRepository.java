package io.paymentservice.api.balance.infrastructure.persistence.core;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import io.paymentservice.api.balance.business.domainentity.Balance;
import io.paymentservice.api.balance.business.persistence.BalanceRepository;
import io.paymentservice.api.balance.infrastructure.persistence.orm.BalanceJpaRepository;
import io.paymentservice.common.exception.definitions.BalanceNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
@Repository
@RequiredArgsConstructor
public class BalanceCoreRepository implements BalanceRepository {
	private final BalanceJpaRepository balanceJpaRepository;

	@Override
	public Optional<Balance> findByUserIdOptional(Long userId) {
		return balanceJpaRepository.findByUserId(userId);
	}

	@Override
	public Balance save(Balance balance) {
		return balanceJpaRepository.save(balance);
	}

	@Override
	public Balance findByUserIdWithThrows(Long userId) {
		return balanceJpaRepository.findByUserId(userId).orElseThrow(BalanceNotFoundException::new);
	}
}
