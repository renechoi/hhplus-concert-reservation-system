package io.reservationservice.api.application.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record SeatResponse(
	Long seatId,
	@JsonIgnore
	ConcertOptionResponse concertOption,
	String section,
	String seatRow,
	String seatNumber,
	Boolean occupied,
	LocalDateTime createdAt
) {
}
