package io.clientchannelservice.common.exception.definitions;

import io.clientchannelservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class FeignCommunicationException extends ServerException{
	public FeignCommunicationException(){
		super(GlobalResponseCode.FEIGN_EXCHANGE_EXCEPTION);

	}

	public FeignCommunicationException(GlobalResponseCode code) {
		super(code);
	}

	public FeignCommunicationException(GlobalResponseCode globalResponseCode, String value) {
		super(globalResponseCode, value);
	}
}
