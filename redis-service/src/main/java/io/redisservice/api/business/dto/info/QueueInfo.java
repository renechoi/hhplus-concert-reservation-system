package io.redisservice.api.business.dto.info;

import io.redisservice.api.business.dto.command.DequeueCommand;
import io.redisservice.api.business.dto.command.EnqueueCommand;
import io.redisservice.api.business.dto.command.RemoveMembersByRankCommand;
import io.redisservice.api.business.dto.command.RemoveMembersByScoreCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */

public record QueueInfo(String queueName, String member, double score, boolean success, long size) {

	public static QueueInfo createQueueInfo(EnqueueCommand command, boolean success) {
		return new QueueInfo(command.getQueueName(), command.getMember(), command.getScore(), success, 0);
	}

	public static QueueInfo createQueueInfo(DequeueCommand command, boolean success) {
		return new QueueInfo(command.getQueueName(), command.getMember(), 0, success, 0);
	}

	public static QueueInfo createQueueInfo(RemoveMembersByScoreCommand command, boolean success) {
		return new QueueInfo(command.getQueueName(), null, 0, success, 0);
	}

	public static QueueInfo createQueueInfo(RemoveMembersByRankCommand command, boolean success) {
		return new QueueInfo(command.getQueueName(), null, 0, success, 0);
	}

	public static QueueInfo createQueueSizeInfo(String queueName, long size) {
		return new QueueInfo(queueName, null, 0, true, size);
	}
}