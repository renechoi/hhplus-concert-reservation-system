package io.apiorchestrationservice.api.business.dto.outport;

import java.time.LocalDateTime;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationStatusInfo(
	Long reservationId,
	Long userId,
	Long concertOptionId,
	Long seatId,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	Boolean isConfirmed,
	Boolean isCanceled,
	Boolean isTemporary
) {
}
