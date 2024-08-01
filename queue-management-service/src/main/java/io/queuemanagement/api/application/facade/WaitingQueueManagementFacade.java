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
public class WaitingQueueManagementFacade {
	private final WaitingQueueService waitingQueueService;
	private final ProcessingQueueService processingQueueService;

	/**
	 * <p>
	 * 대기열 토큰을 생성하고 대기열에 추가합니다.
	 * 동일한 유저의 중복 요청인 경우 userId와 status로 판별하여 Db의 unique constraint를 통해 중복 생성을 방지합니다.
	 * @see {@link io.queuemanagement.common.exception.apiadvice.ApiControllerAdvice.handleDataIntegrityViolationException}
	 * </p>
	 */
	public WaitingQueueTokenGenerationResponse generateAndEnqueue(WaitingQueueTokenGenerateRequest tokenRequest) {
		return WaitingQueueTokenGenerationResponse.from(waitingQueueService.generateAndEnqueue(tokenRequest.toCommand()));
	}

	/**
	 * 먼저 대기열 조회 후 대기열에 존재하지 않는 경우 처리열 조회
	 * 반환 값은 통합된 정보로 반환
	 */
	public WaitingQueueTokenGeneralResponse retrieveToken(String  userId) {
		WaitingQueueTokenGeneralInfo waitingToken = waitingQueueService.retrieveToken(userId);

		if(waitingToken.empty()){
			return WaitingQueueTokenGeneralResponse.from(processingQueueService.retrieve(userId)).withUserId(userId);
		}

		return WaitingQueueTokenGeneralResponse.from(waitingToken).withUserId(userId);
	}
}
