package io.paymentservice.api.payment.business.service;

import io.paymentservice.api.payment.business.dto.inport.PaymentCommand;
import io.paymentservice.api.payment.business.dto.outport.PaymentInfo;
import io.paymentservice.api.payment.business.dto.outport.PaymentInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public interface PaymentService {
	PaymentInfo processPayment(PaymentCommand paymentCommand);
	PaymentInfo cancelPayment(Long transactionId);
	PaymentInfos retrievePaymentHistory(Long userId);

}
