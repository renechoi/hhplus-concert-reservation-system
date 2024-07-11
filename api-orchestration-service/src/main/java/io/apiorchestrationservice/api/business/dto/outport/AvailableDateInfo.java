package io.apiorchestrationservice.api.business.dto.outport;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record AvailableDateInfo(
	Long concertOptionId,
	LocalDateTime concertDate,
	Duration concertDuration,
	String title,
	String description,
	BigDecimal price
) {
}
