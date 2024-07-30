package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.common.token.QueueTokenGenerator.*;

import org.springframework.stereotype.Service;

import io.queuemanagement.api.business.dto.outport.ProcessingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.persistence.ProcessingQueueRetrievalRepository;
import io.queuemanagement.api.business.service.ProcessingQueueService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Service
@RequiredArgsConstructor
public class SimpleProcessingQueueService implements ProcessingQueueService {

	private final ProcessingQueueRetrievalRepository processingQueueRepository;

	@Override
	public ProcessingQueueTokenGeneralInfo checkProcessingTokenAvailability(String tokenValue, String userId) {
		// rdb
		// ProcessingQueueToken processingQueueToken = processingQueueRepository.findSingleByCondition(onProcessing(tokenValue, userId));
		// return ProcessingQueueTokenGeneralInfo.from(processingQueueToken);

		// redis
		return ProcessingQueueTokenGeneralInfo.from(processingQueueRepository.getToken(tokenValue)).withProcessingAndUserInfo(userId);
	}

	@Override
	public ProcessingQueueTokenGeneralInfo checkProcessingTokenAvailability(String tokenValue) {
		return checkProcessingTokenAvailability(tokenValue, null);
	}

	@Override
	public ProcessingQueueTokenGeneralInfo retrieve(String userId) {
		return ProcessingQueueTokenGeneralInfo.from(processingQueueRepository.getToken(generateToken(userId)));
	}
}
