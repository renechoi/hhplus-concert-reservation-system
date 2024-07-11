package io.queuemanagement.api.business.persistence;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public interface ProcessingQueueEnqueueRepository {
	ProcessingQueueToken enqueue(ProcessingQueueToken processingQueueToken);
}
