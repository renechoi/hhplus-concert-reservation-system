package io.queuemanagement.api.business.persistence;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface WaitingQueueRepository {

	WaitingQueueToken enqueue(WaitingQueueToken waitingQueueToken);

	@Async
	CompletableFuture<WaitingQueueToken> enqueueAsync(WaitingQueueToken waitingQueueToken);

	void remove(WaitingQueueToken waitingQueueToken);

	WaitingQueueToken retrieveToken(String userId);

	List<WaitingQueueToken> retrieveTopTokens(int count);
}
