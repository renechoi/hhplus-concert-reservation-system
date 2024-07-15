package io.clientchannelservice.api.application.facade;

import io.clientchannelservice.api.application.dto.response.WaitingQueueTokenPollingResponse;
import io.clientchannelservice.api.business.service.WaitingQueueTokenPollingService;
import io.clientchannelservice.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Facade
@RequiredArgsConstructor
public class WaitingQueueTokenPollingFacade {

	private final WaitingQueueTokenPollingService waitingQueueTokenPollingService;

	public WaitingQueueTokenPollingResponse retrieveToken(String userId) {
		return WaitingQueueTokenPollingResponse.from(waitingQueueTokenPollingService.retrieveToken(userId));
	}

}
