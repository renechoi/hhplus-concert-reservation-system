package io.apiorchestrationservice.api.presentation.controller;

import static io.apiorchestrationservice.api.application.dto.response.SeatReservationResponse.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.apiorchestrationservice.api.application.dto.request.AvailableSeatsRetrievalRequest;
import io.apiorchestrationservice.api.application.dto.request.SeatReservationRequest;
import io.apiorchestrationservice.api.application.dto.response.AvailableDatesResponse;
import io.apiorchestrationservice.api.application.dto.response.AvailableSeatsResponse;
import io.apiorchestrationservice.api.application.dto.response.SeatReservationResponse;
import io.apiorchestrationservice.api.application.facade.ReservationCrudFacade;
import io.apiorchestrationservice.api.business.dto.outport.SeatInfo;
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

	// 예약 가능한 날짜 목록을 조회
	@GetMapping("/available-dates/{concertOptionId}")
	public AvailableDatesResponse getAvailableDates(@PathVariable Long concertOptionId) {
		List<LocalDate> availableDates = Arrays.asList(
			LocalDate.of(2024, 7, 10),
			LocalDate.of(2024, 7, 11),
			LocalDate.of(2024, 7, 12)
		);
		return new AvailableDatesResponse(concertOptionId, availableDates);
	}

	// 특정 날짜의 예약 가능한 좌석 목록을 조회
	@GetMapping("/available-seats")
	public AvailableSeatsResponse getAvailableSeats(@ModelAttribute AvailableSeatsRetrievalRequest request) {
		List<SeatInfo> availableSeats = Arrays.asList(
			new SeatInfo(1L, "A", "1", "1", true),
			new SeatInfo(2L, "A", "1", "2", true),
			new SeatInfo(3L, "A", "1", "3", false)
		);
		return new AvailableSeatsResponse(request.getConcertId(), request.getDate(), availableSeats);
	}


	// 좌석 예약 요청
	// 실제로는 좌석의 사용 가능 여부를 확인하고, 임시 예약을 처리했음을 가정하고 mock response를 응답합니다.
	@PostMapping("/reserve-seat")
	public SeatReservationResponse reserveSeat(@RequestBody SeatReservationRequest request) {

		if (checkSeatAvailability(request.getSeatId())) {
			return createMockSuccessTemporaryReservedResponse();
		} else {
			return createMockFailTemporaryReservedResponse();
		}
	}

	private boolean checkSeatAvailability(Long seatId) {
		return seatId != 3L; // 예시로 seatId가 3인 좌석은 예약 불가능한 것으로 가정
	}

}
