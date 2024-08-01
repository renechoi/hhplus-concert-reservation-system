package io.redisservice.cucumber.steps;

import static io.redisservice.testhelpers.apiexecutor.RedisCounterApiExecutor.*;
import static io.redisservice.testhelpers.contextholder.RedisCounterContextHolder.*;
import static io.redisservice.testhelpers.fieldmatcher.ResponseMatcher.*;
import static io.redisservice.testhelpers.parser.RedisCounterResponseParser.*;
import static io.redisservice.util.FieldMapper.*;
import static org.junit.jupiter.api.Assertions.*;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.redisservice.api.application.dto.request.CounterRequest;
import io.redisservice.api.application.dto.response.CounterResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public class RedisCounterApiStepDef implements En {


	private final RedissonClient redissonClient;

	public RedisCounterApiStepDef(RedissonClient redissonClient) {

		this.redissonClient = redissonClient;


		Given("다음과 같은 Redis 카운터 {string}를 초기화한다", this::initializeCounters);

		When("다음과 같은 Redis 카운터 증가 요청을 보내고 성공 응답을 받는다", this::sendIncrementCounterRequest);
		When("다음과 같은 Redis 카운터 감소 요청을 보내고 성공 응답을 받는다", this::sendDecrementCounterRequest);

		When("다음과 같은 Redis 카운터 조회 요청을 보낸다", this::sendGetCounterRequest);

		Then("Redis 카운터 조회 응답은 다음과 같이 확인되어야 한다", this::verifyCounterResponse);
		Then("Redis 카운터 응답은 다음과 같이 확인되어야 한다", this::verifyCounterResponse);
	}


	private void initializeCounters(String counterKey) {
		RAtomicLong counter = redissonClient.getAtomicLong(counterKey);
		counter.set(0L);
	}

	private void sendIncrementCounterRequest(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			CounterRequest request = updateFields(new CounterRequest(), row);
			CounterResponse counterResponse = parseCounterResponse(increment(request));

			putCounterRequest(request.getCounterKey(), request);
			putCounterResponse(request.getCounterKey(), counterResponse);
		});
	}

	private void sendDecrementCounterRequest(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			CounterRequest request = updateFields(new CounterRequest(), row);
			CounterResponse counterResponse = parseCounterResponse(decrement(request));

			putCounterRequest(request.getCounterKey(), request);
			putCounterResponse(request.getCounterKey(), counterResponse);
		});
	}

	private void sendGetCounterRequest(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			String counterKey = row.get("counterKey");
			ExtractableResponse<Response> response = getCounter(counterKey);

			CounterResponse counterResponse = parseCounterResponse(response);

			putCounterRequest(counterKey, getCounterRequest(counterKey));
			putCounterResponse(counterKey, counterResponse);
		});
	}

	private void verifyCounterResponse(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> assertTrue(matchResponse(row, getCounterResponse(row.get("counterKey")))));
	}
}
