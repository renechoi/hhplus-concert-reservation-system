package io.queuemanagement.api.infrastructure.persistence.core;

import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.persistence.ProcessingQueueStoreRepository;
import io.queuemanagement.api.infrastructure.entity.ProcessingQueueTokenEntity;
import io.queuemanagement.api.infrastructure.persistence.orm.ProcessingQueueTokenJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Repository
@RequiredArgsConstructor
public class SimpleProcessingQueueStoreRepository implements ProcessingQueueStoreRepository {

	private final ProcessingQueueTokenJpaRepository processingQueueTokenJpaRepository;
	@Override
	public ProcessingQueueToken store(ProcessingQueueToken processingQueueToken) {
		return processingQueueTokenJpaRepository.save(ProcessingQueueTokenEntity.from(processingQueueToken)).toDomain();
	}
}
