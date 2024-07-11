package io.queuemanagement.api.infrastructure.persistence.core;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenRetrievalRepository;
import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenEntity;
import io.queuemanagement.api.infrastructure.persistence.orm.WaitingQueueTokenJpaRepository;
import io.queuemanagement.common.exception.WaitingQueueTokenNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
@Repository
@RequiredArgsConstructor
public class WaitingQueueTokenRetrievalCoreRepository implements WaitingQueueTokenRetrievalRepository {
	private final WaitingQueueTokenJpaRepository waitingQueueTokenJpaRepository;

	@Override
	public WaitingQueueToken findTokenByUserIdWithThrows(String  userId) {
		return waitingQueueTokenJpaRepository.findTopByUserIdOrderByRequestAtDesc(userId).orElseThrow(WaitingQueueTokenNotFoundException::new).toDomain();
	}

	@Override
	public Optional<WaitingQueueToken> findTokenByUserOptional(String userId) {
		return waitingQueueTokenJpaRepository.findByUserId(userId).map(WaitingQueueTokenEntity::toDomain);
	}

	@Override
	public Optional<WaitingQueueToken> findByUserIdAndStatusOptional(String userId, QueueStatus queueStatus) {
		return waitingQueueTokenJpaRepository.findByUserIdAndStatus(userId, queueStatus)
			.map(WaitingQueueTokenEntity::toDomain);
	}

	@Override
	public List<WaitingQueueToken> findAllByUserIdAndStatus(String userId, QueueStatus queueStatus) {
		return waitingQueueTokenJpaRepository.findAllByUserIdAndStatus(userId,queueStatus).stream().map(WaitingQueueTokenEntity::toDomain).collect(Collectors.toList());
	}

	@Override
	public List<WaitingQueueToken> findByStatus(QueueStatus queueStatus) {
		return waitingQueueTokenJpaRepository.findByStatus(queueStatus).stream().map(WaitingQueueTokenEntity::toDomain).collect(Collectors.toList());
	}

	@Override
	public List<WaitingQueueToken> findByStatusOrderByRequestAtAsc(QueueStatus queueStatus) {
		return waitingQueueTokenJpaRepository.findByStatus(queueStatus).stream().map(WaitingQueueTokenEntity::toDomain).collect(Collectors.toList());
	}

	@Override
	public List<WaitingQueueToken> findByStatusInAndValidUntilBefore(List<QueueStatus> statuses, LocalDateTime localDateTime) {
		return waitingQueueTokenJpaRepository.findByStatusInAndValidUntilBefore(statuses, localDateTime).stream()
			.map(WaitingQueueTokenEntity::toDomain).collect(Collectors.toList());

	}
}
