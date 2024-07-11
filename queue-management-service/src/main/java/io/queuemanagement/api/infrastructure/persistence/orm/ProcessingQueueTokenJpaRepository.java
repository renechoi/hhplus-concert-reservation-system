package io.queuemanagement.api.infrastructure.persistence.orm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.infrastructure.entity.ProcessingQueueTokenEntity;
import io.queuemanagement.api.infrastructure.persistence.querydsl.ProcessingQueueTokenQueryDslCustomRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public interface ProcessingQueueTokenJpaRepository extends JpaRepository<ProcessingQueueTokenEntity, Long>, ProcessingQueueTokenQueryDslCustomRepository {
	long countByStatus(QueueStatus status);
	Optional<ProcessingQueueTokenEntity> findByTokenValue(String tokenValue);
	List<ProcessingQueueTokenEntity> findAllByStatusAndValidUntilBefore(QueueStatus status, LocalDateTime time);

	Optional<ProcessingQueueTokenEntity> findByUserId(String  userId);
}
