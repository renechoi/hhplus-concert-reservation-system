package io.queuemanagement.api.infrastructure.persistence.core;

import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.persistence.ProcessingQueueEnqueueRepository;
import io.queuemanagement.api.infrastructure.entity.ProcessingQueueTokenEntity;
import io.queuemanagement.api.infrastructure.persistence.orm.ProcessingQueueTokenJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
@Repository
@RequiredArgsConstructor
public class ProcessingQueueEnqueueCoreRepository implements ProcessingQueueEnqueueRepository {

	private final ProcessingQueueTokenJpaRepository processingQueueTokenJpaRepository;

	@Override
	public ProcessingQueueToken enqueue(ProcessingQueueToken processingQueueToken) {
		return processingQueueTokenJpaRepository.save(ProcessingQueueTokenEntity.from(processingQueueToken)).toDomain();
	}
}
