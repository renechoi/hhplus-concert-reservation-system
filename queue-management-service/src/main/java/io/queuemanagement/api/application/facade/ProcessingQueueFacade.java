package io.queuemanagement.api.application.facade;

import io.queuemanagement.api.application.dto.request.CompletedTokenHandlingRequest;
import io.queuemanagement.api.application.dto.request.ExpiredTokenHandlingRequest;
import io.queuemanagement.api.application.dto.response.ProcessingQueueTokenGeneralResponse;
import io.queuemanagement.api.business.service.ProcessingQueueService;
import io.queuemanagement.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Facade
@RequiredArgsConstructor
public class ProcessingQueueFacade {

	private final ProcessingQueueService processingQueueService;

	public ProcessingQueueTokenGeneralResponse checkAvailability(String tokenValue, String userId) {
		return ProcessingQueueTokenGeneralResponse.from(processingQueueService.checkAvailability(tokenValue, userId));
	}

	public ProcessingQueueTokenGeneralResponse checkAvailability(String tokenValue) {
		return ProcessingQueueTokenGeneralResponse.from(processingQueueService.checkAvailability(tokenValue));
	}

	public void processQueueTransfer() {
		processingQueueService.processQueueTransfer();
	}

	public void completeTokens(ExpiredTokenHandlingRequest request) {
		processingQueueService.completeTokens(request.toCommand());
	}

	public void completeToken(CompletedTokenHandlingRequest request) {
		processingQueueService.completeToken(request.toCommand());
	}

}
