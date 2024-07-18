package io.reservationservice.api.business.dto.outport;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.TemporaryReservation;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record TemporaryReservationInfo(
	Long temporaryReservationId,
	Long userId,
	ConcertOptionInfo concertOption,
	SeatInfo seat,
	Boolean isConfirmed,
	Boolean isCanceled,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	LocalDateTime requestAt

) {
	public static TemporaryReservationInfo from(TemporaryReservation temporaryReservation) {
		return ObjectMapperBasedVoMapper.convert(temporaryReservation, TemporaryReservationInfo.class);
	}
}
