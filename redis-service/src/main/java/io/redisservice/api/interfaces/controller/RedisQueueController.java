package io.redisservice.api.interfaces.controller;

import static io.redisservice.common.model.CommonApiResponse.*;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import io.redisservice.api.application.facade.RedisQueueFacade;
import io.redisservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@RestController
@RequestMapping("/api/redis-queue")
@RequiredArgsConstructor
public class RedisQueueController {

	private final RedisQueueFacade redisQueueFacade;

	@PostMapping("/enqueue")
	@Operation(summary = "큐에 항목 추가")
	public CommonApiResponse<QueueResponse> enqueue(@RequestBody @Validated EnqueueRequest request) {
		return OK(redisQueueFacade.enqueue(request));
	}

	@PostMapping("/dequeue")
	@Operation(summary = "큐에서 항목 제거")
	public CommonApiResponse<QueueResponse> dequeue(@RequestBody @Validated DequeueRequest request) {
		return OK(redisQueueFacade.dequeue(request));
	}

	@GetMapping(value = "/members/{queueName}")
	@Operation(summary = "큐 멤버 조회")
	public CommonApiResponse<QueueMembersResponse> getQueueMembers(@PathVariable String queueName) {
		GetQueueMembersRequest request = new GetQueueMembersRequest(queueName);
		return OK(redisQueueFacade.getQueueMembers(request));
	}

	@PostMapping(value = "/members/score")
	@Operation(summary = "점수 범위로 큐 멤버 조회")
	public CommonApiResponse<QueueMembersResponse> getQueueMembersByScore(@RequestBody @Validated GetQueueMembersByScoreRequest request) {
		return OK(redisQueueFacade.getQueueMembersByScore(request));
	}

	@DeleteMapping("/members/score")
	@Operation(summary = "점수 범위로 큐 멤버 제거")
	public CommonApiResponse<QueueResponse> removeMembersByScore(@RequestBody @Validated RemoveMembersByScoreRequest request) {
		return OK(redisQueueFacade.removeMembersByScore(request));
	}

	@GetMapping(value = "/rank/{queueName}/{member}")
	@Operation(summary = "큐 멤버의 순위 조회")
	public CommonApiResponse<MemberRankResponse> getMemberRank(@PathVariable String queueName, @PathVariable String member) {
		GetMemberRankRequest request = new GetMemberRankRequest(queueName, member);
		return OK(redisQueueFacade.getMemberRank(request));
	}

	@GetMapping(value = "/score/{queueName}/{member}")
	@Operation(summary = "큐 멤버의 점수 조회")
	public CommonApiResponse<MemberScoreResponse> getMemberScore(@PathVariable String queueName, @PathVariable String member) {
		GetMemberScoreRequest request = new GetMemberScoreRequest(queueName, member);
		return OK(redisQueueFacade.getMemberScore(request));
	}

	@PostMapping(value = "/members/rank")
	@Operation(summary = "순위 범위로 큐 멤버 조회")
	public CommonApiResponse<QueueMembersResponse> getQueueMembersByRank(@RequestBody @Validated GetQueueMembersByRankRequest request) {
		return OK(redisQueueFacade.getQueueMembersByRank(request));
	}

	@DeleteMapping("/members/rank")
	@Operation(summary = "순위 범위로 큐 멤버 제거")
	public CommonApiResponse<QueueResponse> removeMembersByRank(@RequestBody @Validated RemoveMembersByRankRequest request) {
		return OK(redisQueueFacade.removeMembersByRank(request));
	}

	@GetMapping(value = "/size/{queueName}")
	@Operation(summary = "큐의 현재 크기 조회")
	public CommonApiResponse<QueueResponse> getQueueSize(@PathVariable String queueName) {
		return CommonApiResponse.OK(redisQueueFacade.getQueueSize(queueName));
	}

	@GetMapping(value = "/top/{queueName}/{n}")
	@Operation(summary = "큐의 상위 N명 조회")
	public CommonApiResponse<QueueMembersResponse> getTopNMembers(@PathVariable String queueName, @PathVariable int n) {
		return OK(redisQueueFacade.getTopNMembers(new GetTopNMembersRequest(queueName, n)));
	}
}
