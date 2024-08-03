package io.queuemanagement.api.interfaces.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.queuemanagement.api.application.facade.ProcessingQueueFacade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/15
 */
@Component
@RequiredArgsConstructor
public class TokenScheduler {
	private final ProcessingQueueFacade processingQueueFacade;

	/**
	 * 대기열 -> 처리열 이동
	 */
	@Scheduled(fixedRateString = "${scheduler.queueTransferRate}")
	public void processScheduledQueueTransfer() {
		processingQueueFacade.processQueueTransfer();
	}


}
