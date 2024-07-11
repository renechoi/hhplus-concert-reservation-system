package io.apiorchestrationservice.api.infrastructure.clients.queuemanagement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;

import io.apiorchestrationservice.api.business.client.QueueManagementClientAdapter;
import io.apiorchestrationservice.api.business.dto.outport.ProcessingQueueTokenInfo;
import io.apiorchestrationservice.api.infrastructure.clients.queuemanagement.dto.ProcessingQueueTokenDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.validator.FeignResponseValidator;
import io.apiorchestrationservice.common.annotation.FeignAdapter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@FeignAdapter
@RequiredArgsConstructor
public class QueueManagementClientFeignAdapter implements QueueManagementClientAdapter {


	private final QueueManagementServiceClient queueManagementServiceClient;
	private final FeignResponseValidator responseValidator;


	@Override
	public Optional<ProcessingQueueTokenInfo> retrieveToken(String tokenValue, String userId) {
		ResponseEntity<ProcessingQueueTokenDomainServiceResponse> responseEntity = queueManagementServiceClient.checkProcessingQueueTokenAvailabilityWithUserId(tokenValue, userId);
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(ProcessingQueueTokenDomainServiceResponse::toInfo);
		// return null;
	}

	@Override
	public Optional<ProcessingQueueTokenInfo> retrieveToken(String tokenValue) {
		ResponseEntity<ProcessingQueueTokenDomainServiceResponse> responseEntity = queueManagementServiceClient.checkProcessingQueueTokenAvailabilityWithoutUserId(tokenValue);
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(ProcessingQueueTokenDomainServiceResponse::toInfo);
		// return null;
	}
}
