package io.queuemanagement.common.exception;

import io.queuemanagement.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class WaitingQueueTokenNotFoundException extends ItemNotFoundException {

	public WaitingQueueTokenNotFoundException() {
		super(GlobalResponseCode.ILLEGAL_ARGUMENT);
	}

	public WaitingQueueTokenNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}