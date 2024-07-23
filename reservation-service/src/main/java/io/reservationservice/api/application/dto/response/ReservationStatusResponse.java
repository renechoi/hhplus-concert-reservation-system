package io.reservationservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.reservationservice.api.business.dto.outport.ReservationInfo;
import io.reservationservice.api.business.dto.outport.ReservationStatusInfo;
import io.reservationservice.api.business.dto.outport.TemporalReservationInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationStatusResponse(
     Long reservationId,
     Long userId,
     ConcertOptionResponse concertOption,
     SeatResponse seat,
     LocalDateTime reserveAt,
     LocalDateTime createdAt,
     Boolean isConfirmed,
     Boolean isCanceled,
	 Boolean isTemporary
) {

	public static ReservationStatusResponse from(ReservationInfo reservation) {
		return new ReservationStatusResponse(
			reservation.reservationId(),
			reservation.userId(),
			ConcertOptionResponse.from(reservation.concertOption()),
			SeatResponse.from(reservation.seat()),
			reservation.reserveAt(),
			reservation.createdAt(),
			reservation.isConfirmed(),
			reservation.isCanceled(),
			false
		);
	}

	public static ReservationStatusResponse from(TemporalReservationInfo temporalReservationInfo) {
		return new ReservationStatusResponse(
			temporalReservationInfo.temporalReservationId(),
			temporalReservationInfo.userId(),
			ConcertOptionResponse.from(temporalReservationInfo.concertOption()),
			SeatResponse.from(temporalReservationInfo.seat()),
			temporalReservationInfo.reserveAt(),
			temporalReservationInfo.createdAt(),
			temporalReservationInfo.isConfirmed(),
			temporalReservationInfo.isCanceled(),
			true
		);
	}

	public static ReservationStatusResponse from(ReservationStatusInfo reservationStatusInfo) {
		if (reservationStatusInfo.isTemporary()) {
			return new ReservationStatusResponse(
				reservationStatusInfo.reservationId(),
				reservationStatusInfo.userId(),
				ConcertOptionResponse.from(reservationStatusInfo.concertOption()),
				SeatResponse.from(reservationStatusInfo.seat()),
				reservationStatusInfo.reserveAt(),
				reservationStatusInfo.createdAt(),
				reservationStatusInfo.isConfirmed(),
				reservationStatusInfo.isCanceled(),
				true
			);
		} else {
			return new ReservationStatusResponse(
				reservationStatusInfo.reservationId(),
				reservationStatusInfo.userId(),
				ConcertOptionResponse.from(reservationStatusInfo.concertOption()),
				SeatResponse.from(reservationStatusInfo.seat()),
				reservationStatusInfo.reserveAt(),
				reservationStatusInfo.createdAt(),
				reservationStatusInfo.isConfirmed(),
				reservationStatusInfo.isCanceled(),
				false
			);
		}
	}
}