package io.queuemanagement.common.exception.definitions;

import io.queuemanagement.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class ProcessingQueueMaxLimitReachedException extends ServerException{
	public ProcessingQueueMaxLimitReachedException(){
		super(GlobalResponseCode.TOKEN_TRANSFER_CURRENTLY_NOT_AVAILABLE);

	}

	public ProcessingQueueMaxLimitReachedException(GlobalResponseCode code) {
		super(code);
	}
}
