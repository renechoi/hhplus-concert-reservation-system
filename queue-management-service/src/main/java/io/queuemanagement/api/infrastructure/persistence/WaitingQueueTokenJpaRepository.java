package io.queuemanagement.api.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenEntity;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface WaitingQueueTokenJpaRepository extends JpaRepository<WaitingQueueTokenEntity, Long> {
}
