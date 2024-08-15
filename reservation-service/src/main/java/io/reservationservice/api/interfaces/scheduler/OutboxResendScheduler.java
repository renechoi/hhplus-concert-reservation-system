package io.reservationservice.api.interfaces.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.reservationservice.api.application.facade.OutboxEventFacade;
import io.reservationservice.api.application.facade.ReservationFacade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/17
 */
@Component
@RequiredArgsConstructor
public class OutboxResendScheduler {
	private final OutboxEventFacade outboxEventFacade;

	/**
	 * <p>
	 * 이 메서드는 스케줄링 되어 5초마다 실행되며, `OutboxEvent` 테이블에서
	 * 생성된 지 5분이 경과했지만 아직 발행되지 않은 이벤트들을 조회합니다.
	 * 조회된 이벤트는 `ApplicationEventPublisher`를 통해 내부 이벤트로 발행된 뒤 카프카로 재발행합니다.
	 *</p>
	 */
	@Scheduled(fixedRateString = "${scheduler.outboxResendRate}")
	public void cancelExpiredReservations() {
		outboxEventFacade.reSend();
	}
}
