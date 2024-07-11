package io.paymentservice.api.balance.business.persistence;

import java.util.Optional;

import io.paymentservice.api.balance.business.domainentity.UserBalance;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public interface UserBalanceRepository {
	Optional<UserBalance> findByUserIdOptional(Long userId);

	UserBalance save(UserBalance userBalance);

	UserBalance findByUserIdWithThrows(Long userId);
}
