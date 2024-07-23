package io.reservationservice.common.exception.definitions;

import io.reservationservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class TemporalReservationNotFoundException extends ItemNotFoundException {

	public TemporalReservationNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public TemporalReservationNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}