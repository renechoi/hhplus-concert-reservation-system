package io.redisservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.redisservice.api.application.dto.request.*;
import io.redisservice.api.application.dto.response.*;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public class RedisQueueContextHolder implements TestDtoContextHolder {
	private static final ConcurrentHashMap<String, QueueResponse> queueResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, QueueMembersResponse> queueMembersResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, MemberRankResponse> memberRankResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, MemberScoreResponse> memberScoreResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<String> mostRecentQueueName = new AtomicReference<>();
	private static final AtomicReference<String> mostRecentMember = new AtomicReference<>();

	public static void initFields() {
		queueResponseMap.clear();
		queueMembersResponseMap.clear();
		memberRankResponseMap.clear();
		memberScoreResponseMap.clear();
		mostRecentQueueName.set(null);
		mostRecentMember.set(null);
	}

	public static void putQueueResponse(String queueName, QueueResponse response) {
		queueResponseMap.put(queueName, response);
		mostRecentQueueName.set(queueName);
	}

	public static QueueResponse getQueueResponse(String queueName) {
		return queueResponseMap.get(queueName);
	}

	public static void putQueueMembersResponse(String queueName, QueueMembersResponse response) {
		queueMembersResponseMap.put(queueName, response);
		mostRecentQueueName.set(queueName);
	}

	public static QueueMembersResponse getQueueMembersResponse(String queueName) {
		return queueMembersResponseMap.get(queueName);
	}

	public static void putMemberRankResponse(String queueName, String member, MemberRankResponse response) {
		memberRankResponseMap.put(queueName + member, response);
		mostRecentQueueName.set(queueName);
		mostRecentMember.set(member);
	}

	public static MemberRankResponse getMemberRankResponse(String queueName, String member) {
		return memberRankResponseMap.get(queueName + member);
	}

	public static void putMemberScoreResponse(String queueName, String member, MemberScoreResponse response) {
		memberScoreResponseMap.put(queueName + member, response);
		mostRecentQueueName.set(queueName);
		mostRecentMember.set(member);
	}

	public static MemberScoreResponse getMemberScoreResponse(String queueName, String member) {
		return memberScoreResponseMap.get(queueName + member);
	}

	public static String getMostRecentQueueName() {
		return mostRecentQueueName.get();
	}

	public static String getMostRecentMember() {
		return mostRecentMember.get();
	}

	public static QueueResponse getMostRecentQueueResponse() {
		String recentQueueName = mostRecentQueueName.get();
		return recentQueueName != null ? getQueueResponse(recentQueueName) : null;
	}

	public static QueueMembersResponse getMostRecentQueueMembersResponse() {
		String recentQueueName = mostRecentQueueName.get();
		return recentQueueName != null ? getQueueMembersResponse(recentQueueName) : null;
	}

	public static MemberRankResponse getMostRecentMemberRankResponse() {
		String recentQueueName = mostRecentQueueName.get();
		String recentMember = mostRecentMember.get();
		return recentQueueName != null && recentMember != null ? getMemberRankResponse(recentQueueName, recentMember) : null;
	}

	public static MemberScoreResponse getMostRecentMemberScoreResponse() {
		String recentQueueName = mostRecentQueueName.get();
		String recentMember = mostRecentMember.get();
		return recentQueueName != null && recentMember != null ? getMemberScoreResponse(recentQueueName, recentMember) : null;
	}
}
