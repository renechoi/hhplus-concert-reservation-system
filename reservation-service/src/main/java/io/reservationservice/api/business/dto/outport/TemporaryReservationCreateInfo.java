package io.reservationservice.api.business.dto.outport;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.TemporaryReservation;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record TemporaryReservationCreateInfo(
	Long temporaryReservationId,
	Long userId,
	ConcertOptionCreateInfo concertOption,
	SeatCreateInfo seat,
	Boolean isConfirmed,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	LocalDateTime requestAt

) {
	public static TemporaryReservationCreateInfo from(TemporaryReservation temporaryReservation) {
		return ObjectMapperBasedVoMapper.convert(temporaryReservation, TemporaryReservationCreateInfo.class);
	}
}
