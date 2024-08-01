package io.queuemanagement.api.application.facade;

import io.queuemanagement.api.application.dto.request.ExpiredTokenHandlingRequest;
import io.queuemanagement.api.business.service.QueueManagementService;
import io.queuemanagement.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
@Facade
@RequiredArgsConstructor
public class QueueManagementFacade {
	private final QueueManagementService queueManagementService;

	public void processQueueTransfer() {
		queueManagementService.processQueueTransfer();
	}

	@Deprecated
	public void expireQueueTokens() {
		// queueManagementService.expireProcessingQueueTokens();
		// queueManagementService.expireWaitingQueueTokens();
	}



	// rdb 방식
	public void completeTokens(String userId) {
		// queueManagementService.completeProcessingQueueToken(userId);
		// queueManagementService.completeWaitingQueueTokenByUserId(userId);
	}

	// redis 방식
	public void completeTokens(ExpiredTokenHandlingRequest request) {
		queueManagementService.completeTokensByKeys(request.toCommand());
	}
}
