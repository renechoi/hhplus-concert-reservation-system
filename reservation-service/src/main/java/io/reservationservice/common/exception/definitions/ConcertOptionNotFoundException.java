package io.reservationservice.common.exception.definitions;

import io.reservationservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class ConcertOptionNotFoundException extends ItemNotFoundException {

	public ConcertOptionNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public ConcertOptionNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}