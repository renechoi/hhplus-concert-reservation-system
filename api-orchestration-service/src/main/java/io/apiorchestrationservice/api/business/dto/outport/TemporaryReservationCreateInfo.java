package io.apiorchestrationservice.api.business.dto.outport;

import java.time.LocalDateTime;

public record TemporaryReservationCreateInfo(
	Long temporaryReservationId,
	Long userId,
	ConcertOptionCreateInfo concertOption,
	SeatCreateInfo seat,
	Boolean isConfirmed,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {}
