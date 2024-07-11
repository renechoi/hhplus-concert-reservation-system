package io.paymentservice.common.exception;

import io.paymentservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class PaymentHistoryNotFoundException extends ItemNotFoundException {

	public PaymentHistoryNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public PaymentHistoryNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}