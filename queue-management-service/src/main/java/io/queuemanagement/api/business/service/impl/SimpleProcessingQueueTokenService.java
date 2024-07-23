package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.domainmodel.QueueStatus.*;
import static io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand.*;

import org.springframework.stereotype.Service;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.dto.outport.ProcessingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.persistence.ProcessingQueueRetrievalRepository;
import io.queuemanagement.api.business.service.ProcessingQueueTokenService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Service
@RequiredArgsConstructor
public class SimpleProcessingQueueTokenService implements ProcessingQueueTokenService {

	private final ProcessingQueueRetrievalRepository processingQueueRetrievalRepository;

	@Override
	public ProcessingQueueTokenGeneralInfo checkProcessingQueueTokenAvailability(String tokenValue, String userId) {
		ProcessingQueueToken processingQueueToken = processingQueueRetrievalRepository
			.findSingleByCondition(tokenAndUserIdAndStatus(tokenValue, userId, PROCESSING));

		return ProcessingQueueTokenGeneralInfo.from(processingQueueToken);
	}

	@Override
	public ProcessingQueueTokenGeneralInfo checkProcessingQueueTokenAvailability(String tokenValue) {
		ProcessingQueueToken processingQueueToken = processingQueueRetrievalRepository
			.findSingleByCondition(tokenAndStatus(tokenValue, PROCESSING));

		return ProcessingQueueTokenGeneralInfo.from(processingQueueToken);
	}
}
