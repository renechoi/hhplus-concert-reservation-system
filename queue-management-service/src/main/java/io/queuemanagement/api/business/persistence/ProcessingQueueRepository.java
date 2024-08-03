package io.queuemanagement.api.business.persistence;

import io.queuemanagement.api.business.domainmodel.AvailableSlots;
import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public interface ProcessingQueueRepository {
	ProcessingQueueToken enqueue(ProcessingQueueToken processingQueueToken);

	ProcessingQueueToken dequeue(ProcessingQueueToken processingQueueToken);

	AvailableSlots countAvailableSlots();

	// key - tokenvalue, value - userId
	ProcessingQueueToken retrieveToken(String tokenValue);

	void increaseCounter(int counts);

	void decreaseCounter(int counts);



}
