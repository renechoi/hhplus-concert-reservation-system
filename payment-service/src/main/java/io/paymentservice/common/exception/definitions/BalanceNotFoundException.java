package io.paymentservice.common.exception.definitions;

import io.paymentservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class BalanceNotFoundException extends ItemNotFoundException {

	public BalanceNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public BalanceNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}