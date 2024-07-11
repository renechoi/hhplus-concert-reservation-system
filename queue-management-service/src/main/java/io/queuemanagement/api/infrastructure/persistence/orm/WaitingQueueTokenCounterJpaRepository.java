package io.queuemanagement.api.infrastructure.persistence.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenCounterEntity;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
public interface WaitingQueueTokenCounterJpaRepository extends JpaRepository<WaitingQueueTokenCounterEntity, Long> {
}
