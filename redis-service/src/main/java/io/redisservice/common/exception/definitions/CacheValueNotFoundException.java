package io.redisservice.common.exception.definitions;

import io.redisservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class CacheValueNotFoundException extends ItemNotFoundException {

	public CacheValueNotFoundException() {
		super(GlobalResponseCode.NO_CONTENT);
	}

	public CacheValueNotFoundException(GlobalResponseCode code) {
		super(code);
	}
}