package io.queuemanagement.api.infrastructure.persistence.core;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenRetrievalRepository;
import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenEntity;
import io.queuemanagement.api.infrastructure.persistence.orm.WaitingQueueTokenJpaRepository;
import io.queuemanagement.common.exception.definitions.WaitingQueueTokenNotFoundException;
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
	public WaitingQueueToken findSingleByConditionWithThrows(WaitingQueueTokenSearchCommand searchCommand) {
		return waitingQueueTokenJpaRepository.findSingleByCondition(searchCommand).orElseThrow(WaitingQueueTokenNotFoundException::new).toDomain();
	}

	@Override
	public Optional<WaitingQueueToken> findSingleByConditionOptional(WaitingQueueTokenSearchCommand searchCommand) {
		return waitingQueueTokenJpaRepository.findSingleByCondition(searchCommand).map(WaitingQueueTokenEntity::toDomain);
	}

	@Override
	public List<WaitingQueueToken> findAllByCondition(WaitingQueueTokenSearchCommand searchCommand) {
		return waitingQueueTokenJpaRepository.findAllByCondition(searchCommand).stream().map(WaitingQueueTokenEntity::toDomain).collect(Collectors.toList());
	}
}
