package io.paymentservice.api.payment.business.operators.processor;

import static io.paymentservice.api.balance.business.dto.inport.BalanceChargeCommand.*;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.paymentservice.api.balance.business.operators.balancecharger.BalanceCharger;
import io.paymentservice.api.payment.business.entity.PaymentTransaction;
import io.paymentservice.api.payment.business.dto.outport.PaymentInfo;
import io.paymentservice.api.payment.business.persistence.PaymentTransactionRepository;
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
		PaymentTransaction transaction = paymentTransactionRepository.findById(transactionId).assertNotCanceled();

		balanceCharger.charge(rollbackCommand(transaction.getUserId(), transaction.getAmount()));
		paymentTransactionRepository.save(transaction.doCancel());

		return PaymentInfo.from(transaction);
	}


}
