package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.domainmodel.QueueStatus.*;
import static io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand.*;

import org.springframework.stereotype.Service;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand;
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

	private final ProcessingQueueRetrievalRepository processingQueueRepository;

	@Override
	public ProcessingQueueTokenGeneralInfo checkProcessingTokenAvailability(String tokenValue, String userId) {
		ProcessingQueueToken processingQueueToken = processingQueueRepository.findSingleByCondition(onProcessing(tokenValue, userId));

		return ProcessingQueueTokenGeneralInfo.from(processingQueueToken);
	}

	@Override
	public ProcessingQueueTokenGeneralInfo checkProcessingTokenAvailability(String tokenValue) {
		ProcessingQueueToken processingQueueToken = processingQueueRepository.findSingleByCondition(onProcessing(tokenValue));

		return ProcessingQueueTokenGeneralInfo.from(processingQueueToken);
	}
}
