package io.paymentservice.api.payment.business.service.impl;

import org.springframework.stereotype.Service;

import io.paymentservice.api.payment.business.aspect.PaymentTransactionAspect;
import io.paymentservice.api.payment.business.dto.inport.PaymentCommand;
import io.paymentservice.api.payment.business.dto.outport.PaymentInfo;
import io.paymentservice.api.payment.business.dto.outport.PaymentInfos;
import io.paymentservice.api.payment.business.operators.processor.PaymentCanceller;
import io.paymentservice.api.payment.business.operators.processor.PaymentHistoryReader;
import io.paymentservice.api.payment.business.operators.processor.PaymentProcessor;
import io.paymentservice.api.payment.business.service.PaymentService;
import io.paymentservice.common.exception.definitions.PaymentHistoryNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
@Service
@RequiredArgsConstructor
public class SimplePaymentService implements PaymentService {

	private final PaymentProcessor paymentProcessor;
	private final PaymentHistoryReader paymentHistoryReader;
	private final PaymentCanceller paymentCanceller;

	/**
	 * 결제 처리 프로세스
	 * - 금액 차감 (롤백 로직 포함) -> aspect
	 * - 중복 결제 불가 -> db unique constraints
	 * @see {@link PaymentTransactionAspect}
	 */
	@Override
	public PaymentInfo processPayment(PaymentCommand paymentCommand) {
		return paymentProcessor.process(paymentCommand);
	}

	@Override
	public PaymentInfo cancelPayment(Long transactionId) {
		return paymentCanceller.cancel(transactionId);
	}

	@Override
	public PaymentInfos retrievePaymentHistory(Long userId) {
		return paymentHistoryReader.retrievePaymentHistory(userId);
	}


}