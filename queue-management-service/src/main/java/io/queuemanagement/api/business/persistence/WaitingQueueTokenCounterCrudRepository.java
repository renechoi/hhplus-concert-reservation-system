package io.queuemanagement.api.business.persistence;

import java.util.Optional;

import io.queuemanagement.api.business.domainmodel.WaitingQueueTokenCounter;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
public interface WaitingQueueTokenCounterCrudRepository {
	void save(WaitingQueueTokenCounter counter);
	Optional<WaitingQueueTokenCounter> findByIdOptional(Long id);

	WaitingQueueTokenCounter getCounter();

	WaitingQueueTokenCounter getOrInitializeCounter();
}
