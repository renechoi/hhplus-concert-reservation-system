package io.paymentservice.common.exception.definitions;

import io.paymentservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class PaymentTransactionNotFoundException extends ItemNotFoundException {

	public PaymentTransactionNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public PaymentTransactionNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}