package io.reservationservice.api.business.service;

import io.reservationservice.api.business.dto.inport.ReservationCreateCommand;
import io.reservationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.reservationservice.api.business.dto.outport.ReservationStatusInfos;
import io.reservationservice.api.business.dto.outport.TemporalReservationCreateInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface ReservationCrudService {
	TemporalReservationCreateInfo createTemporalReservation(ReservationCreateCommand command);
	ReservationConfirmInfo confirmReservation(Long temporalReservationId);
	ReservationStatusInfos getReservationStatus(Long userId, Long concertOptionId);
	void cancelTemporalReservation(Long temporalReservationId);
	void cancelExpiredTemporalReservations();

}

