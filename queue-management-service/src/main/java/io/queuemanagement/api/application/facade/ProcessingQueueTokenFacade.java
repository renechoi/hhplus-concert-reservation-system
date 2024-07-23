package io.queuemanagement.api.application.facade;

import io.queuemanagement.api.application.dto.response.ProcessingQueueTokenGeneralResponse;
import io.queuemanagement.api.business.service.ProcessingQueueTokenService;
import io.queuemanagement.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Facade
@RequiredArgsConstructor
public class ProcessingQueueTokenFacade {

	private final ProcessingQueueTokenService processingQueueTokenService;

	public ProcessingQueueTokenGeneralResponse checkProcessingQueueTokenAvailability(String tokenValue, String userId) {
		return ProcessingQueueTokenGeneralResponse.from(processingQueueTokenService.checkProcessingTokenAvailability(tokenValue,userId));
	}

	public ProcessingQueueTokenGeneralResponse checkProcessingQueueTokenAvailability(String tokenValue) {
		return ProcessingQueueTokenGeneralResponse.from(processingQueueTokenService.checkProcessingTokenAvailability(tokenValue));
	}



}
