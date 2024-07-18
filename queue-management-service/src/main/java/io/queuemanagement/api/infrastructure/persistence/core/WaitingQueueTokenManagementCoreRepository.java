package io.queuemanagement.api.infrastructure.persistence.core;

import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenManagementRepository;
import io.queuemanagement.api.infrastructure.persistence.orm.WaitingQueueTokenJpaRepository;
import io.queuemanagement.common.exception.definitions.WaitingQueueTokenNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Repository
@RequiredArgsConstructor
public class WaitingQueueTokenManagementCoreRepository implements WaitingQueueTokenManagementRepository {
	private final WaitingQueueTokenJpaRepository waitingQueueTokenJpaRepository;

	@Override
	public WaitingQueueToken updateStatus(WaitingQueueToken waitingQueueToken) {
		return waitingQueueTokenJpaRepository
			.findById(waitingQueueToken.getWaitingQueueTokenId())
			.orElseThrow(WaitingQueueTokenNotFoundException::new)
			.updateStatus(waitingQueueToken.getStatus())
			.toDomain();
	}

	@Override
	public WaitingQueueToken updatePosition(WaitingQueueToken waitingQueueToken) {
		return waitingQueueTokenJpaRepository
			.findById(waitingQueueToken.getWaitingQueueTokenId())
			.orElseThrow(WaitingQueueTokenNotFoundException::new)
			.updatePosition(waitingQueueToken.getPosition())
			.toDomain();
	}
}
