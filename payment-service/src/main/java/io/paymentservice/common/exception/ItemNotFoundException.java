package io.paymentservice.common.exception;

import io.paymentservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public class ItemNotFoundException extends ServerException {

	public ItemNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public ItemNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}