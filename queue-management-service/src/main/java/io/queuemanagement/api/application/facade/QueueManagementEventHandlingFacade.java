package io.queuemanagement.api.application.facade;

import org.springframework.context.event.EventListener;

import io.queuemanagement.api.business.dto.event.PaymentCancelEvent;
import io.queuemanagement.api.business.dto.event.PaymentCompleteEvent;
import io.queuemanagement.api.business.service.QueueManagementService;
import io.queuemanagement.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Facade
@RequiredArgsConstructor
public class QueueManagementEventHandlingFacade {

	private final QueueManagementService queueManagementService;

	@EventListener
	public void handlePaymentCompleteEvent(PaymentCompleteEvent event) {
		queueManagementService.completeProcessingQueueToken(event.getUserId());
		queueManagementService.completeWaitingQueueTokenByUserId(event.getUserId());
	}

	@EventListener
	public void handlePaymentCancelEvent(PaymentCancelEvent event) {
	}
}