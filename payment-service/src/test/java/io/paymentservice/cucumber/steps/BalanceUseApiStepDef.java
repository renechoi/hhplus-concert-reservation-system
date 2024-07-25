package io.paymentservice.cucumber.steps;

import static io.paymentservice.testhelpers.apiexecutor.BalanceApiExecutor.*;
import static io.paymentservice.testhelpers.contextholder.BalanceContextHolder.*;
import static io.paymentservice.testhelpers.parser.BalanceResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import io.cucumber.java8.En;
import io.paymentservice.api.balance.interfaces.dto.request.BalanceUseRequest;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public class BalanceUseApiStepDef implements En {

	public BalanceUseApiStepDef() {

		When("사용자의 id가 {long}이고 사용 금액이 {long}인 경우 잔액을 사용 요청하고 정상 응답을 받는다", this::useBalanceRequestedWithSuccessResponse);
		Then("사용자의 남은 잔액는 {long}이어야 한다", this::verifyBalance);

		When("사용자의 id가 {long}이고 사용 금액이 {long}인 잔액 사용 요청을 동시에 {int}번 보낸다", this::sendConcurrentBalanceUseRequests);
	}

	private void useBalanceRequestedWithSuccessResponse(Long userId, Long useAmount) {
		putUseResponse(parseBalanceUseResponse(useBalanceWithOk(userId, new BalanceUseRequest(userId, BigDecimal.valueOf(useAmount)))));
	}

	private void verifyBalance(Long expectedBalance) {
		assertEquals(expectedBalance, getMostRecentUseResponse().amount().longValue(), "사용 후 잔액이 예상 값과 다릅니다.");
	}



	private void sendConcurrentBalanceUseRequests(Long userId, Long useAmount, Integer times) throws ExecutionException, InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(times);
		CountDownLatch latch = new CountDownLatch(times);

		IntStream.range(0, times).forEach(i -> {
			executorService.submit(() -> {
				try {
					latch.await();
					useBalanceRequestedWithSuccessResponse(userId, useAmount);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			});
			latch.countDown();
		});

		executorService.shutdown();
		awaitTermination(executorService, 20, TimeUnit.SECONDS);
	}


	@SneakyThrows
	private void awaitTermination(ExecutorService executorService, long timeout, TimeUnit unit) {
		if (!executorService.awaitTermination(timeout, unit)) {
			executorService.shutdownNow();
		}
	}

}
