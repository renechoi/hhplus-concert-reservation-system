package io.queuemanagement.api.interfaces.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
		queueManagementFacade.processScheduledQueueTransfer();
	}

	/**
	 * 만료 토큰 처리
	 */
	@Scheduled(fixedRateString = "${scheduler.expireTokensRate}")
	// @SchedulerLock(name = "expireTokens", lockAtMostFor = "PT15S", lockAtLeastFor = "PT10S")
	public void expireTokens() {
		queueManagementFacade.expireQueueTokens();
	}



	/**
	 * 대기열에 존재하는 모든 토큰들에 대해 순서를 일일히 재조정하여 각각 업데이트 치는 방식
	 */
	@Deprecated
	public void recalculateWaitingQueuePositionsBySimpleReOrderingEach() {
		queueManagementFacade.recalculateWaitingQueuePositionsBySimpleReOrderingEach();
	}



	/**
	 * 대기열에 존재하는 모든 토큰들에 대해 순서를 일일히 재조정하되, 별도의 position 테이블에, 전체 토큰의 순번이 계산된 json 문서를 업데이트
	 * -> IO가 1회 발생하므로 어플리케이션의 json 연산 비용만으로 해결
	 * -> 실시간성 미보장, 데이터 수가 많아질 때 json 연산에 대한 비용 계산 측정 필요
	 * -> 조회시에 해당 테이블의 값을 가져와서 position으로 사용
	 */
	@Deprecated
	public void recalculateWaitingQueuePositionsWithJsonStoring() {
		queueManagementFacade.recalculateWaitingQueuePositionsWithJsonStoring();
	}

}
