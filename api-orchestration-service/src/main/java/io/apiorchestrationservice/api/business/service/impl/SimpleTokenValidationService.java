package io.apiorchestrationservice.api.business.service.impl;

import org.springframework.stereotype.Service;

import io.apiorchestrationservice.api.business.client.QueueManagementClientAdapter;
import io.apiorchestrationservice.api.business.service.TokenValidationService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Service
@RequiredArgsConstructor
public class SimpleTokenValidationService implements TokenValidationService {

	private final QueueManagementClientAdapter queueManagementClientAdapter;

	@Override
	public boolean validateQueueToken(String tokenValue) {
		return queueManagementClientAdapter
			.retrieveToken(tokenValue)
			.map(processingQueueTokenInfo -> processingQueueTokenInfo.hasSameTokenValue(tokenValue))
			.orElse(false);
	}
}
