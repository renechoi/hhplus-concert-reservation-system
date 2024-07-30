package io.apiorchestrationservice.api.infrastructure.clients.redisservice;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.apiorchestrationservice.api.business.client.RedisClientAdapter;
import io.apiorchestrationservice.api.business.dto.inport.DistributedLockCommand;
import io.apiorchestrationservice.api.business.dto.inport.DistributedUnLockCommand;
import io.apiorchestrationservice.api.business.dto.outport.DistributedLockInfo;
import io.apiorchestrationservice.api.business.dto.outport.DistributedUnLockInfo;
import io.apiorchestrationservice.api.infrastructure.clients.validator.FeignResponseValidator;
import io.apiorchestrationservice.api.infrastructure.clients.redisservice.dto.LockDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.redisservice.dto.LockDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.redisservice.dto.UnlockDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.redisservice.dto.UnlockDomainServiceResponse;
import io.apiorchestrationservice.common.annotation.FeignAdapter;
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
	public DistributedLockInfo lock(DistributedLockCommand command) {
		ResponseEntity<LockDomainServiceResponse> responseEntity = redisServiceClient.lock(LockDomainServiceRequest.from(command));
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(LockDomainServiceResponse::toLockInfo).orElseThrow();
	}

	@Override
	public DistributedUnLockInfo unlock(DistributedUnLockCommand request) {
		ResponseEntity<UnlockDomainServiceResponse> responseEntity = redisServiceClient.unlock(UnlockDomainServiceRequest.from(request));
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(UnlockDomainServiceResponse::toUnlockInfo).orElseThrow();
	}
}
