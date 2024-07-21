package io.paymentservice.api.balance.business.persistence;

import java.util.Optional;

import io.paymentservice.api.balance.business.domainentity.Balance;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public interface BalanceRepository {
	Optional<Balance> findByUserIdOptional(Long userId);

	Balance save(Balance balance);

	Balance findByUserIdWithThrows(Long userId);
}
