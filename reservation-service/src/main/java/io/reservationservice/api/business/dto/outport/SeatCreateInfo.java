package io.reservationservice.api.business.dto.outport;

import java.time.LocalDateTime;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record SeatCreateInfo(
	 Long seatId,
	 ConcertOptionCreateInfo concertOption,
	 String section,
	 String seatRow,
	 String seatNumber,
	 Boolean occupied,
	 LocalDateTime createdAt
) {
}
