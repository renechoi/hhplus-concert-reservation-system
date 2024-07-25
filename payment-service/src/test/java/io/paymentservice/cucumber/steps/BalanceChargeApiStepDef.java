package io.paymentservice.cucumber.steps;

import static io.paymentservice.testhelpers.apiexecutor.BalanceApiExecutor.*;
import static io.paymentservice.testhelpers.contextholder.BalanceContextHolder.*;
import static io.paymentservice.testhelpers.parser.BalanceResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import io.cucumber.java8.En;
import io.paymentservice.api.balance.interfaces.dto.request.BalanceChargeRequest;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceChargeResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */

public class BalanceChargeApiStepDef implements En {

	public BalanceChargeApiStepDef() {
		When("사용자의 id가 {long}이고 충전 금액이 {long}인 경우 잔액을 충전 요청하고 정상 응답을 받는다", this::chargeBalanceRequestedWithSuccessResponse);
		When("사용자의 id가 {long}이고 추가 충전 금액이 {long}인 경우 잔액을 추가 충전 요청하고 정상 응답을 받는다", this::chargeAdditionalBalanceRequestedWithSuccessResponse);
		Then("사용자의 잔액은 {long}이어야 한다", this::verifyBalance);

		// 예외 및 제약 조건
		When("사용자의 id가 {long}이고 충전 금액이 {long}인 경우 잔액을 충전 요청하면 예외가 발생한다", this::chargeBalanceRequestedWithFailureResponse);
		When("{long} 잔액을 충전 요청하면 예외가 발생한다", this::chargeBalanceExceedsLimitRequestedWithFailureResponse);

		// 동시 요청
		When("사용자의 id가 {long}이고 충전 금액이 {long}인 잔액 충전 요청을 동시에 {int}번 보낸다", this::sendConcurrentBalanceChargeRequests);
	}

	private void chargeBalanceRequestedWithSuccessResponse(Long id, Long amount) {
		ExtractableResponse<Response> response = chargeBalance(id, new BalanceChargeRequest(id, BigDecimal.valueOf(amount)));
		assertEquals(200, response.statusCode());
		putChargeResponse(id, parseBalanceChargeResponse(response));
	}

	private void chargeAdditionalBalanceRequestedWithSuccessResponse(Long id, Long additionalAmount) {
		ExtractableResponse<Response> response = chargeBalance(id, new BalanceChargeRequest(id, BigDecimal.valueOf(additionalAmount)));
		assertEquals(200, response.statusCode());
		putChargeResponse(id, parseBalanceChargeResponse(response));
	}

	private void verifyBalance(Long expectedBalance) {
		BalanceChargeResponse chargeResponse = getMostRecentChargeResponse();
		assertEquals(expectedBalance, chargeResponse.amount().longValue(), "충전 후 잔액이 예상 값과 다릅니다.");
	}

	private void chargeBalanceRequestedWithFailureResponse(Long id, Long amount) {
		BalanceChargeRequest chargeRequest = new BalanceChargeRequest(id, BigDecimal.valueOf(amount));
		assertNotEquals(200, chargeBalance(id, chargeRequest).statusCode());
	}


	private void chargeBalanceExceedsLimitRequestedWithFailureResponse(Long amount) {
		Long userId = getMostRecentUserId();
		ExtractableResponse<Response> response = chargeBalance(userId,  new BalanceChargeRequest(userId, BigDecimal.valueOf(amount)));
		assertNotEquals(200, response.statusCode());
	}




	private void sendConcurrentBalanceChargeRequests(Long userId, Long amount, Integer times) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(times);
		CountDownLatch latch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(times);

		IntStream.range(0, times).forEach(i -> {
			executorService.submit(() -> {
				try {
					latch.await();
					try {
						ExtractableResponse<Response> response = chargeBalance(userId, new BalanceChargeRequest(userId, BigDecimal.valueOf(amount)));
						putChargeResponse(userId, parseBalanceChargeResponse(response));
					} catch (Exception ignored) {
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					doneLatch.countDown();
				}
			});
		});

		latch.countDown(); // 모든 스레드가 준비될 때까지 기다린 후, 동시에 시작하도록 카운트 다운
		awaitTermination(executorService, doneLatch);
	}

	@SneakyThrows
	private void awaitTermination(ExecutorService executorService, CountDownLatch doneLatch) {
		doneLatch.await(20, TimeUnit.SECONDS);
		executorService.shutdown();
		if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
			executorService.shutdownNow();
		}
	}


}