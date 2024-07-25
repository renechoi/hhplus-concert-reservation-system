package io.apiorchestrationservice.api.application.facade;

import io.apiorchestrationservice.api.application.dto.request.ReservationConfirmRequest;
import io.apiorchestrationservice.api.application.dto.request.ReservationCreateRequest;
import io.apiorchestrationservice.api.application.dto.response.ReservationConfirmResponse;
import io.apiorchestrationservice.api.application.dto.response.ReservationCreateResponse;
import io.apiorchestrationservice.api.application.dto.response.ReservationStatusResponses;
import io.apiorchestrationservice.api.business.service.ReservationService;
import io.apiorchestrationservice.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Facade
@RequiredArgsConstructor
public class ReservationCrudFacade {
	private final ReservationService reservationService;

	public ReservationCreateResponse createTemporalReservation(ReservationCreateRequest request) {
		return ReservationCreateResponse.from(reservationService.createTemporalReservation(request.toCommand()));
	}


	public ReservationConfirmResponse confirmReservation(ReservationConfirmRequest request) {
		return ReservationConfirmResponse.from(reservationService.confirmReservation(request.toCommand()));
	}

	public ReservationStatusResponses getReservationStatus(Long userId, Long concertOptionId) {
		return ReservationStatusResponses.from(reservationService.getReservationStatus(userId, concertOptionId));
	}
}
