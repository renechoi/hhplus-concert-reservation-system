package io.paymentservice.cucumber.steps;

import static io.paymentservice.testhelpers.apiexecutor.BalanceApiExecutor.*;
import static io.paymentservice.testhelpers.contextholder.BalanceContextHolder.*;
import static io.paymentservice.testhelpers.fieldmatcher.ResponseMatcher.*;
import static io.paymentservice.testhelpers.parser.BalanceResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceTransactionResponses;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */

public class BalanceHistoryApiStepDef implements En {

	public BalanceHistoryApiStepDef() {
		When("사용자의 id가 {long}인 경우 잔액 이력을 조회 요청하고 정상 응답을 받는다", this::historyBalanceRequestedWithSuccessResponse);
		Then("조회한 사용자의 잔액 이력은 다음과 같이 확인되어야 한다", this::verifyHistoryContains);
		Then("조회한 사용자의 잔액 이력은 비어있어야 한다", this::verifyHistoryIsEmpty);
	}

	private void historyBalanceRequestedWithSuccessResponse(Long id) {
		BalanceTransactionResponses response = parseBalanceTransactionResponses(getBalanceHistories(id));
		putTransactionResponses(id, response);
	}

	private void verifyHistoryContains(DataTable dataTable) {
		BalanceTransactionResponses historiesResponse = getTransactionResponses(getMostRecentUserId());
		List<Map<String, String>> expectedHistories = dataTable.asMaps(String.class, String.class);

		for (Map<String, String> expectedHistory : expectedHistories) {
			boolean matchFound = historiesResponse.balanceTransactionResponses().stream().anyMatch(actualHistory -> matchResponse(expectedHistory, actualHistory));
			assertTrue(matchFound, "기대한 이력이 발견되지 않았습니다: " + expectedHistory);
		}
	}

	@SneakyThrows
	private Field getField(Class<?> clazz, String fieldName) {
		return clazz.getDeclaredField(fieldName);
	}

	private void verifyHistoryIsEmpty() {
		BalanceTransactionResponses historiesResponse = getTransactionResponses(getMostRecentUserId());
		assertTrue(historiesResponse.balanceTransactionResponses().isEmpty(), "조회한 이력이 비어 있지 않습니다.");
	}
}