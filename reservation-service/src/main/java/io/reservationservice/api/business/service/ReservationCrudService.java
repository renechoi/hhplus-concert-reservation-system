package io.reservationservice.api.business.service;

import org.springframework.transaction.annotation.Transactional;

import io.reservationservice.api.application.dto.response.ReservationStatusResponses;
import io.reservationservice.api.business.dto.inport.ReservationCreateCommand;
import io.reservationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.reservationservice.api.business.dto.outport.TemporaryReservationCreateInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface ReservationCrudService {
	TemporaryReservationCreateInfo createTemporaryReservation(ReservationCreateCommand command);

	@Transactional
	ReservationConfirmInfo confirmReservation(Long temporaryReservationId);

	ReservationStatusResponses getReservationStatus(Long userId, Long concertOptionId);
	@Transactional
	void cancelTemporaryReservation(Long temporaryReservationId);

}

