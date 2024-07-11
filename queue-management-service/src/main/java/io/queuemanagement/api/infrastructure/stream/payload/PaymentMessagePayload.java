package io.queuemanagement.api.infrastructure.stream.payload;

import io.queuemanagement.api.business.dto.event.PaymentCancelEvent;
import io.queuemanagement.api.business.dto.event.PaymentCompleteEvent;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Data
public class PaymentMessagePayload {
	private  String paymentTransactionId;
	private  String userId;

	public PaymentCompleteEvent toPaymentCompleteEvent(Object source) {
		return new PaymentCompleteEvent(source, this.paymentTransactionId, this.userId);
	}

	public PaymentCancelEvent toPaymentCancelEvent(Object source) {
		return new PaymentCancelEvent(source, this.paymentTransactionId, this.userId);
	}

}
