package io.queuemanagement.api.business.service;

import io.queuemanagement.api.business.dto.inport.CompletedTokenHandlingCommand;
import io.queuemanagement.api.business.dto.inport.ExpiredTokenHandlingCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public interface QueueManagementService {
	void processQueueTransfer();

	void completeToken(CompletedTokenHandlingCommand command);

	void expireProcessingQueueTokens();
	void expireWaitingQueueTokens();
	void completeProcessingQueueToken(String userId);
	void completeWaitingQueueTokenByUserId(String userId);
	void completeTokensByKeys(ExpiredTokenHandlingCommand command);
}
