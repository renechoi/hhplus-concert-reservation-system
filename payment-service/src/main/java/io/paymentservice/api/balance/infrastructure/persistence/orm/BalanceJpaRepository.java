package io.paymentservice.api.balance.infrastructure.persistence.orm;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.paymentservice.api.balance.business.entity.Balance;
import io.paymentservice.api.balance.infrastructure.persistence.querydsl.BalanceQueryDslCustomRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public interface BalanceJpaRepository extends JpaRepository<Balance, Long>, BalanceQueryDslCustomRepository {
	Optional<Balance> findByUserId(Long userId);
}
