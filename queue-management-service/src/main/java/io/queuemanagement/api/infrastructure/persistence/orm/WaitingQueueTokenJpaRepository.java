package io.queuemanagement.api.infrastructure.persistence.orm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenEntity;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface WaitingQueueTokenJpaRepository extends JpaRepository<WaitingQueueTokenEntity, Long> {
	Optional<WaitingQueueTokenEntity> findByUserId(String  userId);
	Optional<WaitingQueueTokenEntity> findTopByUserIdOrderByRequestAtDesc(String userId);
	Optional<WaitingQueueTokenEntity> findByUserIdAndStatus(String userId, QueueStatus status);
	List<WaitingQueueTokenEntity> findAllByUserIdAndStatus(String userId, QueueStatus status);
	List<WaitingQueueTokenEntity> findByStatus(QueueStatus status);
	List<WaitingQueueTokenEntity> findByStatusInAndValidUntilBefore(List<QueueStatus> statuses, LocalDateTime now);

}
