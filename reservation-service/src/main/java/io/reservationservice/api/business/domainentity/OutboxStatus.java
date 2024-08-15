package io.reservationservice.api.business.domainentity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */
@Getter
@RequiredArgsConstructor
public enum OutboxStatus {
	INIT("초기화", "아웃박스 이벤트가 초기화된 상태"),
	SENT("전송 완료", "아웃박스 이벤트가 전송 완료된 상태"),
	;

	private final String title;
	private final String description;

}
