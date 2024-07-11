package io.queuemanagement.api.business.persistence;

import io.queuemanagement.api.business.domainmodel.WaitingQueuePositionJson;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public interface QueuePositionDocumentRepository {
	void updateQueuePositionJson(WaitingQueuePositionJson waitingQueuePositionJson);

	WaitingQueuePositionJson findDocumentByIdOrElseDefault(Long id);
}
