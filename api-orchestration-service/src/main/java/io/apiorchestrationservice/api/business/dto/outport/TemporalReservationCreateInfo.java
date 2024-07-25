package io.apiorchestrationservice.api.business.dto.outport;

import java.time.LocalDateTime;

public record TemporalReservationCreateInfo(
	Long temporalReservationId,
	Long userId,
	ConcertOptionCreateInfo concertOption,
	SeatCreateInfo seat,
	Boolean isConfirmed,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {}
