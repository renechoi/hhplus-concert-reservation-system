package io.redisservice.testhelpers.parser;

import io.redisservice.api.application.dto.response.*;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public class RedisQueueResponseParser {
	public static QueueResponse parseQueueResponse(ExtractableResponse<Response> response) {
		return response.as(QueueResponse.class);
	}

	public static QueueMembersResponse parseQueueMembersResponse(ExtractableResponse<Response> response) {
		return response.as(QueueMembersResponse.class);
	}

	public static MemberRankResponse parseMemberRankResponse(ExtractableResponse<Response> response) {
		return response.as(MemberRankResponse.class);
	}

	public static MemberScoreResponse parseMemberScoreResponse(ExtractableResponse<Response> response) {
		return response.as(MemberScoreResponse.class);
	}
}
