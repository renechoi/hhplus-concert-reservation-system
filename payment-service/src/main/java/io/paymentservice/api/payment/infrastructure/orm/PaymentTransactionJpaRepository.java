package io.paymentservice.api.payment.infrastructure.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.paymentservice.api.payment.business.entity.PaymentTransaction;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public interface PaymentTransactionJpaRepository extends JpaRepository<PaymentTransaction, Long> {
	List<PaymentTransaction> findAllByUserId(Long userId);
}
