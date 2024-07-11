package io.queuemanagement.api.business.persistence;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface WaitingQueueTokenEnqueueRepository {

	WaitingQueueToken enqueue(WaitingQueueToken waitingQueueToken);

}
