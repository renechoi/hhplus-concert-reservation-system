package io.queuemanagement.api.business.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
public interface WaitingQueueTokenRetrievalRepository {

	WaitingQueueToken findTokenByUserIdWithThrows(String  userId);
	Optional<WaitingQueueToken> findTokenByUserOptional(String  userId);

	Optional<WaitingQueueToken> findByUserIdAndStatusOptional(String userId, QueueStatus queueStatus);
	List<WaitingQueueToken> findAllByUserIdAndStatus(String userId, QueueStatus queueStatus);

	List<WaitingQueueToken> findByStatus(QueueStatus queueStatus);
	List<WaitingQueueToken> findByStatusOrderByRequestAtAsc(QueueStatus status);

	List<WaitingQueueToken> findByStatusInAndValidUntilBefore(List<QueueStatus> statuses, LocalDateTime localDateTime);
}
