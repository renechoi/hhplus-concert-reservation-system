package io.redisservice.api.business.dto.info;

import java.util.Set;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public record QueueMembersInfo(String queueName, Set<String> members) {
	public static QueueMembersInfo createQueueMembersInfo(String queueName, Set<String> members) {
		return new QueueMembersInfo(queueName, members);
	}
}
