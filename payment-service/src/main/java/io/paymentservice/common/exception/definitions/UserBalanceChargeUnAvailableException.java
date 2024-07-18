package io.paymentservice.common.exception.definitions;

import io.paymentservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class UserBalanceChargeUnAvailableException extends ServerException{
	public UserBalanceChargeUnAvailableException(){
		super(GlobalResponseCode.USER_BALANCE_CHARGE_UNAVAILABLE);

	}

	public UserBalanceChargeUnAvailableException(GlobalResponseCode code) {
		super(code);
	}
}
