package io.queuemanagement.api.infrastructure.persistence.core;

import static io.queuemanagement.api.business.domainmodel.QueueName.*;
import static io.queuemanagement.api.business.domainmodel.WaitingQueueToken.*;
import static io.queuemanagement.api.infrastructure.clients.redisservice.dto.QueueDomainServiceRequest.*;
import static io.queuemanagement.common.token.QueueTokenGenerator.*;
import static java.util.concurrent.CompletableFuture.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.persistence.WaitingQueueRepository;
import io.queuemanagement.api.infrastructure.clients.FeignResponseValidator;
import io.queuemanagement.api.infrastructure.clients.redisservice.RedisServiceClient;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.MemberRankDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.QueueDomainServiceRequest;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.QueueDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.TopNMembersDomainServiceResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Repository
@RequiredArgsConstructor
public class WaitingQueueEnqueueCoreRepository implements WaitingQueueRepository {
	private final RedisServiceClient redisServiceClient;
	private final FeignResponseValidator responseValidator;

	@Override
	public WaitingQueueToken enqueue(WaitingQueueToken waitingQueueToken) {
		ResponseEntity<QueueDomainServiceResponse> responseEntity = redisServiceClient.enqueue(waitingEnqueueCommand(waitingQueueToken));
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(QueueDomainServiceResponse::toQueueToken).orElseThrow();
	}

	@Override
	@Async
	public CompletableFuture<WaitingQueueToken> enqueueAsync(WaitingQueueToken waitingQueueToken) {
		return supplyAsync(() -> enqueue(waitingQueueToken));
	}



	@Override
	public void remove(WaitingQueueToken waitingQueueToken) {
		QueueDomainServiceRequest request = dequeRequest(waitingQueueToken);
		redisServiceClient.dequeue(request);
	}



	@Override
	public WaitingQueueToken retrieveToken(String userId) {
		String tokenValue = generateToken(userId);

		ResponseEntity<MemberRankDomainServiceResponse> response = redisServiceClient.getMemberRank(WAITING_QUEUE.getValue(), tokenValue);
		if (response.getStatusCode().value() != 200 || response.getBody() == null) {
			return WaitingQueueToken.empty();
		}

		return tokenWithPosition(userId, response.getBody().getRank());

	}

	@Override
	public List<WaitingQueueToken> retrieveTopTokens(int count) {
		TopNMembersDomainServiceResponse response = redisServiceClient.getTopNMembers(WAITING_QUEUE.getValue(), count).getBody();

		if (response != null) {
			return response.getMembers().stream()
				.map(WaitingQueueToken::valueOf)
				.collect(Collectors.toList());
		}
		return List.of();
	}

}
