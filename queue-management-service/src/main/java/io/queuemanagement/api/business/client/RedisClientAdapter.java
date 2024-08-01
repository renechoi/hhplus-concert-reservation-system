package io.queuemanagement.api.business.client;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

import io.queuemanagement.api.business.dto.inport.CounterCommand;
import io.queuemanagement.api.business.dto.inport.EnqueueCommand;
import io.queuemanagement.api.business.dto.outport.CounterInfo;
import io.queuemanagement.api.business.dto.outport.QueueInfo;
import io.queuemanagement.api.business.dto.outport.QueueMemberRankInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
public interface RedisClientAdapter {

	QueueInfo enqueue(EnqueueCommand request);

	@Async
	CompletableFuture<QueueInfo> enqueueAsync(EnqueueCommand request);

	QueueMemberRankInfo getMemberRank(String queueName, String member);

	CounterInfo increment(CounterCommand command);

	CounterInfo decrement(CounterCommand command);

	CounterInfo getCounter(String counterKey);
}
