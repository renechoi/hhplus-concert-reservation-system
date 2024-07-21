package io.queuemanagement.api.interfaces.eventhandler;

import io.queuemanagement.api.business.dto.event.PaymentCancelEvent;
import io.queuemanagement.api.business.dto.event.PaymentCompleteEvent;

/**
 * @author : Rene Choi
 * @since : 2024/07/22
 */
public interface PaymentEventHandler {
	void handlePaymentCompleteEvent(PaymentCompleteEvent event);
	void handlePaymentCancelEvent(PaymentCancelEvent event);
}
