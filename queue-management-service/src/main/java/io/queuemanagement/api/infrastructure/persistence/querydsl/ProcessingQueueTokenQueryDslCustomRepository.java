package io.queuemanagement.api.infrastructure.persistence.querydsl;

import java.util.List;
import java.util.Optional;

import io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand;
import io.queuemanagement.api.infrastructure.entity.ProcessingQueueTokenEntity;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface ProcessingQueueTokenQueryDslCustomRepository {
	Optional<ProcessingQueueTokenEntity> findSingleByCondition(ProcessingQueueTokenSearchCommand searchCommand);

	List<ProcessingQueueTokenEntity> findAllByCondition(ProcessingQueueTokenSearchCommand searchCommand);
}
