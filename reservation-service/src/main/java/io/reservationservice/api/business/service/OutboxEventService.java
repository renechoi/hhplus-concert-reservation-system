package io.reservationservice.api.business.service;

import org.springframework.transaction.annotation.Transactional;

import io.reservationservice.api.business.dto.inport.OutboxStatusCompleteCommand;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
public interface OutboxEventService {
	void complete(OutboxStatusCompleteCommand command);

	@Transactional
	void publishUnsentEvents();
}
