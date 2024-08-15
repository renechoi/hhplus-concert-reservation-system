package io.reservationservice.api.infrastructure.stream.strategy;

import io.reservationservice.api.business.domainentity.OutboxEvent;

/**
 * @author : Rene Choi
 * @since : 2024/08/11
 */
public interface OutboxEventStrategy {

	boolean supports(String eventType);
	void process(OutboxEvent outboxEvent);
}
