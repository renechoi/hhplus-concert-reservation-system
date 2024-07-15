package io.paymentservice.cucumber.steps;

import static io.paymentservice.testhelpers.apiexecutor.UserBalanceApiExecutor.*;
import static io.paymentservice.testhelpers.contextholder.UserBalanceContextHolder.*;
import static io.paymentservice.testhelpers.parser.UserBalanceResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import io.cucumber.java8.En;
import io.paymentservice.api.balance.presentation.dto.request.UserBalanceUseRequest;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public class UserBalanceUseApiStepDef implements En {

	public UserBalanceUseApiStepDef() {

		When("사용자의 id가 {long}이고 사용 금액이 {long}인 경우 잔액을 사용 요청하고 정상 응답을 받는다", this::useBalanceRequestedWithSuccessResponse);
		Then("사용자의 남은 잔액는 {long}이어야 한다", this::verifyUserBalance);
	}

	private void useBalanceRequestedWithSuccessResponse(Long userId, Long useAmount) {
		putUseResponse(parseUserBalanceUseResponse(useUserBalanceWithOk(userId, new UserBalanceUseRequest(userId, BigDecimal.valueOf(useAmount)))));
	}

	private void verifyUserBalance(Long expectedBalance) {
		assertEquals(expectedBalance, getMostRecentUseResponse().amount().longValue(), "사용 후 잔액이 예상 값과 다릅니다.");
	}

}
