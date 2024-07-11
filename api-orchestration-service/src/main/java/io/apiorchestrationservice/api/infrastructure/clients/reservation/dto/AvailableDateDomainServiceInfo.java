package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableDateDomainServiceInfo(
	Long concertOptionId,
	LocalDateTime concertDate,
	Duration concertDuration,
	String title,
	String description,
	BigDecimal price
) {

}
