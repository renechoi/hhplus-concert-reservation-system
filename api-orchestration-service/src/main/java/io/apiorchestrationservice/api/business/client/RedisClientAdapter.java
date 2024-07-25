package io.apiorchestrationservice.api.business.client;

import io.apiorchestrationservice.api.business.dto.inport.DistributedLockCommand;
import io.apiorchestrationservice.api.business.dto.inport.DistributedUnLockCommand;
import io.apiorchestrationservice.api.business.dto.outport.DistributedLockInfo;
import io.apiorchestrationservice.api.business.dto.outport.DistributedUnLockInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
public interface RedisClientAdapter {
	DistributedLockInfo lock(DistributedLockCommand command);

	DistributedUnLockInfo unlock(DistributedUnLockCommand request);
}
