package io.queuemanagement.api.application.facade;

import io.queuemanagement.api.application.dto.request.WaitingQueueTokenGenerateRequest;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGeneralResponse;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGenerationResponse;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.service.WaitingQueueService;
import io.queuemanagement.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Facade
@RequiredArgsConstructor
public class WaitingQueueManagementFacade {
	private final WaitingQueueService waitingQueueService;

	public WaitingQueueTokenGenerationResponse generateAndEnqueue(WaitingQueueTokenGenerateRequest tokenRequest) {
		return WaitingQueueTokenGenerationResponse.from(waitingQueueService.generateAndEnqueue(tokenRequest.toCommand()));
	}

	public WaitingQueueTokenGeneralResponse retrieveToken(String  userId) {
		return WaitingQueueTokenGeneralResponse.from(waitingQueueService.retrieveByAiAtOnceCalculation(userId));
	}
}
