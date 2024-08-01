package io.redisservice.testhelpers.apiexecutor;

import static io.redisservice.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.redisservice.api.application.dto.request.*;
import io.redisservice.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public class RedisQueueApiExecutor extends AbstractRequestExecutor {

	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();

	private static final String REDIS_SERVICE_API_URL_PATH = CONTEXT_PATH + "/api/redis-queue";
	private static final String ENQUEUE_API_URL_PATH = REDIS_SERVICE_API_URL_PATH + "/enqueue";
	private static final String DEQUEUE_API_URL_PATH = REDIS_SERVICE_API_URL_PATH + "/dequeue";
	private static final String MEMBERS_API_URL_PATH = REDIS_SERVICE_API_URL_PATH + "/members";
	private static final String MEMBERS_SCORE_API_URL_PATH = MEMBERS_API_URL_PATH + "/score";
	private static final String MEMBERS_RANK_API_URL_PATH = MEMBERS_API_URL_PATH + "/rank";
	private static final String RANK_API_URL_PATH = REDIS_SERVICE_API_URL_PATH + "/rank";
	private static final String SCORE_API_URL_PATH = REDIS_SERVICE_API_URL_PATH + "/score";

	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> enqueue(EnqueueRequest request) {
		return doPost(getRequestSpecification(getPort()), ENQUEUE_API_URL_PATH, request);
	}

	public static ExtractableResponse<Response> dequeue(DequeueRequest request) {
		return doPost(getRequestSpecification(getPort()), DEQUEUE_API_URL_PATH, request);
	}

	public static ExtractableResponse<Response> getQueueMembers(String queueName) {
		return doGet(getRequestSpecification(getPort()), MEMBERS_API_URL_PATH + "/" + queueName);
	}

	public static ExtractableResponse<Response> getQueueMembersByScore(GetQueueMembersByScoreRequest request) {
		return doPost(getRequestSpecification(getPort()), MEMBERS_SCORE_API_URL_PATH, request);
	}

	public static ExtractableResponse<Response> removeMembersByScore(RemoveMembersByScoreRequest request) {
		return doDelete(getRequestSpecification(getPort()), MEMBERS_SCORE_API_URL_PATH, request);
	}

	public static ExtractableResponse<Response> getMemberRank(String queueName, String member) {
		return doGet(getRequestSpecification(getPort()), RANK_API_URL_PATH + "/" + queueName + "/" + member);
	}

	public static ExtractableResponse<Response> getMemberScore(String queueName, String member) {
		return doGet(getRequestSpecification(getPort()), SCORE_API_URL_PATH + "/" + queueName + "/" + member);
	}

	public static ExtractableResponse<Response> getQueueMembersByRank(GetQueueMembersByRankRequest request) {
		return doPost(getRequestSpecification(getPort()), MEMBERS_RANK_API_URL_PATH, request);
	}

	public static ExtractableResponse<Response> removeMembersByRank(RemoveMembersByRankRequest request) {
		return doDelete(getRequestSpecification(getPort()), MEMBERS_RANK_API_URL_PATH, request);
	}
}
