package io.paymentservice.api.payment.interfaces.dto.response;

import java.util.List;

import io.paymentservice.api.payment.business.dto.outport.PaymentInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record PaymentResponses(List<PaymentResponse> paymentResponses) {

	public static PaymentResponses from(PaymentInfos paymentInfos) {
		List<PaymentResponse> paymentResponseList = paymentInfos.paymentInfos().stream()
			.map(PaymentResponse::from)
			.toList();
		return new PaymentResponses(paymentResponseList);
	}
}
