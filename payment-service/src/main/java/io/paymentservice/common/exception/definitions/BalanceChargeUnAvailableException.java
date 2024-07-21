package io.paymentservice.common.exception.definitions;

import io.paymentservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class BalanceChargeUnAvailableException extends ServerException{
	public BalanceChargeUnAvailableException(){
		super(GlobalResponseCode.USER_BALANCE_CHARGE_UNAVAILABLE);

	}

	public BalanceChargeUnAvailableException(GlobalResponseCode code) {
		super(code);
	}
}
