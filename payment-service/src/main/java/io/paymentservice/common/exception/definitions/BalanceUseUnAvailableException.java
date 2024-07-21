package io.paymentservice.common.exception.definitions;

import io.paymentservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class BalanceUseUnAvailableException extends ServerException{
	public BalanceUseUnAvailableException(){
		super(GlobalResponseCode.USER_BALANCE_USE_UNAVAILABLE);

	}

	public BalanceUseUnAvailableException(GlobalResponseCode code) {
		super(code);
	}
}
