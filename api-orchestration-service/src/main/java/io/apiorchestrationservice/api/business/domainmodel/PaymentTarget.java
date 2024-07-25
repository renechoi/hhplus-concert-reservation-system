package io.apiorchestrationservice.api.business.domainmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
@Getter
@RequiredArgsConstructor
public enum PaymentTarget {
	RESERVATION("reservation", "결제 요청이 예약 API로부터 인입되었음을 식별"),
	;

	private final String title;
	private final String description;
}
