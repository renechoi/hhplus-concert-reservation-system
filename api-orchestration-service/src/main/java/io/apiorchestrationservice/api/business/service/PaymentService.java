package io.apiorchestrationservice.api.business.service;

import io.apiorchestrationservice.api.business.dto.inport.PaymentProcessCommand;
import io.apiorchestrationservice.api.business.dto.outport.PaymentInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface PaymentService {
	PaymentInfo processPayment(PaymentProcessCommand command);

	PaymentInfo cancelPayment(Long transactionId);
}
