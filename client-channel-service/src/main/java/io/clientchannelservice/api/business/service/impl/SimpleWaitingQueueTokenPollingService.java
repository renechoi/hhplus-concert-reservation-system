package io.clientchannelservice.api.business.service.impl;

import org.springframework.stereotype.Service;

import io.clientchannelservice.api.business.client.WaitingQueueTokenClientAdapter;
import io.clientchannelservice.api.business.dto.outport.WaitingQueueTokenPollingInfo;
import io.clientchannelservice.api.business.service.WaitingQueueTokenPollingService;
import io.clientchannelservice.common.exception.WaitingQueueTokenNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Service
@RequiredArgsConstructor
public class SimpleWaitingQueueTokenPollingService implements WaitingQueueTokenPollingService {

	private final WaitingQueueTokenClientAdapter waitingQueueTokenClientAdapter;

	/**
	 * todo -> 요청시에 캐시를 1차 조회하거나, 아니면 도메인 서비스에서 반환할 때 캐시를 리턴하도록 to be refactored
	 */
	@Override
	public WaitingQueueTokenPollingInfo retrieveToken(String userId) {
		return waitingQueueTokenClientAdapter.retrieveToken(userId).orElseThrow(WaitingQueueTokenNotFoundException::new);
	}
}
