package io.reservationservice.api.business.service.impl;

import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.reservationservice.api.business.domainentity.OutboxEvent;
import io.reservationservice.api.business.domainentity.OutboxRepository;
import io.reservationservice.api.business.dto.inport.OutboxStatusCompleteCommand;
import io.reservationservice.api.business.service.OutboxEventService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
@Service
@RequiredArgsConstructor
public class SimpleOutboxEventService implements OutboxEventService {

	private final OutboxRepository outboxRepository;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	@Transactional
	public void complete(OutboxStatusCompleteCommand command) {
		outboxRepository.fetch(command.getOutboxEventId())
			.ifPresent(OutboxEvent::complete);
	}



	/**
	 * 5분 이상 `INIT` 상태로 남아 있는 `OutboxEvent`들을 조회하여 이벤트를 발행하는 메서드입니다.
	 *
	 * <p>이 메서드는 트랜잭션 내에서 실행되며, 발행되지 않은 이벤트들을 안전하게 발행할 수 있도록 보장합니다.
	 *
	 * @see OutboxRepository#fetchUnsentEvents(LocalDateTime)
	 * @see ApplicationEventPublisher#publishEvent(Object)
	 */
	@Transactional
	@Override
	public void publishUnsentEvents() {
		List<OutboxEvent> unsentEvents = outboxRepository.fetchUnsentEvents(now().minusMinutes(5));
		unsentEvents.forEach(this::sendAlertToDeveloper);
		unsentEvents.forEach(applicationEventPublisher::publishEvent);
	}

	private void sendAlertToDeveloper(OutboxEvent outboxEvent) {
		// 필요에 따른 direct alert 구현
	}



}
