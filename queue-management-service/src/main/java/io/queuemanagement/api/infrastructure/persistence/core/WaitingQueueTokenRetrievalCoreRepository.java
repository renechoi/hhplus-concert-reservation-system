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
	public WaitingQueueToken findSingleByCondition(WaitingQueueTokenSearchCommand searchCommand) {
		return waitingQueueTokenJpaRepository.findSingleByCondition(searchCommand).orElseThrow(WaitingQueueTokenNotFoundException::new).toDomain();
	}

	@Override
	public Optional<WaitingQueueToken> findOptionalByCondition(WaitingQueueTokenSearchCommand searchCommand) {
		return waitingQueueTokenJpaRepository.findSingleByCondition(searchCommand).map(WaitingQueueTokenEntity::toDomain);
	}

	@Override
	public List<WaitingQueueToken> findAllByCondition(WaitingQueueTokenSearchCommand searchCommand) {
		return waitingQueueTokenJpaRepository.findAllByCondition(searchCommand).stream().map(WaitingQueueTokenEntity::toDomain).collect(Collectors.toList());
	}


	/**
	 * 주어진 검색 조건에 따라 대기열 토큰을 조회하고, 비관적 락을 적용하여 동시성 이슈를 방지한다.
	 * @param searchCommand 검색 조건
	 * @return 조건에 맞는 대기열 토큰을 Optional로 반환하며, 없을 경우 빈 Optional을 반환
	 */
	@Override
	public Optional<WaitingQueueToken> findOptionalByConditionWithLock(WaitingQueueTokenSearchCommand searchCommand) {
		return waitingQueueTokenJpaRepository.findOptionalByConditionWithLock(searchCommand).map(WaitingQueueTokenEntity::toDomain);
	}
}
