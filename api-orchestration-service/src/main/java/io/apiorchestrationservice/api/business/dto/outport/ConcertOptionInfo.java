package io.apiorchestrationservice.api.business.dto.outport;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public record ConcertOptionInfo(
	Long concertOptionId,
	ConcertInfo concert,
	LocalDateTime concertDate,
	Duration concertDuration,
	String title,
	String description,
	BigDecimal price,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {}