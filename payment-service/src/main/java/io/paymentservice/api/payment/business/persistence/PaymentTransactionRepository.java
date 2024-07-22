package io.paymentservice.api.payment.business.persistence;

import java.util.List;

import io.paymentservice.api.payment.business.entity.PaymentTransaction;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public interface PaymentTransactionRepository {
	PaymentTransaction save(PaymentTransaction paymentTransaction);

	List<PaymentTransaction> findListByUserId(Long userId);

	PaymentTransaction findById(Long transactionId);

}
