package io.reservationservice.api.presentation.controller;

import static io.reservationservice.common.model.CommonApiResponse.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.reservationservice.api.application.dto.request.ReservationConfirmRequest;
import io.reservationservice.api.application.dto.request.ReservationCreateRequest;
import io.reservationservice.api.application.dto.response.ReservationConfirmResponse;
import io.reservationservice.api.application.dto.response.ReservationStatusResponses;
import io.reservationservice.api.application.dto.response.TemporaryReservationCreateResponse;
import io.reservationservice.api.application.facade.ReservationFacade;
import io.reservationservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "예약 API")
public class ReservationController {
	private final ReservationFacade reservationFacade;

	@PostMapping
	@Operation(summary = "예약 요청 - 임시 예약을 수행함")
	public CommonApiResponse<TemporaryReservationCreateResponse> createTemporaryReservation(@RequestBody ReservationCreateRequest request) {
		return created(reservationFacade.createTemporaryReservation(request));
	}


	@PostMapping("/confirm")
	@Operation(summary = "임시 예약 확정")
	public CommonApiResponse<ReservationConfirmResponse> confirmReservation(@RequestBody ReservationConfirmRequest request) {
		return OK(reservationFacade.confirmReservation(request));
	}

	@GetMapping("/status/{userId}/{concertOptionId}")
	@Operation(summary = "예약 상태 조회")
	public CommonApiResponse<ReservationStatusResponses> getReservationStatus(@PathVariable Long userId, @PathVariable Long concertOptionId) {
		return OK(reservationFacade.getReservationStatus(userId, concertOptionId));
	}





}
