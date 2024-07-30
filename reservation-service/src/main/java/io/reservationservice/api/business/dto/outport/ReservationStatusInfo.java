package io.reservationservice.api.business.dto.outport;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.api.business.domainentity.TemporalReservation;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationStatusInfo(
     Long reservationId,
     Long userId,
     ConcertOptionInfo concertOption,
     SeatInfo seat,
     LocalDateTime reserveAt,
     LocalDateTime createdAt,
     Boolean isConfirmed,
     Boolean isCanceled,
	 Boolean isTemporary
) implements Serializable {

	public static ReservationStatusInfo from(Reservation reservation) {
		return new ReservationStatusInfo(
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

	public static ReservationStatusInfo from(TemporalReservation temporalReservation) {
		return new ReservationStatusInfo(
			temporalReservation.getTemporalReservationId(),
			temporalReservation.getUserId(),
			ConcertOptionInfo.from(temporalReservation.getConcertOption()),
			SeatInfo.from(temporalReservation.getSeat()),
			temporalReservation.getReserveAt(),
			temporalReservation.getCreatedAt(),
			temporalReservation.getIsConfirmed(),
			temporalReservation.getIsCanceled(),
			true
		);
	}
}