package io.reservationservice.common.exception;

import io.reservationservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class ReservationUnAvailableException extends ServerException{
	public ReservationUnAvailableException(){
		super(GlobalResponseCode.RESERVATION_UNAVAILABLE);

	}

	public ReservationUnAvailableException(GlobalResponseCode code) {
		super(code);
	}
}
