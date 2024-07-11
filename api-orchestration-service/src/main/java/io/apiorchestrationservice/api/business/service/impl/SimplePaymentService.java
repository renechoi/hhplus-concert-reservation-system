package io.apiorchestrationservice.api.business.service.impl;

import org.springframework.stereotype.Service;

import io.apiorchestrationservice.api.business.client.PaymentServiceClientAdapter;
import io.apiorchestrationservice.api.business.dto.inport.PaymentProcessCommand;
import io.apiorchestrationservice.api.business.dto.outport.PaymentInfo;
import io.apiorchestrationservice.api.business.service.PaymentService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Service
@RequiredArgsConstructor
public class SimplePaymentService implements PaymentService {

	private final PaymentServiceClientAdapter paymentServiceClientAdapter;

	@Override
	public PaymentInfo processPayment(PaymentProcessCommand command) {
		return paymentServiceClientAdapter.processPayment(command);
	}

	@Override
	public PaymentInfo cancelPayment(Long transactionId) {
		return paymentServiceClientAdapter.cancelPayment(transactionId);
	}



}
