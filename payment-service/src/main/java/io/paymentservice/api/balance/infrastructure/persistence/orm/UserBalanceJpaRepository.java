package io.paymentservice.api.balance.infrastructure.persistence.orm;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.paymentservice.api.balance.business.domainentity.UserBalance;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public interface UserBalanceJpaRepository extends JpaRepository<UserBalance, Long> {
	Optional<UserBalance> findByUserId(Long userId);
}
