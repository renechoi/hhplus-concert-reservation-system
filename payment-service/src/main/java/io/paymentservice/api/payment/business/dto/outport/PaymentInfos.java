package io.paymentservice.api.payment.business.dto.outport;

import java.util.List;

import io.paymentservice.common.exception.definitions.PaymentHistoryNotFoundException;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record PaymentInfos(List<PaymentInfo> paymentInfos) {
	public boolean isEmpty() {
		return this.paymentInfos==null || this.paymentInfos.isEmpty();
	}

	public PaymentInfos withValidated() {
		if (paymentInfos.isEmpty()) {
			throw new PaymentHistoryNotFoundException();
		}
		return this;
	}
}
