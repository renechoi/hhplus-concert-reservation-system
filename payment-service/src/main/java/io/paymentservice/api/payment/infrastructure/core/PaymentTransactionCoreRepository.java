package io.paymentservice.api.payment.infrastructure.core;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.paymentservice.api.payment.business.entity.PaymentTransaction;
import io.paymentservice.api.payment.business.persistence.PaymentTransactionRepository;
import io.paymentservice.api.payment.infrastructure.orm.PaymentTransactionJpaRepository;
import io.paymentservice.common.exception.definitions.PaymentTransactionNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
@Repository
@RequiredArgsConstructor
public class PaymentTransactionCoreRepository  implements PaymentTransactionRepository{
	private final PaymentTransactionJpaRepository paymentTransactionJpaRepository;

	@Override
	public PaymentTransaction save(PaymentTransaction paymentTransaction) {
		return paymentTransactionJpaRepository.save(paymentTransaction);
	}

	@Override
	public List<PaymentTransaction> getPayment(Long userId) {
		return paymentTransactionJpaRepository.findAllByUserId(userId);
	}

	@Override
	public PaymentTransaction fetch(Long transactionId) {
		return paymentTransactionJpaRepository.findById(transactionId).orElseThrow(PaymentTransactionNotFoundException::new);
	}
}
