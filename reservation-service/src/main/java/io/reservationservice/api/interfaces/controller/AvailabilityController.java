package io.reservationservice.api.interfaces.controller;

import static io.reservationservice.common.model.CommonApiResponse.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.reservationservice.api.application.dto.response.AvailableDatesResponses;
import io.reservationservice.api.application.dto.response.AvailableSeatsResponses;
import io.reservationservice.api.application.facade.AvailabilityFacade;
import io.reservationservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
@Tag(name = "예약 가능 날짜 / 좌석 API")
public class AvailabilityController {

	private final AvailabilityFacade availabilityFacade;

	@GetMapping("/dates/{concertId}")
	@Operation(summary = "예약 가능 날짜 조회")
	public CommonApiResponse<AvailableDatesResponses> getAvailableDates(@PathVariable Long concertId) {
		return OK(availabilityFacade.getAvailableDates(concertId));
	}


	@GetMapping("/seats/{concertOptionId}/{requestAt}")
	@Operation(summary = "예약 가능 좌석 조회")
	public CommonApiResponse<AvailableSeatsResponses> getAvailableSeats(@PathVariable Long concertOptionId, @PathVariable Long requestAt) {
		return OK(availabilityFacade.getAvailableSeats(concertOptionId, requestAt));
	}
}
