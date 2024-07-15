package io.reservationservice.api.application.facade;

import io.reservationservice.api.application.dto.request.ReservationConfirmRequest;
import io.reservationservice.api.application.dto.request.ReservationCreateRequest;
import io.reservationservice.api.application.dto.response.ReservationConfirmResponse;
import io.reservationservice.api.application.dto.response.ReservationStatusResponses;
import io.reservationservice.api.application.dto.response.TemporaryReservationCreateResponse;
import io.reservationservice.api.business.service.ReservationCrudService;
import io.reservationservice.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Facade
@RequiredArgsConstructor
public class ReservationFacade {

	private final ReservationCrudService reservationCrudService;

	public TemporaryReservationCreateResponse createTemporaryReservation(ReservationCreateRequest request) {
		return TemporaryReservationCreateResponse.from(reservationCrudService.createTemporaryReservation(request.toCommand()));
	}


	public ReservationConfirmResponse confirmReservation(ReservationConfirmRequest request) {
		return ReservationConfirmResponse.from(reservationCrudService.confirmReservation(request.getTemporaryReservationId()));
	}

	public ReservationStatusResponses getReservationStatus(Long userId, Long concertOptionId) {
		return reservationCrudService.getReservationStatus(userId, concertOptionId);
	}
}
