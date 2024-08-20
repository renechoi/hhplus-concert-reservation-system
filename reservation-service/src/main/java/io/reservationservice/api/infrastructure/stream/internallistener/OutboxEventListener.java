package io.reservationservice.api.infrastructure.stream.internallistener;

import static org.springframework.transaction.event.TransactionPhase.*;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import io.reservationservice.api.business.domainentity.OutboxEvent;
import io.reservationservice.api.infrastructure.stream.strategy.OutboxEventStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/08/11
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventListener {

	private final List<OutboxEventStrategy> strategies;

	@TransactionalEventListener(phase = AFTER_COMMIT)
	public void handleOutboxEvent(OutboxEvent outboxEvent) {
		strategies.stream()
			.filter(strategy -> strategy.supports(outboxEvent.getEventType()))
			.findFirst()
			.ifPresentOrElse(strategy -> strategy.process(outboxEvent), () -> log.warn("No strategy found for event type: {}", outboxEvent.getEventType()));
	}

}
