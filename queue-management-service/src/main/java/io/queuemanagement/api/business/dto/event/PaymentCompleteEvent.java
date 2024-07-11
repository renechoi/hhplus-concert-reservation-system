package io.queuemanagement.api.business.dto.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Getter
public class PaymentCompleteEvent extends ApplicationEvent {
	private final String paymentTransactionId;
	private final String userId;

	public PaymentCompleteEvent(Object source, String paymentTransactionId, String userId) {
		super(source);
		this.paymentTransactionId = paymentTransactionId;
		this.userId= userId;

	}
}
