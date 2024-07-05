package io.apiorchestrationservice.api.infrastructure.persistence.core;

import org.springframework.stereotype.Repository;

import io.apiorchestrationservice.api.business.persistence.BalanceChargeRepository;
import io.apiorchestrationservice.api.infrastructure.persistence.orm.BalanceJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Repository
@RequiredArgsConstructor
public class SimpleBalanceChargeRepository implements BalanceChargeRepository {
	private final BalanceJpaRepository balanceJpaRepository;
}
