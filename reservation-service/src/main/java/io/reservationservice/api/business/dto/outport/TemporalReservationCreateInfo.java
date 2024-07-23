package io.reservationservice.api.business.dto.outport;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.TemporalReservation;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record TemporalReservationCreateInfo(
	Long temporalReservationId,
	Long userId,
	ConcertOptionCreateInfo concertOption,
	SeatCreateInfo seat,
	Boolean isConfirmed,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	LocalDateTime requestAt

) {
	public static TemporalReservationCreateInfo from(TemporalReservation temporalReservation) {
		return ObjectMapperBasedVoMapper.convert(temporalReservation, TemporalReservationCreateInfo.class);
	}
}
