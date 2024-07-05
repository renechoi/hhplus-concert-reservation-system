package io.queuemanagement.api.business.domainmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */
@Getter
@RequiredArgsConstructor
public enum WaitingQueueStatus {
	WAITING("대기중", "대기열에서 기다리고 있는 상태"),
	PROCESSING("처리중", "처리열로 이동하여 활성 상태인 유저"),
	COMPLETED("완료", "예약을 성공적으로 마친 상태"),
	REENTER("재진입", "5분의 시간이 만료되어 대기열로 다시 돌아간 상태");

	private final String title;
	private final String description;

}
