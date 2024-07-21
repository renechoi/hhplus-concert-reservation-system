package io.paymentservice.api.payment.business.operators.processor;

import static io.paymentservice.api.balance.business.dto.inport.BalanceChargeCommand.*;
import static io.paymentservice.common.model.GlobalResponseCode.*;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.paymentservice.api.balance.business.operators.balancecharger.BalanceCharger;
import io.paymentservice.api.payment.business.domainentity.PaymentTransaction;
import io.paymentservice.api.payment.business.dto.outport.PaymentInfo;
import io.paymentservice.api.payment.business.persistence.PaymentTransactionRepository;
import io.paymentservice.common.exception.definitions.PaymentCancelUnAvailableException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Component
@RequiredArgsConstructor
public class PaymentCanceller {

	private final PaymentTransactionRepository paymentTransactionRepository;
	private final BalanceCharger balanceCharger;

	@Transactional
	public PaymentInfo cancel(Long transactionId) {
		PaymentTransaction transaction = paymentTransactionRepository.findByIdWithThrows(transactionId);

		if (transaction.isCancelled()) {
			throw new PaymentCancelUnAvailableException(PAYMENT_ALREADY_CANCELED, transactionId);
		}

		balanceCharger.charge(rollbackCommand(transaction.getUserId(), transaction.getAmount()));
		paymentTransactionRepository.save(transaction.doCancel());

		return PaymentInfo.from(transaction);
	}


}
