package io.reservationservice.api.business.dto.outport;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record ReservationConfirmInfo(
	Long reservationId,
	Long userId,
	ConcertOptionInfo concertOption,
	SeatInfo seat,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	Boolean isConfirmed
) {
	public static ReservationConfirmInfo from(Reservation entity) {
		return ObjectMapperBasedVoMapper.convert(entity, ReservationConfirmInfo.class);
	}
}
