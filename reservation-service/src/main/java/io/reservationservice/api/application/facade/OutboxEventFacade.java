package io.reservationservice.api.application.facade;

import static java.time.LocalDateTime.*;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import io.reservationservice.api.application.dto.request.OutboxStatusCompleteRequest;
import io.reservationservice.api.business.domainentity.OutboxEvent;
import io.reservationservice.api.business.service.OutboxEventService;
import io.reservationservice.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
@Facade
@RequiredArgsConstructor
public class OutboxEventFacade {
	private final OutboxEventService outboxEventService ;


	public void complete(OutboxStatusCompleteRequest outboxStatusCompleteRequest) {
		outboxEventService.complete(outboxStatusCompleteRequest.toCommand());
	}

	public void reSend() {
		outboxEventService.publishUnsentEvents();
	}
}
