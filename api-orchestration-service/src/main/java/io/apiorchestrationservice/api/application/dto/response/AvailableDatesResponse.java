package io.apiorchestrationservice.api.application.dto.response;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.AvailableDateInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public record AvailableDatesResponse(
	Long concertOptionId,
	LocalDateTime concertDate,
	Duration concertDuration,
	String title,
	String description,
	BigDecimal price
) {
	public static AvailableDatesResponse from(AvailableDateInfo availableDateInfo) {
		return ObjectMapperBasedVoMapper.convert(availableDateInfo, AvailableDatesResponse.class);
	}
}
