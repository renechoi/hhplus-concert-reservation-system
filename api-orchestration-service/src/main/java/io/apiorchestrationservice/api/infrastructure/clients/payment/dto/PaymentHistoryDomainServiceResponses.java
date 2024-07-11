package io.apiorchestrationservice.api.infrastructure.clients.payment.dto;

import java.util.List;

import io.apiorchestrationservice.api.business.dto.outport.PaymentHistoryInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record PaymentHistoryDomainServiceResponses(
    List<PaymentHistoryDomainServiceResponse> paymentHistoryResponses
) {
	public PaymentHistoryInfos toPaymentHistoryInfo() {
		return new PaymentHistoryInfos
			(paymentHistoryResponses.stream().map(PaymentHistoryDomainServiceResponse::toPaymentHistoryInfo).toList());
	}
}
