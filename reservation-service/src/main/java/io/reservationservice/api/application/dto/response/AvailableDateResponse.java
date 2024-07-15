package io.reservationservice.api.application.dto.response;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.dto.outport.AvailableDateInfo;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableDateResponse(
	Long concertOptionId,
	LocalDateTime concertDate,
	Duration concertDuration,
	String title,
	String description,
	BigDecimal price
) {
	public static AvailableDateResponse from(ConcertOption option) {
		return ObjectMapperBasedVoMapper.convert(option, AvailableDateResponse.class);
	}

	public static AvailableDateResponse from(AvailableDateInfo availableDateInfo) {
		return ObjectMapperBasedVoMapper.convert(availableDateInfo, AvailableDateResponse.class);
	}
}
