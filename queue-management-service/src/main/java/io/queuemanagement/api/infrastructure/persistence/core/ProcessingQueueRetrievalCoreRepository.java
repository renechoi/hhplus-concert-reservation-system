package io.queuemanagement.api.infrastructure.persistence.core;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand;
import io.queuemanagement.api.business.persistence.ProcessingQueueRetrievalRepository;
import io.queuemanagement.api.infrastructure.entity.ProcessingQueueTokenEntity;
import io.queuemanagement.api.infrastructure.persistence.orm.ProcessingQueueTokenJpaRepository;
import io.queuemanagement.common.exception.ProcessingQueueTokenNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
@Repository
@RequiredArgsConstructor
public class ProcessingQueueRetrievalCoreRepository implements ProcessingQueueRetrievalRepository {
	private final ProcessingQueueTokenJpaRepository processingQueueTokenJpaRepository;

	@Override
	public long countByStatus(QueueStatus queueStatus) {
		return processingQueueTokenJpaRepository.countByStatus(queueStatus);
	}

	@Override
	public ProcessingQueueToken findSingleByConditionWithThrows(ProcessingQueueTokenSearchCommand searchCommand) {
		return processingQueueTokenJpaRepository.findSingleByCondition(searchCommand).orElseThrow(ProcessingQueueTokenNotFoundException::new).toDomain();
	}

	@Override
	public Optional<ProcessingQueueToken> findSingleByConditionOptional(ProcessingQueueTokenSearchCommand searchCommand) {
		return processingQueueTokenJpaRepository.findSingleByCondition(searchCommand).map(ProcessingQueueTokenEntity::toDomain);
	}

	@Override
	public Optional<ProcessingQueueToken> findTokenByUserOptional(String userId) {
		return processingQueueTokenJpaRepository.findByUserId(userId).map(ProcessingQueueTokenEntity::toDomain);
	}

	@Override
	public List<ProcessingQueueToken> findAllByStatusAndValidUntilBefore(QueueStatus queueStatus, LocalDateTime time) {
		return processingQueueTokenJpaRepository.findAllByStatusAndValidUntilBefore(queueStatus, time)
			.stream()
			.map(ProcessingQueueTokenEntity::toDomain)
			.toList();
	}


}
