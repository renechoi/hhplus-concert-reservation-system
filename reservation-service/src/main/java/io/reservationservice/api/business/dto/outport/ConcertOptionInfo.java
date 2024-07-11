package io.reservationservice.api.business.dto.outport;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record ConcertOptionInfo(

	Long concertOptionId,
	ConcertCreateInfo concert,
	LocalDateTime concertDate,
	Duration concertDuration,
	String title,
	String description,
	BigDecimal price,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {

	public static ConcertOptionInfo from(ConcertOption concertOption) {
		return ObjectMapperBasedVoMapper.convert(concertOption, ConcertOptionInfo.class);
	}
}
