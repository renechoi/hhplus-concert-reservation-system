package io.redisservice.common.exception.definitions;

import io.redisservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public class ItemNotFoundException extends ServerException {

	public ItemNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public ItemNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}