package io.redisservice.cucumber.steps;

import static io.redisservice.testhelpers.apiexecutor.RedisLockApiExecutor.*;
import static io.redisservice.testhelpers.contextholder.RedisLockContextHolder.*;
import static io.redisservice.testhelpers.fieldmatcher.ResponseMatcher.*;
import static io.redisservice.testhelpers.parser.RedisLockResponseParser.*;
import static io.redisservice.util.FieldMapper.*;
import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.redisservice.api.application.dto.LockRequest;
import io.redisservice.api.application.dto.LockResponse;
import io.redisservice.api.application.dto.UnLockRequest;
import io.redisservice.api.application.dto.UnLockResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
public class RedisLockApiStepDef implements En {

	public RedisLockApiStepDef() {

		Given("다음과 같은 Redis 락 요청을 보내고 성공 응답을 받는다", this::sendLockRequest);
		Then("Redis 락 응답은 다음과 같이 확인되어야 한다", this::verifyLockResponse);

		When("다음과 같은 Redis 락 해제 요청을 보내고 성공 응답을 받는다", this::sendUnlockRequest);
		Then("Redis 락 해제 응답은 다음과 같이 확인되어야 한다", this::verifyUnlockResponse);

		When("동일한 key로 lock을 획득하려고 할 때 실패해야함", this::attemptToLockWithSameKeyShouldFail);
		Then("동일한 key로 lock을 획득하려고 할 때 성공해야함", this::attemptToLockWithSameKeyShouldSucceed);
	}

	private void sendLockRequest(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			LockRequest request = updateFields(new LockRequest(), row);
			LockResponse lockResponse = parseLockResponse(lockWithOk(request));

			putLockRequest(request.getLockKey(), request);
			putLockResponse(request.getLockKey(), lockResponse);
		});
	}

	private void sendUnlockRequest(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			UnLockRequest request = updateFields( new UnLockRequest(), row);
			UnLockResponse unlockResponse = parseUnLockResponse(unlockWithOk(request));

			putUnLockRequest(request.getLockKey(), request);
			putUnLockResponse(request.getLockKey(), unlockResponse);
		});
	}

	private void verifyLockResponse(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> assertTrue(matchResponse(row, getLockResponse(row.get("lockKey")))));
	}

	private void verifyUnlockResponse(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> assertTrue(matchResponse(row, getUnLockResponse(row.get("lockKey")))));
	}

	private void attemptToLockWithSameKeyShouldFail(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> assertFalse(parseLockResponse(lockWithOk(updateFields(new LockRequest(), row))).isLocked()));
	}

	private void attemptToLockWithSameKeyShouldSucceed(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			LockRequest request = updateFields(new LockRequest(), row);
			putLockRequest(request.getLockKey(), request);

			putLockResponse(request.getLockKey(), parseLockResponse(lockWithOk(request)));
		});
	}
}