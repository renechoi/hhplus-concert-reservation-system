package io.redisservice.api.infrastructure.repository;

import java.util.HashSet;
import java.util.Set;

import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import io.redisservice.api.business.repository.RedisQueueRepository;
import io.redisservice.common.exception.definitions.QueueItemNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */

@Component
@RequiredArgsConstructor
public class RedisQueueCoreRepository implements RedisQueueRepository {

	private final RedissonClient redissonClient;

	@Override
	public boolean enqueue(String queueName, String member, double score) {
		RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(queueName);
		return sortedSet.add(score, member);
	}

	@Override
	public boolean dequeue(String queueName, String member) {
		RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(queueName);
		return sortedSet.remove(member);
	}

	@Override
	public Set<String> getQueueMembers(String queueName) {
		RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(queueName);
		return new HashSet<>(sortedSet.readAll());
	}

	@Override
	public Set<String> getQueueMembersByScore(String queueName, double minScore, double maxScore) {
		RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(queueName);
		return new HashSet<>(sortedSet.valueRange(minScore, true, maxScore, true));

	}

	@Override
	public boolean removeMembersByScore(String queueName, double minScore, double maxScore) {
		RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(queueName);
		return sortedSet.removeRangeByScore(minScore, true, maxScore, true) > 0;
	}

	@Override
	public Integer getMemberRank(String queueName, String member) {
		RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(queueName);
		Integer rank = sortedSet.rank(member);
		if (rank == null) {
			throw new QueueItemNotFoundException();
		}
		return rank;
	}

	@Override
	public Double getMemberScore(String queueName, String member) {
		RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(queueName);
		return sortedSet.getScore(member);
	}

	@Override
	public Set<String> getQueueMembersByRank(String queueName, int start, int end) {
		RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(queueName);
		return new HashSet<>(sortedSet.valueRange(start, end));
	}

	@Override
	public boolean removeMembersByRank(String queueName, int start, int end) {
		RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(queueName);
		return sortedSet.removeRangeByRank(start, end) > 0;
	}

	@Override
	public long getQueueSize(String queueName) {
		RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(queueName);
		return sortedSet.size();
	}

	@Override
	public Set<String> getTopNMembers(String queueName, int n) {
		RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(queueName);
		return new HashSet<>(sortedSet.valueRange(0, n - 1));
	}
}
