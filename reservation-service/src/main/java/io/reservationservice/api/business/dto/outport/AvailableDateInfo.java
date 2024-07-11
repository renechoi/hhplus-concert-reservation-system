package io.reservationservice.api.business.dto.outport;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableDateInfo(
	Long concertOptionId,
	LocalDateTime concertDate,
	Duration concertDuration,
	String title,
	String description,
	BigDecimal price
) {
	public static AvailableDateInfo from(ConcertOption option) {
		return ObjectMapperBasedVoMapper.convert(option, AvailableDateInfo.class);
	}
}
