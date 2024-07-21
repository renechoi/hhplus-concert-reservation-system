package io.queuemanagement.api.interfaces.eventhandler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.queuemanagement.api.application.facade.QueueManagementFacade;
import io.queuemanagement.api.business.dto.event.PaymentCancelEvent;
import io.queuemanagement.api.business.dto.event.PaymentCompleteEvent;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/22
 */
@Component
@RequiredArgsConstructor
public class QueuePaymentEventHandler implements PaymentEventHandler{
	private final QueueManagementFacade queueManagementFacade;

	@Override
	@EventListener
	public void handlePaymentCompleteEvent(PaymentCompleteEvent event) {
		queueManagementFacade.completeTokens(event.getUserId());
	}

	@Override
	@Description(value = "in the meantime, do nothing")
	public void handlePaymentCancelEvent(PaymentCancelEvent event) {
	}
}
