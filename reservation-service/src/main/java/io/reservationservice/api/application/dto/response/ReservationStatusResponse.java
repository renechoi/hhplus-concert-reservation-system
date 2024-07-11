package io.reservationservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.api.business.domainentity.TemporaryReservation;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationStatusResponse(
     Long reservationId,
     Long userId,
     Long concertOptionId,
     Long seatId,
     LocalDateTime reserveAt,
     LocalDateTime createdAt,
     Boolean isConfirmed,
     Boolean isCanceled,
	 Boolean isTemporary
) {

	public static ReservationStatusResponse from(Reservation reservation) {
		return new ReservationStatusResponse(
			reservation.getReservationId(),
			reservation.getUserId(),
			reservation.getConcertOption().getConcertOptionId(),
			reservation.getSeat().getSeatId(),
			reservation.getReserveAt(),
			reservation.getCreatedAt(),
			true,
			reservation.getIsCanceled(),
			false
		);
	}

	public static ReservationStatusResponse from(TemporaryReservation temporaryReservation) {
		return new ReservationStatusResponse(
			temporaryReservation.getTemporaryReservationId(),
			temporaryReservation.getUserId(),
			temporaryReservation.getConcertOption().getConcertOptionId(),
			temporaryReservation.getSeat().getSeatId(),
			temporaryReservation.getReserveAt(),
			temporaryReservation.getCreatedAt(),
			temporaryReservation.getIsConfirmed(),
			false,
			true
		);
	}
}