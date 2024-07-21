package io.paymentservice.api.balance.infrastructure.persistence.orm;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.paymentservice.api.balance.business.domainentity.Balance;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public interface BalanceJpaRepository extends JpaRepository<Balance, Long> {
	Optional<Balance> findByUserId(Long userId);
}
