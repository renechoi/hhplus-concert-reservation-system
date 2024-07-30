package io.redisservice.common.exception.definitions;

import io.redisservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class CacheValueNotRetrievableException extends ItemNotFoundException {

	public CacheValueNotRetrievableException() {
		super(GlobalResponseCode.ILLEGAL_ARGUMENT);
	}

	public CacheValueNotRetrievableException(GlobalResponseCode code) {
		super(code);
	}
}