package io.paymentservice.api.balance.business.persistence;

import java.util.Optional;

import io.paymentservice.api.balance.business.dto.inport.BalanceSearchCommand;
import io.paymentservice.api.balance.business.entity.Balance;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public interface BalanceRepository {
	Optional<Balance> findByUserIdOptional(Long userId);

	Balance findSingleWithLock(BalanceSearchCommand balanceSearchCommand);

	Balance save(Balance balance);

	Balance findByUserId(Long userId);
}
