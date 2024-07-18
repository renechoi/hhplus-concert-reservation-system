package io.queuemanagement.common.exception.definitions;

import io.queuemanagement.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class WaitingQueueMaxLimitExceededException extends ServerException{
	public WaitingQueueMaxLimitExceededException(){
		super(GlobalResponseCode.ILLEGAL_ARGUMENT);

	}

	public WaitingQueueMaxLimitExceededException(GlobalResponseCode code) {
		super(code);
	}
}
