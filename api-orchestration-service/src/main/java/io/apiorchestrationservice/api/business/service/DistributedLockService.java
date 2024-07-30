package io.apiorchestrationservice.api.business.service;

import io.apiorchestrationservice.api.business.dto.inport.DistributedLockCommand;
import io.apiorchestrationservice.api.business.dto.inport.DistributedUnLockCommand;
import io.apiorchestrationservice.api.business.dto.outport.DistributedLockInfo;
import io.apiorchestrationservice.api.business.dto.outport.DistributedUnLockInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
public interface DistributedLockService {
	DistributedLockInfo lock(DistributedLockCommand command);

	DistributedUnLockInfo unlock(DistributedUnLockCommand command);
}
