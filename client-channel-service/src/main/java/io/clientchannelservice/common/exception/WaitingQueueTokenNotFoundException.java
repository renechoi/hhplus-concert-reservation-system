package io.clientchannelservice.common.exception;

import io.clientchannelservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class WaitingQueueTokenNotFoundException extends ServerException{

	public WaitingQueueTokenNotFoundException(){
		super(GlobalResponseCode.ILLEGAL_ARGUMENT);

	}

	public WaitingQueueTokenNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}
