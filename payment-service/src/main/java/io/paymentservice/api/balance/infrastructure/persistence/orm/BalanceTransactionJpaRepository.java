package io.paymentservice.api.balance.infrastructure.persistence.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.paymentservice.api.balance.business.domainentity.BalanceTransaction;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
public interface BalanceTransactionJpaRepository extends JpaRepository<BalanceTransaction, Long> {
	List<BalanceTransaction> findByUserId(Long userId);
}
