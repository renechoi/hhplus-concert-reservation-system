package io.queuemanagement.api.business.persistence;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface WaitingQueueTokenManagementRepository {
	WaitingQueueToken updateStatus(WaitingQueueToken waitingQueueToken);

	void remove(WaitingQueueToken waitingQueueToken);

	WaitingQueueToken updatePosition(WaitingQueueToken waitingQueueToken);
}
