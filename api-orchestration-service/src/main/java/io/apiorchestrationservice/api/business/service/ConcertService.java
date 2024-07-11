package io.apiorchestrationservice.api.business.service;

import io.apiorchestrationservice.api.business.dto.inport.AvailableSeatsRetrievalCommand;
import io.apiorchestrationservice.api.business.dto.inport.ConcertCreateCommand;
import io.apiorchestrationservice.api.business.dto.inport.ConcertOptionCreateCommand;
import io.apiorchestrationservice.api.business.dto.outport.AvailableDatesInfos;
import io.apiorchestrationservice.api.business.dto.outport.ConcertCreateInfo;
import io.apiorchestrationservice.api.business.dto.outport.ConcertOptionCreateInfo;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AvailableSeatsInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public interface ConcertService {
	ConcertCreateInfo createConcert(ConcertCreateCommand command);

	ConcertOptionCreateInfo createConcertOption(Long concertId, ConcertOptionCreateCommand command);

	AvailableDatesInfos getAvailableDates(Long concertOptionId);

	AvailableSeatsInfos getAvailableSeats(AvailableSeatsRetrievalCommand command);
}
