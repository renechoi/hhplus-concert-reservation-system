package io.queuemanagement.api.application.facade;

import org.springframework.scheduling.annotation.Scheduled;

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

	public void processScheduledQueueTransfer() {
		queueManagementService.processScheduledQueueTransfer();
	}

	public void expireQueueTokens() {
		queueManagementService.expireProcessingQueueTokens();
		queueManagementService.expireWaitingQueueTokens();
	}

	@Deprecated
	public void recalculateWaitingQueuePositionsBySimpleReOrderingEach() {
		queueManagementService.recalculateWaitingQueuePositionsBySimpleReOrderingEach();
	}

	@Deprecated
	public void recalculateWaitingQueuePositionsWithJsonStoring() {
		queueManagementService.recalculateWaitingQueuePositionsWithJsonStoring();
	}

}
