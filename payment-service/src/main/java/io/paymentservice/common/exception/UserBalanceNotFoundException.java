package io.paymentservice.common.exception;

import io.paymentservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class UserBalanceNotFoundException extends ItemNotFoundException {

	public UserBalanceNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public UserBalanceNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}