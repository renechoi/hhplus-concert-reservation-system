package io.reservationservice.common.exception.definitions;

import io.reservationservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class ConcertNotFoundException extends ItemNotFoundException {

	public ConcertNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public ConcertNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}