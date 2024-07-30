package io.redisservice.common.exception.definitions;

import io.redisservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class QueueItemNotFoundException extends ItemNotFoundException {

	public QueueItemNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public QueueItemNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}