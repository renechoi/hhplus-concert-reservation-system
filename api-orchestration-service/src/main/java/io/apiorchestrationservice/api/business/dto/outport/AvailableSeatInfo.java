package io.apiorchestrationservice.api.business.dto.outport;

import java.time.LocalDateTime;

public record AvailableSeatInfo(
	Long seatId,
	ConcertOptionInfo concertOption,
	Long seatNumber,
	Boolean occupied,
	LocalDateTime createdAt
) {
}
