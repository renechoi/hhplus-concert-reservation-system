package io.clientchannelservice.fixture;

import java.time.LocalDateTime;

import io.clientchannelservice.api.business.domainmodel.QueueStatus;
import io.clientchannelservice.api.infrastructure.clients.dto.WaitingQueueTokenDomainServiceResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public class WaitingQueueTokenFixture {

	public static WaitingQueueTokenDomainServiceResponse createWaitingQueueTokenDefaultFixtureResponse() {
		return new WaitingQueueTokenDomainServiceResponse(
			1L,
			"123",
			"token123",
			1L,
			LocalDateTime.of(2024, 7, 31, 12, 0),
			QueueStatus.WAITING,
			LocalDateTime.of(2024, 7, 7, 10, 0)
		);
	}

	public static WaitingQueueTokenDomainServiceResponse createWaitingQueueTokenFixtureResponse(String userId) {
		return new WaitingQueueTokenDomainServiceResponse(
			1L,
			userId,
			"token_" + userId,
			1L,
			LocalDateTime.now().plusMinutes(30),
			QueueStatus.WAITING,
			LocalDateTime.now()
		);
	}
}