package io.redisservice.api.business.repository;

import java.util.Set;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public interface RedisQueueRepository {
	boolean enqueue(String queueName, String member, double score);

	boolean dequeue(String queueName, String member);

	Set<String> getQueueMembers(String queueName);

	Set<String> getQueueMembersByScore(String queueName, double minScore, double maxScore);

	boolean removeMembersByScore(String queueName, double minScore, double maxScore);

	Integer getMemberRank(String queueName, String member);

	Double getMemberScore(String queueName, String member);

	Set<String> getQueueMembersByRank(String queueName, int start, int end);

	boolean removeMembersByRank(String queueName, int start, int end);

	long getQueueSize(String queueName);

	Set<String> getTopNMembers(String queueName, int n);
}
