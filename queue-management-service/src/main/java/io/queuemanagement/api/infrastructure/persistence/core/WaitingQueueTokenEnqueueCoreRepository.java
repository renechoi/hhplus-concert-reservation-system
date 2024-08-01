package io.queuemanagement.api.infrastructure.persistence.core;

import static io.queuemanagement.api.infrastructure.clients.redisservice.dto.QueueDomainServiceRequest.*;
import static java.util.concurrent.CompletableFuture.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenEnqueueRepository;
import io.queuemanagement.api.infrastructure.clients.FeignResponseValidator;
import io.queuemanagement.api.infrastructure.clients.redisservice.RedisServiceClient;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.QueueDomainServiceRequest;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.QueueDomainServiceResponse;
import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenEntity;
import io.queuemanagement.api.infrastructure.persistence.orm.WaitingQueueTokenJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Repository
@RequiredArgsConstructor
public class WaitingQueueTokenEnqueueCoreRepository implements WaitingQueueTokenEnqueueRepository {
	private final WaitingQueueTokenJpaRepository waitingQueueTokenJpaRepository;
	private final RedisServiceClient redisServiceClient;
	private final FeignResponseValidator responseValidator;

	@Override
	public WaitingQueueToken enqueue(WaitingQueueToken waitingQueueToken) {
		// return waitingQueueTokenJpaRepository.save(WaitingQueueTokenEntity.from(waitingQueueToken)).toDomain();
		ResponseEntity<QueueDomainServiceResponse> responseEntity = redisServiceClient.enqueue(waitingEnqueueCommand(waitingQueueToken));
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(QueueDomainServiceResponse::toQueueToken).orElseThrow();
	}

	@Override
	@Async
	public CompletableFuture<WaitingQueueToken> enqueueAsync(WaitingQueueToken waitingQueueToken) {
		return supplyAsync(()->enqueue(waitingQueueToken));
	}





}
