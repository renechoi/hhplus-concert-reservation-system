package io.paymentservice.api.balance.infrastructure.persistence.core;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import io.paymentservice.api.balance.business.domainentity.UserBalance;
import io.paymentservice.api.balance.business.persistence.UserBalanceRepository;
import io.paymentservice.api.balance.infrastructure.persistence.orm.UserBalanceJpaRepository;
import io.paymentservice.common.exception.UserBalanceNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
@Repository
@RequiredArgsConstructor
public class UserBalanceCoreRepository implements UserBalanceRepository {
	private final UserBalanceJpaRepository userBalanceJpaRepository;

	@Override
	public Optional<UserBalance> findByUserIdOptional(Long userId) {
		return userBalanceJpaRepository.findByUserId(userId);
	}

	@Override
	public UserBalance save(UserBalance userBalance) {
		return userBalanceJpaRepository.save(userBalance);
	}

	@Override
	public UserBalance findByUserIdWithThrows(Long userId) {
		return userBalanceJpaRepository.findByUserId(userId).orElseThrow(UserBalanceNotFoundException::new);
	}
}
