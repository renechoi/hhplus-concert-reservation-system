package io.apiorchestrationservice.api.business.client;

import java.util.Optional;

import io.apiorchestrationservice.api.business.dto.outport.ProcessingQueueTokenInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface QueueManagementClientAdapter {
	Optional<ProcessingQueueTokenInfo> retrieveToken(String tokenValue, String userId);

	Optional<ProcessingQueueTokenInfo> retrieveToken(String tokenValue);
}
