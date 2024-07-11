package io.queuemanagement.common.exception;

import io.queuemanagement.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class ProcessingQueueTokenNotFoundException extends ItemNotFoundException {

	public ProcessingQueueTokenNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public ProcessingQueueTokenNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}