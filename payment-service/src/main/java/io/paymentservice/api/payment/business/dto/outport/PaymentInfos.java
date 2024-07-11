package io.paymentservice.api.payment.business.dto.outport;

import java.util.List;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record PaymentInfos(List<PaymentInfo> paymentInfos) {
	public boolean isEmpty() {
		return this.paymentInfos==null || this.paymentInfos.isEmpty();
	}
}
