package io.reservationservice.api.business.dto.outport;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.api.business.domainentity.TemporalReservation;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationInfo(
     Long reservationId,
     Long userId,
     ConcertOptionInfo concertOption,
     SeatInfo seat,
     LocalDateTime reserveAt,
     LocalDateTime createdAt,
     Boolean isConfirmed,
     Boolean isCanceled,
	 Boolean isTemporary
) {

	public static ReservationInfo from(Reservation reservation) {
		return new ReservationInfo(
			reservation.getReservationId(),
			reservation.getUserId(),
			ConcertOptionInfo.from(reservation.getConcertOption()),
			SeatInfo.from(reservation.getSeat()),
			reservation.getReserveAt(),
			reservation.getCreatedAt(),
			true,
			reservation.getIsCanceled(),
			false
		);
	}

	public static ReservationInfo from(TemporalReservation temporalReservation) {
		return new ReservationInfo(
			temporalReservation.getTemporalReservationId(),
			temporalReservation.getUserId(),
			ConcertOptionInfo.from(temporalReservation.getConcertOption()),
			SeatInfo.from(temporalReservation.getSeat()),
			temporalReservation.getReserveAt(),
			temporalReservation.getCreatedAt(),
			temporalReservation.getIsConfirmed(),
			false,
			true
		);
	}
}