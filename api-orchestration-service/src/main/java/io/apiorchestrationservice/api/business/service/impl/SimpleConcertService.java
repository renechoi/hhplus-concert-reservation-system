package io.apiorchestrationservice.api.business.service.impl;

import org.springframework.stereotype.Service;

import io.apiorchestrationservice.api.business.client.ReservationServiceClientAdapter;
import io.apiorchestrationservice.api.business.dto.inport.AvailableSeatsRetrievalCommand;
import io.apiorchestrationservice.api.business.dto.inport.ConcertCreateCommand;
import io.apiorchestrationservice.api.business.dto.inport.ConcertOptionCreateCommand;
import io.apiorchestrationservice.api.business.dto.outport.AvailableDatesInfos;
import io.apiorchestrationservice.api.business.dto.outport.ConcertCreateInfo;
import io.apiorchestrationservice.api.business.dto.outport.ConcertOptionCreateInfo;
import io.apiorchestrationservice.api.business.service.ConcertService;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AvailableSeatsInfos;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Service
@RequiredArgsConstructor
public class SimpleConcertService implements ConcertService {

	private final ReservationServiceClientAdapter reservationServiceClientAdapter;

	@Override
	public ConcertCreateInfo createConcert(ConcertCreateCommand command) {
		return reservationServiceClientAdapter.createConcert(command);
	}

	@Override
	public ConcertOptionCreateInfo createConcertOption(Long concertId, ConcertOptionCreateCommand command) {
		return reservationServiceClientAdapter.createConcertOption(command.withConcertId(concertId));
	}

	@Override
	public AvailableDatesInfos getAvailableDates(Long concertOptionId) {
		return reservationServiceClientAdapter.getAvailableDates(concertOptionId);
	}

	@Override
	public AvailableSeatsInfos getAvailableSeats(AvailableSeatsRetrievalCommand command) {
		return reservationServiceClientAdapter.getAvailableSeats(command.getConcertOptionId(), command.getRequestAt());
	}
}
