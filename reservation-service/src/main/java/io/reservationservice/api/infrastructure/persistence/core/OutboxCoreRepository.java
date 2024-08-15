package io.reservationservice.api.infrastructure.persistence.core;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import io.reservationservice.api.business.domainentity.OutboxEvent;
import io.reservationservice.api.business.domainentity.OutboxRepository;
import io.reservationservice.api.business.domainentity.OutboxStatus;
import io.reservationservice.api.infrastructure.persistence.orm.OutboxJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
@Repository
@RequiredArgsConstructor
public class OutboxCoreRepository implements OutboxRepository {

	private final OutboxJpaRepository outboxJpaRepository;

	@Override
	public OutboxEvent save(OutboxEvent outboxEvent) {
		return outboxJpaRepository.save(outboxEvent);
	}

	@Override
	public Optional<OutboxEvent> fetch(Long outboxEventId) {
		return outboxJpaRepository.findById(outboxEventId);
	}

	@Override
	public List<OutboxEvent> fetchUnsentEvents(LocalDateTime olderThan) {
		return outboxJpaRepository.findByStatusAndCreatedAtBefore(OutboxStatus.INIT, olderThan);
	}
}
