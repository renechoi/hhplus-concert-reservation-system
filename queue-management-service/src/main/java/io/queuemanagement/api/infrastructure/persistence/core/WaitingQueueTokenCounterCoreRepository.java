package io.queuemanagement.api.infrastructure.persistence.core;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.WaitingQueueTokenCounter;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenCounterCrudRepository;
import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenCounterEntity;
import io.queuemanagement.api.infrastructure.persistence.orm.WaitingQueueTokenCounterJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
@Repository
@RequiredArgsConstructor
public class WaitingQueueTokenCounterCoreRepository implements WaitingQueueTokenCounterCrudRepository {

	private final WaitingQueueTokenCounterJpaRepository waitingQueueTokenCounterJpaRepository;

	@Override
	public void save(WaitingQueueTokenCounter counter) {
		waitingQueueTokenCounterJpaRepository.save(WaitingQueueTokenCounterEntity.from(counter));
	}

	@Override
	public Optional<WaitingQueueTokenCounter> findByIdOptional(Long id) {
		return waitingQueueTokenCounterJpaRepository.findById(id).map(WaitingQueueTokenCounterEntity::toDomain);
	}

	@Override
	public WaitingQueueTokenCounter getCounterWithThrows() {
		return waitingQueueTokenCounterJpaRepository.findById(1L).orElseThrow().toDomain();
	}

	@Override
	public WaitingQueueTokenCounter getOrInitializeCounter() {
		return waitingQueueTokenCounterJpaRepository.findById(1L).map(WaitingQueueTokenCounterEntity::toDomain).orElseGet(WaitingQueueTokenCounter::createInitializingCounter);
	}


}
