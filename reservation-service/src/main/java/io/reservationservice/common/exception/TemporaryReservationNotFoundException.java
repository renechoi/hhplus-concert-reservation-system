package io.reservationservice.common.exception;

import io.reservationservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class TemporaryReservationNotFoundException extends ItemNotFoundException {

	public TemporaryReservationNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public TemporaryReservationNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}