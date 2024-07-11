package io.apiorchestrationservice.api.business.client;

import io.apiorchestrationservice.api.business.dto.inport.ConcertCreateCommand;
import io.apiorchestrationservice.api.business.dto.inport.ConcertOptionCreateCommand;
import io.apiorchestrationservice.api.business.dto.inport.ReservationConfirmCommand;
import io.apiorchestrationservice.api.business.dto.inport.TemporaryReservationCreateCommand;
import io.apiorchestrationservice.api.business.dto.outport.AvailableDatesInfos;
import io.apiorchestrationservice.api.business.dto.outport.ConcertCreateInfo;
import io.apiorchestrationservice.api.business.dto.outport.ConcertOptionCreateInfo;
import io.apiorchestrationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.apiorchestrationservice.api.business.dto.outport.ReservationStatusInfos;
import io.apiorchestrationservice.api.business.dto.outport.TemporaryReservationCreateInfo;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AvailableSeatsInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface ReservationServiceClientAdapter {
	ConcertCreateInfo createConcert(ConcertCreateCommand command);

	ConcertOptionCreateInfo createConcertOption(ConcertOptionCreateCommand command);

	TemporaryReservationCreateInfo createTemporaryReservation(TemporaryReservationCreateCommand command);

	ReservationConfirmInfo confirmReservation(ReservationConfirmCommand command);

	ReservationStatusInfos getReservationStatus(Long userId, Long concertOptionId);

	AvailableDatesInfos getAvailableDates(Long concertId);

	AvailableSeatsInfos getAvailableSeats(Long concertOptionId, Long requestAt);
}
