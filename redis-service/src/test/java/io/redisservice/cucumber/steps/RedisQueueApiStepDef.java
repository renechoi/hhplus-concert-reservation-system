package io.redisservice.cucumber.steps;


import static io.redisservice.testhelpers.apiexecutor.RedisQueueApiExecutor.*;
import static io.redisservice.testhelpers.contextholder.RedisQueueContextHolder.*;
import static io.redisservice.testhelpers.parser.RedisQueueResponseParser.*;

import java.util.Arrays;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.redisservice.api.application.dto.request.EnqueueRequest;
import io.redisservice.api.application.dto.request.GetMemberRankRequest;
import io.redisservice.api.application.dto.request.GetQueueMembersRequest;
import io.redisservice.api.application.dto.response.QueueResponse;
import io.redisservice.api.application.dto.response.QueueMembersResponse;
import io.redisservice.api.application.dto.response.MemberRankResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public class RedisQueueApiStepDef implements En {

	public RedisQueueApiStepDef() {

		When("다음과 같은 Redis 큐 요청을 보내고 성공 응답을 받는다", this::sendQueueRequest);
		Then("Redis 큐 응답은 다음과 같이 확인되어야 한다", this::verifyQueueResponse);

		When("다음과 같은 Redis 큐 멤버 조회 요청을 보낸다", this::sendGetQueueMembersRequest);
		Then("Redis 큐 멤버 조회 응답은 다음과 같이 확인되어야 한다", this::verifyGetQueueMembersResponse);

		When("다음과 같은 Redis 큐 멤버 순위 조회 요청을 보낸다", this::sendGetMemberRankRequest);
		Then("Redis 큐 멤버 순위 조회 응답은 다음과 같이 확인되어야 한다", this::verifyMemberRankResponse);

	}

	private void sendQueueRequest(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			EnqueueRequest request = EnqueueRequest.builder()
				.queueName(row.get("queueName"))
				.member(row.get("member"))
				.score(Double.parseDouble(row.get("score")))
				.build();
			ExtractableResponse<Response> response = enqueue(request);
			QueueResponse queueResponse = parseQueueResponse(response);
			putQueueResponse(request.getQueueName(), queueResponse);
		});
	}

	private void sendGetQueueMembersRequest(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			GetQueueMembersRequest request = new GetQueueMembersRequest(row.get("queueName"));
			ExtractableResponse<Response> response = getQueueMembers(request.getQueueName());
			QueueMembersResponse queueMembersResponse = parseQueueMembersResponse(response);
			putQueueMembersResponse(request.getQueueName(), queueMembersResponse);
		});
	}

	private void sendGetMemberRankRequest(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			GetMemberRankRequest request = new GetMemberRankRequest(row.get("queueName"), row.get("member"));
			ExtractableResponse<Response> response = getMemberRank(request.getQueueName(), request.getMember());
			MemberRankResponse memberRankResponse = parseMemberRankResponse(response);
			putMemberRankResponse(request.getQueueName(), request.getMember(), memberRankResponse);
		});
	}

	private void verifyQueueResponse(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			QueueResponse queueResponse = getQueueResponse(row.get("queueName"));
			assert queueResponse != null;
			assert queueResponse.getQueueName().equals(row.get("queueName"));
			assert queueResponse.getMember().equals(row.get("member"));
			assert queueResponse.getScore() == Double.parseDouble(row.get("score"));
			assert queueResponse.isSuccess() == Boolean.parseBoolean(row.get("success"));
		});
	}

	private void verifyGetQueueMembersResponse(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			QueueMembersResponse queueMembersResponse = getQueueMembersResponse(row.get("queueName"));
			assert queueMembersResponse != null;
			assert queueMembersResponse.getQueueName().equals(row.get("queueName"));
			String[] expectedMembers = row.get("members").replace("[", "").replace("]", "").split(", ");
			assert queueMembersResponse.getMembers().containsAll(Arrays.asList(expectedMembers));
		});
	}

	private void verifyMemberRankResponse(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			MemberRankResponse memberRankResponse = getMemberRankResponse(row.get("queueName"), row.get("member"));
			assert memberRankResponse != null;
			assert memberRankResponse.getQueueName().equals(row.get("queueName"));
			assert memberRankResponse.getMember().equals(row.get("member"));
			assert memberRankResponse.getRank() == Integer.parseInt(row.get("rank"));
		});
	}
}
