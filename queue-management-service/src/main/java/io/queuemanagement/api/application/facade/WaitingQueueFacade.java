package io.queuemanagement.api.application.facade;

import io.queuemanagement.api.application.dto.request.WaitingQueueTokenGenerateRequest;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGeneralResponse;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGenerationResponse;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.service.ProcessingQueueService;
import io.queuemanagement.api.business.service.WaitingQueueService;
import io.queuemanagement.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Facade
@RequiredArgsConstructor
public class WaitingQueueFacade {
	private final WaitingQueueService waitingQueueService;
	private final ProcessingQueueService processingQueueService;

	public WaitingQueueTokenGenerationResponse enqueue(WaitingQueueTokenGenerateRequest tokenRequest) {
		return WaitingQueueTokenGenerationResponse.from(waitingQueueService.enqueue(tokenRequest.toCommand()));
	}

	/**
	 * 먼저 대기열 조회 후 대기열에 존재하지 않는 경우 처리열 조회
	 * 반환 값은 통합된 정보로 반환
	 */
	public WaitingQueueTokenGeneralResponse retrieve(String  userId) {
		WaitingQueueTokenGeneralInfo waitingToken = waitingQueueService.retrieve(userId);

		if(waitingToken.empty()){
			return WaitingQueueTokenGeneralResponse.from(processingQueueService.retrieve(userId)).withUserId(userId);
		}

		return WaitingQueueTokenGeneralResponse.from(waitingToken).withUserId(userId);
	}
}
