package io.reservationservice.api.business.dto.outport;

import java.time.LocalDateTime;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record SeatInfo(
	 Long seatId,
	 ConcertOptionInfo concertOption,
	 String section,
	 String seatRow,
	 String seatNumber,
	 Boolean occupied,
	 LocalDateTime createdAt
) {
}
