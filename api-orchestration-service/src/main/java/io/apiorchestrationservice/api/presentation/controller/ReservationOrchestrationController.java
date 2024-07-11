package io.apiorchestrationservice.api.presentation.controller;

import static io.apiorchestrationservice.common.model.CommonApiResponse.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.apiorchestrationservice.api.application.dto.request.AvailableSeatsRetrievalRequest;
import io.apiorchestrationservice.api.application.dto.request.ConcertCreateRequest;
import io.apiorchestrationservice.api.application.dto.request.ConcertOptionCreateRequest;
import io.apiorchestrationservice.api.application.dto.request.ReservationConfirmRequest;
import io.apiorchestrationservice.api.application.dto.request.ReservationCreateRequest;
import io.apiorchestrationservice.api.application.dto.response.AvailableDatesResponses;
import io.apiorchestrationservice.api.application.dto.response.AvailableSeatsResponses;
import io.apiorchestrationservice.api.application.dto.response.ConcertCreateResponse;
import io.apiorchestrationservice.api.application.dto.response.ConcertOptionCreateResponse;
import io.apiorchestrationservice.api.application.dto.response.ReservationConfirmResponse;
import io.apiorchestrationservice.api.application.dto.response.ReservationCreateResponse;
import io.apiorchestrationservice.api.application.dto.response.ReservationStatusResponses;
import io.apiorchestrationservice.api.application.facade.ConcertFacade;
import io.apiorchestrationservice.api.application.facade.ReservationCrudFacade;
import io.apiorchestrationservice.common.annotation.ValidatedToken;
import io.apiorchestrationservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationOrchestrationController {

	private final ReservationCrudFacade reservationCrudFacade;
	private final ConcertFacade concertFacade;


	@PostMapping("/concerts")
	@Operation(summary = "콘서트 생성")
	public CommonApiResponse<ConcertCreateResponse> createConcert(@RequestBody ConcertCreateRequest request) {
		return created(concertFacade.createConcert(request));
	}

	@PostMapping("/concerts/{concertId}/options")
	@Operation(summary = "콘서트 옵션 생성")
	public CommonApiResponse<ConcertOptionCreateResponse> createConcertOption(@PathVariable Long concertId, @RequestBody @Validated ConcertOptionCreateRequest request) {
		return created(concertFacade.createConcertOption(concertId, request));
	}




	@ValidatedToken
	@GetMapping("/available-dates/{concertOptionId}")
	@Operation(summary = "예약 가능한 날짜 목록을 조회")
	public CommonApiResponse<AvailableDatesResponses> getAvailableDates(  @PathVariable Long concertOptionId) {
		return OK(concertFacade.getAvailableDates(concertOptionId));
	}


	@ValidatedToken
	@GetMapping("/available-seats")
	@Operation(summary = "특정 날짜의 예약 가능한 좌석 목록을 조회")
	public CommonApiResponse<AvailableSeatsResponses> getAvailableSeats(  @ModelAttribute AvailableSeatsRetrievalRequest request) {
		return OK(concertFacade.getAvailableSeats(request));
	}






	@ValidatedToken
	@PostMapping
	@Operation(summary = "콘서트 좌석 예약 - 임시 예약")
	public CommonApiResponse<ReservationCreateResponse> reserveSeat(@RequestBody ReservationCreateRequest request) {
		return OK(reservationCrudFacade.createTemporaryReservation(request));
	}



	@PostMapping("/confirm")
	@Operation(summary = "임시 예약 확정")
	public CommonApiResponse<ReservationConfirmResponse> confirmReservation(@RequestBody ReservationConfirmRequest request) {
		return OK(reservationCrudFacade.confirmReservation(request));
	}

	@GetMapping("/status/{userId}/{concertOptionId}")
	@Operation(summary = "예약 상태 조회")
	public CommonApiResponse<ReservationStatusResponses> getReservationStatus(@PathVariable Long userId, @PathVariable Long concertOptionId) {
		return OK(reservationCrudFacade.getReservationStatus(userId, concertOptionId));
	}



}
