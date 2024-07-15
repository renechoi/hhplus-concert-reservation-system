package io.apiorchestrationservice.cucumber.steps;

import static io.apiorchestrationservice.testhelpers.contextholder.QueueTokenContextHolder.*;

import org.springframework.beans.factory.annotation.Autowired;

import io.apiorchestrationservice.testhelpers.feignexecutor.clients.WaitingQueueFeignClientFixture;
import io.cucumber.java8.En;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public class TokenGenerateStepDef implements En {

	private WaitingQueueFeignClientFixture waitingQueueFeignClientFixture;

	@Autowired
	public TokenGenerateStepDef(WaitingQueueFeignClientFixture waitingQueueFeignClientFixture) {
		this.waitingQueueFeignClientFixture = waitingQueueFeignClientFixture;
		When("사용자 id {long}로 대기열에 인입 요청하고 토큰을 받는다", this::requestWaitingQueueAndStoreToken);

	}

	private void requestWaitingQueueAndStoreToken(long userId) {
		putToken(userId, waitingQueueFeignClientFixture.generateTokenForUser(userId));

	}
}
