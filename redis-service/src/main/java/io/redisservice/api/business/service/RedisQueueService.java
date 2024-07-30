package io.redisservice.api.business.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import io.redisservice.api.business.dto.command.DequeueCommand;
import io.redisservice.api.business.dto.command.EnqueueCommand;
import io.redisservice.api.business.dto.command.GetMemberRankCommand;
import io.redisservice.api.business.dto.command.GetMemberScoreCommand;
import io.redisservice.api.business.dto.command.GetQueueMembersByRankCommand;
import io.redisservice.api.business.dto.command.GetQueueMembersByScoreCommand;
import io.redisservice.api.business.dto.command.GetQueueMembersCommand;
import io.redisservice.api.business.dto.command.GetTopNMembersCommand;
import io.redisservice.api.business.dto.command.RemoveMembersByRankCommand;
import io.redisservice.api.business.dto.command.RemoveMembersByScoreCommand;
import io.redisservice.api.business.dto.info.MemberRankInfo;
import io.redisservice.api.business.dto.info.MemberScoreInfo;
import io.redisservice.api.business.dto.info.QueueInfo;
import io.redisservice.api.business.dto.info.QueueMembersInfo;
import io.redisservice.api.business.repository.RedisQueueRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Service
@RequiredArgsConstructor
public class RedisQueueService {

	private final RedisQueueRepository redisQueueRepository;


	public QueueInfo enqueue(EnqueueCommand command) {
		boolean enqueued = redisQueueRepository.enqueue(command.getQueueName(), command.getMember(), command.getScore());
		return QueueInfo.createQueueInfo(command, enqueued);
	}

	public QueueInfo dequeue(DequeueCommand command) {
		boolean dequeued = redisQueueRepository.dequeue(command.getQueueName(), command.getMember());
		return QueueInfo.createQueueInfo(command, dequeued);
	}


	public QueueMembersInfo getQueueMembers(GetQueueMembersCommand command) {
		Set<String> members = redisQueueRepository.getQueueMembers(command.getQueueName());
		return QueueMembersInfo.createQueueMembersInfo(command.getQueueName(), members);
	}

	public QueueMembersInfo getQueueMembersByScore(GetQueueMembersByScoreCommand command) {
		Set<String> members = redisQueueRepository.getQueueMembersByScore(command.getQueueName(), command.getMinScore(), command.getMaxScore());
		return QueueMembersInfo.createQueueMembersInfo(command.getQueueName(), members);
	}

	public QueueInfo removeMembersByScore(RemoveMembersByScoreCommand command) {
		boolean removed = redisQueueRepository.removeMembersByScore(command.getQueueName(), command.getMinScore(), command.getMaxScore());
		return QueueInfo.createQueueInfo(command, removed);
	}

	public MemberRankInfo getMemberRank(GetMemberRankCommand command) {
		Integer rank = redisQueueRepository.getMemberRank(command.getQueueName(), command.getMember());
		return MemberRankInfo.createMemberRankInfo(command.getQueueName(), command.getMember(), rank);
	}

	public MemberScoreInfo getMemberScore(GetMemberScoreCommand command) {
		Double score = redisQueueRepository.getMemberScore(command.getQueueName(), command.getMember());
		return MemberScoreInfo.createMemberScoreInfo(command.getQueueName(), command.getMember(), score);
	}

	public QueueMembersInfo getQueueMembersByRank(GetQueueMembersByRankCommand command) {
		Set<String> members = redisQueueRepository.getQueueMembersByRank(command.getQueueName(), command.getStart(), command.getEnd());
		return QueueMembersInfo.createQueueMembersInfo(command.getQueueName(), members);
	}

	public QueueInfo removeMembersByRank(RemoveMembersByRankCommand command) {
		boolean removed = redisQueueRepository.removeMembersByRank(command.getQueueName(), command.getStart(), command.getEnd());
		return QueueInfo.createQueueInfo(command, removed);
	}

	public QueueInfo getQueueSize(String queueName) {
		long size = redisQueueRepository.getQueueSize(queueName);
		return QueueInfo.createQueueSizeInfo(queueName, size);
	}

	public QueueMembersInfo getTopNMembers(GetTopNMembersCommand command) {
		Set<String> members = redisQueueRepository.getTopNMembers(command.getQueueName(), command.getN());
		return QueueMembersInfo.createQueueMembersInfo(command.getQueueName(), members);
	}


}