package io.queuemanagement.api.infrastructure.persistence.querydsl;

import java.util.List;
import java.util.Optional;

import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand;
import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenEntity;

/**
 * @author : Rene Choi
 * @since : 2024/07/15
 */
public interface WaitingQueueTokenQueryDslCustomRepository {
	Optional<WaitingQueueTokenEntity> findSingleByCondition(WaitingQueueTokenSearchCommand searchCommand);

	List<WaitingQueueTokenEntity> findAllByCondition(WaitingQueueTokenSearchCommand searchCommand);

	Optional<WaitingQueueTokenEntity> findOptionalByConditionWithLock(WaitingQueueTokenSearchCommand searchCommand);
}
