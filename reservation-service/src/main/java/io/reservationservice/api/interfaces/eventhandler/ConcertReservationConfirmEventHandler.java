package io.reservationservice.api.interfaces.eventhandler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.reservationservice.api.application.facade.OutboxEventFacade;
import io.reservationservice.api.interfaces.stream.payload.ConcertReservationConfirmMessagePayload;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
@Component
@RequiredArgsConstructor
public class ConcertReservationConfirmEventHandler {
	private final OutboxEventFacade outboxEventFacade;

	@EventListener
	public void handleOutboxCompleteEvent(ConcertReservationConfirmMessagePayload event) {
		outboxEventFacade.complete(event.toOutboxStatusCompleteRequest());
	}

}
