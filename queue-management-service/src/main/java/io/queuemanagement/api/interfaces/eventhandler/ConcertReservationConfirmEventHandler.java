package io.queuemanagement.api.interfaces.eventhandler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.queuemanagement.api.application.facade.ProcessingQueueFacade;
import io.queuemanagement.api.interfaces.stream.payload.ConcertReservationConfirmMessagePayload;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/22
 */
@Component
@RequiredArgsConstructor
public class ConcertReservationConfirmEventHandler {
	private final ProcessingQueueFacade processingQueueFacade;

	@EventListener
	public void handleConfirmCompleteEvent(ConcertReservationConfirmMessagePayload event) {
		processingQueueFacade.completeToken(event.toCompletedTokenHandlingRequest());
	}

}
