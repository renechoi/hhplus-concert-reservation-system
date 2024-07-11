package io.reservationservice.api.business.dto.outport;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.Concert;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record ConcertCreateInfo(
	Long concertId,
	String title,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {
	public static ConcertCreateInfo from(Concert concert) {
		return ObjectMapperBasedVoMapper.convert(concert, ConcertCreateInfo.class);
	}
}
