package io.queuemanagement.api.business.service;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public interface QueueManagementService {

	void processScheduledQueueTransfer();

	void expireProcessingQueueTokens();
	void expireWaitingQueueTokens();

	void completeProcessingQueueToken(String userId);
	void completeWaitingQueueTokenByUserId(String userId);

	void recalculateWaitingQueuePositionsBySimpleReOrderingEach();

	void recalculateWaitingQueuePositionsWithJsonStoring();

}
