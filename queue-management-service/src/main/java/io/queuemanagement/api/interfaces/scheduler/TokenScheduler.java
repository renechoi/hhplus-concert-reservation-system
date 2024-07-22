package io.queuemanagement.api.interfaces.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import io.queuemanagement.api.application.facade.QueueManagementFacade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/15
 */
@Component
@RequiredArgsConstructor
public class TokenScheduler {
	private final QueueManagementFacade queueManagementFacade;


	/**
	 * 대기열 -> 처리열 이동
	 */
	@Scheduled(fixedRateString = "${scheduler.queueTransferRate}")
	// @SchedulerLock(name = "processScheduledQueueTransfer", lockAtMostFor = "PT5S", lockAtLeastFor = "PT2S")
	public void processScheduledQueueTransfer() {
		queueManagementFacade.processQueueTransfer();
	}

	/**
	 * 만료 토큰 처리
	 */
	@Scheduled(fixedRateString = "${scheduler.expireTokensRate}")
	// @SchedulerLock(name = "expireTokens", lockAtMostFor = "PT15S", lockAtLeastFor = "PT10S")
	public void expireTokens() {
		queueManagementFacade.expireQueueTokens();
	}

}
