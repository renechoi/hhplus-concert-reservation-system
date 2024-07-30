package io.queuemanagement.api.business.service;

import io.queuemanagement.api.business.dto.outport.ProcessingQueueTokenGeneralInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface ProcessingQueueService {

	ProcessingQueueTokenGeneralInfo checkProcessingTokenAvailability(String queueToken, String userId);

	ProcessingQueueTokenGeneralInfo checkProcessingTokenAvailability(String tokenValue);

	ProcessingQueueTokenGeneralInfo retrieve(String userId);
}
