package io.redisservice.testhelpers.parser;

import io.redisservice.api.application.dto.response.LockResponse;
import io.redisservice.api.application.dto.response.UnLockResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */

public class RedisLockResponseParser {
	public static LockResponse parseLockResponse(ExtractableResponse<Response> response) {
		return response.as(LockResponse.class);
	}

	public static UnLockResponse parseUnLockResponse(ExtractableResponse<Response> response) {
		return response.as(UnLockResponse.class);
	}
}

