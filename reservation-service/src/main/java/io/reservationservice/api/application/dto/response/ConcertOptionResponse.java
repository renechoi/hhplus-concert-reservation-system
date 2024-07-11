package io.reservationservice.api.application.dto.response;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import io.reservationservice.api.business.dto.outport.ConcertOptionCreateInfo;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record ConcertOptionResponse(

	Long concertOptionId,
	ConcertResponse concert,
	LocalDateTime concertDate,
	Duration concertDuration,
	String title,
	String description,
	BigDecimal price,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {
	public static ConcertOptionResponse from(ConcertOptionCreateInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, ConcertOptionResponse.class);
	}
}
