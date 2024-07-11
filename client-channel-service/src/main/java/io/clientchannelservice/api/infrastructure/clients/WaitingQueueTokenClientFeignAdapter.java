package io.clientchannelservice.api.infrastructure.clients;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import io.clientchannelservice.api.business.client.WaitingQueueTokenClientAdapter;
import io.clientchannelservice.api.business.dto.outport.WaitingQueueTokenPollingInfo;
import io.clientchannelservice.api.infrastructure.clients.dto.WaitingQueueTokenDomainServiceResponse;
import io.clientchannelservice.api.infrastructure.clients.validator.FeignResponseValidator;
import io.clientchannelservice.common.annotation.FeignAdapter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@FeignAdapter
@RequiredArgsConstructor
public class WaitingQueueTokenClientFeignAdapter implements WaitingQueueTokenClientAdapter {
	private final WaitingQueueTokenClient waitingQueueTokenClient;
	private final FeignResponseValidator responseValidator;

	@Override
	public Optional<WaitingQueueTokenPollingInfo> retrieveToken(String userId) {
		ResponseEntity<WaitingQueueTokenDomainServiceResponse> responseEntity = waitingQueueTokenClient.retrieveWaitingQueueToken(userId);
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(WaitingQueueTokenDomainServiceResponse::toInfo);
	}
}
