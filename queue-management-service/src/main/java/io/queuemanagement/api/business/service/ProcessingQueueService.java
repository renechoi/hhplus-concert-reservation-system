package io.queuemanagement.api.business.service;

import io.queuemanagement.api.business.dto.inport.CompletedTokenHandlingCommand;
import io.queuemanagement.api.business.dto.inport.ExpiredTokenHandlingCommand;
import io.queuemanagement.api.business.dto.outport.ProcessingQueueTokenGeneralInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface ProcessingQueueService {

	ProcessingQueueTokenGeneralInfo checkAvailability(String queueToken, String userId);

	ProcessingQueueTokenGeneralInfo checkAvailability(String tokenValue);

	ProcessingQueueTokenGeneralInfo retrieve(String userId);


	void processQueueTransfer();
	void completeToken(CompletedTokenHandlingCommand command);
	void completeTokens(ExpiredTokenHandlingCommand command);
}
