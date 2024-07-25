package io.queuemanagement.api.infrastructure.persistence.querydsl;

import java.util.Optional;

import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenCounterEntity;

/**
 * @author : Rene Choi
 * @since : 2024/07/24
 */
public interface WaitingQueueTokenCounterQueryDslCustomRepository {
	Optional<WaitingQueueTokenCounterEntity> getAndLockCounter();

}
