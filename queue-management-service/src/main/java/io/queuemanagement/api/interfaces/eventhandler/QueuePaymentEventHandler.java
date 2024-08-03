package io.queuemanagement.api.interfaces.eventhandler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.queuemanagement.api.application.facade.ProcessingQueueFacade;
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
	private final ProcessingQueueFacade processingQueueFacade;

	@Override
	@EventListener
	public void handlePaymentCompleteEvent(PaymentCompleteEvent event) {
		processingQueueFacade.completeToken(event.toCompletedTokenHandlingRequest());
	}

	@Override
	@Description(value = "in the meantime, do nothing -> 결제 취소가 되더라도 대기열을 회복시켜줄 필요는 없다. 따라서 대기열에서 결제 취소에 대한 이벤트 핸들링은 불필요")
	public void handlePaymentCancelEvent(PaymentCancelEvent event) {
	}
}
