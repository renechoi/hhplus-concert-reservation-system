package io.reservationservice.api.business.dto.outport;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.TemporalReservation;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record TemporalReservationInfo(
	Long temporalReservationId,
	Long userId,
	ConcertOptionInfo concertOption,
	SeatInfo seat,
	Boolean isConfirmed,
	Boolean isCanceled,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	LocalDateTime requestAt

) {
	public static TemporalReservationInfo from(TemporalReservation temporalReservation) {
		return ObjectMapperBasedVoMapper.convert(temporalReservation, TemporalReservationInfo.class);
	}
}
