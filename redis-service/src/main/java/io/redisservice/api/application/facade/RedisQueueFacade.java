package io.redisservice.api.application.facade;

import org.springframework.stereotype.Component;

import io.redisservice.api.application.dto.request.DequeueRequest;
import io.redisservice.api.application.dto.request.EnqueueRequest;
import io.redisservice.api.application.dto.request.GetMemberRankRequest;
import io.redisservice.api.application.dto.request.GetMemberScoreRequest;
import io.redisservice.api.application.dto.request.GetQueueMembersByRankRequest;
import io.redisservice.api.application.dto.request.GetQueueMembersByScoreRequest;
import io.redisservice.api.application.dto.request.GetQueueMembersRequest;
import io.redisservice.api.application.dto.request.GetTopNMembersRequest;
import io.redisservice.api.application.dto.request.RemoveMembersByRankRequest;
import io.redisservice.api.application.dto.request.RemoveMembersByScoreRequest;
import io.redisservice.api.application.dto.response.MemberRankResponse;
import io.redisservice.api.application.dto.response.MemberScoreResponse;
import io.redisservice.api.application.dto.response.QueueMembersResponse;
import io.redisservice.api.application.dto.response.QueueResponse;
import io.redisservice.api.business.service.RedisQueueService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */

@Component
@RequiredArgsConstructor
public class RedisQueueFacade {

	private final RedisQueueService redisQueueService;

	public QueueResponse enqueue(EnqueueRequest request) {
		return QueueResponse.from(redisQueueService.enqueue(request.toCommand()));
	}

	public QueueResponse dequeue(DequeueRequest request) {
		return QueueResponse.from(redisQueueService.dequeue(request.toCommand()));
	}

	public QueueMembersResponse getQueueMembers(GetQueueMembersRequest request) {
		return QueueMembersResponse.from(redisQueueService.getQueueMembers(request.toCommand()));
	}

	public QueueMembersResponse getQueueMembersByScore(GetQueueMembersByScoreRequest request) {
		return QueueMembersResponse.from(redisQueueService.getQueueMembersByScore(request.toCommand()));
	}

	public QueueResponse removeMembersByScore(RemoveMembersByScoreRequest request) {
		return QueueResponse.from(redisQueueService.removeMembersByScore(request.toCommand()));
	}

	public MemberRankResponse getMemberRank(GetMemberRankRequest request) {
		return MemberRankResponse.from(redisQueueService.getMemberRank(request.toCommand()));
	}

	public MemberScoreResponse getMemberScore(GetMemberScoreRequest request) {
		return MemberScoreResponse.from(redisQueueService.getMemberScore(request.toCommand()));
	}

	public QueueMembersResponse getQueueMembersByRank(GetQueueMembersByRankRequest request) {
		return QueueMembersResponse.from(redisQueueService.getQueueMembersByRank(request.toCommand()));
	}

	public QueueResponse removeMembersByRank(RemoveMembersByRankRequest request) {
		return QueueResponse.from(redisQueueService.removeMembersByRank(request.toCommand()));
	}

	public QueueResponse getQueueSize(String queueName) {
		return QueueResponse.from(redisQueueService.getQueueSize(queueName));
	}

	public QueueMembersResponse getTopNMembers(GetTopNMembersRequest getTopNMembersRequest) {
		return QueueMembersResponse.from(redisQueueService.getTopNMembers(getTopNMembersRequest.toCommand()));
	}
}
