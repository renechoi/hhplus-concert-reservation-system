package io.apiorchestrationservice.api.business.dto.outport;

import java.time.LocalDateTime;


/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record ReservationConfirmInfo(
	Long reservationId,
	Long userId,
	ConcertOptionInfo concertOption,
	SeatInfo seat,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	Boolean isConfirmed
) {

}
