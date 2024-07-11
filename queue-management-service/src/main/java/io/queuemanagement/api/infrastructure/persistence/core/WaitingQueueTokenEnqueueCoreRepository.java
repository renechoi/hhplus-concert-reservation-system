package io.queuemanagement.api.infrastructure.persistence.core;

import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenEnqueueRepository;
import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenEntity;
import io.queuemanagement.api.infrastructure.persistence.orm.WaitingQueueTokenJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Repository
@RequiredArgsConstructor
public class WaitingQueueTokenEnqueueCoreRepository implements WaitingQueueTokenEnqueueRepository {
	private final WaitingQueueTokenJpaRepository waitingQueueTokenJpaRepository;

	@Override
	public WaitingQueueToken enqueue(WaitingQueueToken waitingQueueToken) {
		return waitingQueueTokenJpaRepository.save(WaitingQueueTokenEntity.from(waitingQueueToken)).toDomain();
	}



}
