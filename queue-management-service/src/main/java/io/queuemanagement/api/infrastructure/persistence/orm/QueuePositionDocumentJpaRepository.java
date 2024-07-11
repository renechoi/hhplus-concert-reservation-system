package io.queuemanagement.api.infrastructure.persistence.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import io.queuemanagement.api.infrastructure.entity.WaitingQueuePositionJsonEntity;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public interface QueuePositionDocumentJpaRepository extends JpaRepository<WaitingQueuePositionJsonEntity, Long> {
}
