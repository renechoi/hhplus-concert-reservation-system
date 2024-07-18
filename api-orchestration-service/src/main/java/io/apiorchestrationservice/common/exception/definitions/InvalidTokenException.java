package io.apiorchestrationservice.common.exception.definitions;

import io.apiorchestrationservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class InvalidTokenException extends ServerException{
	public InvalidTokenException(){
		super(GlobalResponseCode.ILLEGAL_ARGUMENT);

	}

	public InvalidTokenException(GlobalResponseCode code) {
		super(code);
	}
}
