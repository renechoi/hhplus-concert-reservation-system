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
public interface ProcessingQueueRepository {
	ProcessingQueueToken enqueue(ProcessingQueueToken processingQueueToken);
	ProcessingQueueToken dequeue(ProcessingQueueToken processingQueueToken);

	AvailableSlots countAvailableSlots();

	// key - tokenvalue, value - userId
	ProcessingQueueToken getToken(String tokenValue);

	void increaseCounter(int counts);

	void decreaseCounter(int counts);

	ProcessingQueueToken findSingleByCondition(ProcessingQueueTokenSearchCommand searchCommand);

	Optional<ProcessingQueueToken> findOptionalByCondition(ProcessingQueueTokenSearchCommand searchCommand);

	List<ProcessingQueueToken> findAllByCondition(ProcessingQueueTokenSearchCommand searchCommand);

	ProcessingQueueToken store(ProcessingQueueToken processingQueueToken);
}
