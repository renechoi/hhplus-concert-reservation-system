package io.queuemanagement.api.infrastructure.clients.redisservice;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import io.queuemanagement.api.business.client.RedisClientAdapter;
import io.queuemanagement.api.business.dto.inport.CounterCommand;
import io.queuemanagement.api.business.dto.inport.EnqueueCommand;
import io.queuemanagement.api.business.dto.outport.CounterInfo;
import io.queuemanagement.api.business.dto.outport.QueueInfo;
import io.queuemanagement.api.business.dto.outport.QueueMemberRankInfo;
import io.queuemanagement.api.infrastructure.clients.FeignResponseValidator;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.CounterDomainServiceRequest;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.CounterDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.MemberRankDomainServiceResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.QueueDomainServiceRequest;
import io.queuemanagement.api.infrastructure.clients.redisservice.dto.QueueDomainServiceResponse;
import io.queuemanagement.common.annotation.FeignAdapter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
@FeignAdapter
@RequiredArgsConstructor
public class RedisClientFeignAdapter implements RedisClientAdapter {
	private final RedisServiceClient redisServiceClient;
	private final FeignResponseValidator responseValidator;


	@Override
	public QueueInfo enqueue(EnqueueCommand request) {
		ResponseEntity<QueueDomainServiceResponse> responseEntity = redisServiceClient.enqueue(QueueDomainServiceRequest.from(request));
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(QueueDomainServiceResponse::toQueueInfo).orElseThrow();
	}

	@Async
	@Override
	public CompletableFuture<QueueInfo> enqueueAsync(EnqueueCommand request) {
		return CompletableFuture.completedFuture(enqueue(request));
	}

	@Override
	public QueueMemberRankInfo getMemberRank(String queueName, String member) {
		ResponseEntity<MemberRankDomainServiceResponse> responseEntity = redisServiceClient.getMemberRank(queueName, member);
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(MemberRankDomainServiceResponse::toQueueMemberRankInfo).orElseThrow();
	}


	@Override
	public CounterInfo increment(CounterCommand command) {
		ResponseEntity<CounterDomainServiceResponse> responseEntity = redisServiceClient.increment(new CounterDomainServiceRequest(command.getCounterKey(), command.getValue()));
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(response -> new CounterInfo(response.getCounterKey(), response.getValue())).orElseThrow();
	}



	@Override
	public CounterInfo decrement(CounterCommand command) {
		ResponseEntity<CounterDomainServiceResponse> responseEntity = redisServiceClient.decrement(new CounterDomainServiceRequest(command.getCounterKey(), command.getValue()));
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(response -> new CounterInfo(response.getCounterKey(), response.getValue())).orElseThrow();
	}


	@Override
	public CounterInfo getCounter(String counterKey) {
		ResponseEntity<CounterDomainServiceResponse> responseEntity = redisServiceClient.getCounter(counterKey);
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(response -> new CounterInfo(response.getCounterKey(), response.getValue())).orElseThrow();
	}
}
