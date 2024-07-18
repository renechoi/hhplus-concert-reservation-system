package io.reservationservice.api.business.dto.outport;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.api.business.domainentity.TemporaryReservation;

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

	public static ReservationInfo from(TemporaryReservation temporaryReservation) {
		return new ReservationInfo(
			temporaryReservation.getTemporaryReservationId(),
			temporaryReservation.getUserId(),
			ConcertOptionInfo.from(temporaryReservation.getConcertOption()),
			SeatInfo.from(temporaryReservation.getSeat()),
			temporaryReservation.getReserveAt(),
			temporaryReservation.getCreatedAt(),
			temporaryReservation.getIsConfirmed(),
			false,
			true
		);
	}
}