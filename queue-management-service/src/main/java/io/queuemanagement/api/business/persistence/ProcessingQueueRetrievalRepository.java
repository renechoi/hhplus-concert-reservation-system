package io.queuemanagement.api.business.persistence;

import java.util.List;
import java.util.Optional;

import io.queuemanagement.api.business.domainmodel.AvailableSlots;
import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public interface ProcessingQueueRetrievalRepository {

	AvailableSlots countAvailableSlots();
	ProcessingQueueToken getToken(String tokenValue);

	ProcessingQueueToken findSingleByCondition(ProcessingQueueTokenSearchCommand searchCommand);

	Optional<ProcessingQueueToken> findOptionalByCondition(ProcessingQueueTokenSearchCommand searchCommand);

	List<ProcessingQueueToken> findAllByCondition(ProcessingQueueTokenSearchCommand searchCommand);

	void increase(int counts);
}
