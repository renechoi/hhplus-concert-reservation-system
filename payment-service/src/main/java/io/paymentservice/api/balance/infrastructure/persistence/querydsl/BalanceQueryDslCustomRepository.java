package io.paymentservice.api.balance.infrastructure.persistence.querydsl;

import java.util.Optional;

import io.paymentservice.api.balance.business.dto.inport.BalanceSearchCommand;
import io.paymentservice.api.balance.business.entity.Balance;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
public interface BalanceQueryDslCustomRepository {
	Optional<Balance> findSingleWithLock(BalanceSearchCommand searchCommand);
}
