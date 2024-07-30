package io.apiorchestrationservice.api.business.dto.outport;

import io.apiorchestrationservice.common.exception.definitions.DistributedLockNotAvailableException;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
public record DistributedLockInfo(
	 String lockKey,
	 Long waitTime,
	 Long leaseTime,
	 Boolean isLocked
) {

	public boolean isNotLocked() {
		return !isLocked;
	}

	public DistributedLockInfo confirm() {
		if (isNotLocked()) {
			throw new DistributedLockNotAvailableException();
		}
		return this;
	}
}
