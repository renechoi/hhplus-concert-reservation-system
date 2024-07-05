package io.apiorchestrationservice.api.application.dto.response;

import java.time.LocalDateTime;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public record SeatReservationResponse(
	Long reservationId,
	Long userId,
	Long concertOptionId,
	Long seatId,
	String status,
	String message
) {
	// Mock 성공 응답 생성 메서드
	public static SeatReservationResponse createMockSuccessTemporaryReservedResponse() {
		return new SeatReservationResponse(
			1L,
			1L,
			1L,
			1L,
			"TEMPORARY_RESERVED",
			LocalDateTime.now().plusMinutes(5) + "분간 예약되었습니다."
		);
	}

	// Mock 실패 응답 생성 메서드
	public static SeatReservationResponse createMockFailTemporaryReservedResponse() {
		return new SeatReservationResponse(
			null,
			1L,
			1L,
			3L,
			"UNAVAILABLE",
			"예약이 불가합니다"
		);
	}

}
