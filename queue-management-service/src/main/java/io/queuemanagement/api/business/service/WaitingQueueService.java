package io.queuemanagement.api.business.service;

import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenGenerateCommand;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGenerateInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface WaitingQueueService {
	WaitingQueueTokenGenerateInfo generateAndEnqueue(WaitingQueueTokenGenerateCommand waitingQueueTokenGenerateCommand);

	WaitingQueueTokenGeneralInfo retrieve(String  userId);
}
