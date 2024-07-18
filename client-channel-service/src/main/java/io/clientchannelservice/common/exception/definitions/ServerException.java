package io.clientchannelservice.common.exception.definitions;

import io.clientchannelservice.common.model.GlobalResponseCode;
import lombok.Getter;

/**
 * @author : Rene Choi
 * @since : 2024/06/25
 */
@Getter
public class ServerException extends RuntimeException{


	private final GlobalResponseCode code;

	public ServerException(GlobalResponseCode code) {
		super(code.getResultMessage());
		this.code = code;
	}

	public ServerException(GlobalResponseCode code, Throwable cause) {
		super(code.getResultMessage(), cause);
		this.code = code;
	}

	public ServerException(GlobalResponseCode code, String additionalMessage) {
		super(code.getResultMessage() + ": " + additionalMessage);
		this.code = code;
	}


}