package io.paymentservice.common.exception;

import io.paymentservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class UserBalanceUseUnAvailableException extends ServerException{
	public UserBalanceUseUnAvailableException(){
		super(GlobalResponseCode.USER_BALANCE_USE_UNAVAILABLE);

	}

	public UserBalanceUseUnAvailableException(GlobalResponseCode code) {
		super(code);
	}
}
