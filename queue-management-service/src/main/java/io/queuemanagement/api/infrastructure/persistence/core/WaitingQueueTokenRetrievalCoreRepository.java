package io.queuemanagement.api.infrastructure.persistence.core;

import static io.queuemanagement.api.business.domainmodel.QueueName.*;
import static io.queuemanagement.api.business.domainmodel.WaitingQueueToken.*;
import static io.queuemanagement.common.token.QueueTokenGenerator.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.QueueName;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenRetrievalRepository;
import io.queuemanagement.api.infrastructure.clients.redisservice.RedisServiceClient;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.MemberRankDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.TopNMembersDomainServiceResponse;
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
	private final RedisServiceClient redisServiceClient;



	@Override
	public WaitingQueueToken findToken(String userId) {
		String tokenValue = generateToken(userId);

		ResponseEntity<MemberRankDomainServiceResponse> response = redisServiceClient.getMemberRank(WAITING_QUEUE.getValue(), tokenValue);
		if (response.getStatusCode().value() != 200 || response.getBody() == null) {
			return WaitingQueueToken.empty();
		}

		return tokenWithPosition(userId, response.getBody().getRank());

	}

	@Override
	public List<WaitingQueueToken> findTopTokens(int count) {
		TopNMembersDomainServiceResponse response = redisServiceClient.getTopNMembers(WAITING_QUEUE.getValue(), count).getBody();

		if (response != null) {
			return response.getMembers().stream()
				.map(WaitingQueueToken::valueOf)
				.collect(Collectors.toList());
		}
		return List.of();
	}












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
	 *
	 * @param searchCommand 검색 조건
	 * @return 조건에 맞는 대기열 토큰을 Optional로 반환하며, 없을 경우 빈 Optional을 반환
	 */
	@Override
	public Optional<WaitingQueueToken> findOptionalByConditionWithLock(WaitingQueueTokenSearchCommand searchCommand) {
		return waitingQueueTokenJpaRepository.findOptionalByConditionWithLock(searchCommand).map(WaitingQueueTokenEntity::toDomain);
	}


}
