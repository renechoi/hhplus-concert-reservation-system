package io.apiorchestrationservice.common.exception.definitions;

import io.apiorchestrationservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class DistributedLockNotAvailableException extends ServerException{
	public DistributedLockNotAvailableException(){
		super(GlobalResponseCode.DISTRIBUTED_LOCK_ACQUIREMENT_FAILURE);

	}

	public DistributedLockNotAvailableException(GlobalResponseCode code) {
		super(code);
	}
}
