package io.apiorchestrationservice.cucumber.steps;

import static io.apiorchestrationservice.testhelpers.apiexecutor.BalanceAndPaymentApiExecutor.*;
import static io.apiorchestrationservice.testhelpers.contextholder.BalanceAndPaymentContextHolder.*;
import static io.apiorchestrationservice.testhelpers.contextholder.QueueTokenContextHolder.*;
import static io.apiorchestrationservice.testhelpers.contextholder.ReservationAndConcertContextHolder.*;
import static io.apiorchestrationservice.testhelpers.parser.BalanceAndPaymentResponseParser.*;
import static io.apiorchestrationservice.util.FieldMapper.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.apiorchestrationservice.api.application.dto.request.PaymentProcessRequest;
import io.apiorchestrationservice.api.application.dto.response.PaymentResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */

public class PaymentConcurrencyApiStepDef implements En {

	public PaymentConcurrencyApiStepDef() {
		Given("동일한 사용자가 가장 최근의 예약에 대해 다음과 같은 결제 요청을 동시에 시도한다", this::sendConcurrentPaymentRequests);
		Given("동일한 사용자가 가장 최근의 예약에 대해 다음과 같은 결제 요청을 순차적으로 시도한다", this::sendSequentialPaymentRequests);
		Then("조회한 사용자의 잔액은 {long}이어야 한다", this::verifyUserBalance);
	}

	private void sendConcurrentPaymentRequests(DataTable dataTable) {
		List<Map<String, String>> paymentRequests = dataTable.asMaps();
		ExecutorService executorService = Executors.newFixedThreadPool(paymentRequests.size());
		CountDownLatch latch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(paymentRequests.size());

		paymentRequests.forEach(paymentData -> {
			executorService.submit(() -> {
				try {
					latch.await();
					try {
						PaymentProcessRequest paymentProcessRequest = updateFields(new PaymentProcessRequest(), paymentData);
						paymentProcessRequest.setTargetId(String.valueOf(getMostRecentReservationCreateResponse().temporalReservationId()));
						ExtractableResponse<Response> response = processPaymentWithToken(paymentProcessRequest, getToken(getMostRecentUserId()));
						putPaymentResponse(parsePaymentResponse(response));
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

	private void sendSequentialPaymentRequests(DataTable dataTable) {
		List<Map<String, String>> paymentRequests = dataTable.asMaps();

		paymentRequests.forEach(paymentData -> {
			try {
				PaymentProcessRequest paymentProcessRequest = updateFields(new PaymentProcessRequest(), paymentData);
				paymentProcessRequest.setTargetId(String.valueOf(getMostRecentReservationCreateResponse().temporalReservationId()));
				Thread.sleep(Long.parseLong(paymentData.get("requestGap")));
				ExtractableResponse<Response> response = processPaymentWithToken(paymentProcessRequest, getToken(getMostRecentUserId()));
				putPaymentResponse(parsePaymentResponse(response));
			} catch (Exception ignored) {
			}
		});
	}

	@SneakyThrows
	private void awaitTermination(ExecutorService executorService, CountDownLatch doneLatch) {
		doneLatch.await(20, TimeUnit.SECONDS);
		executorService.shutdown();
		if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
			executorService.shutdownNow();
		}
	}

	private void verifyUserBalance(Long expectedBalance) {
		Long userId = getMostRecentUserId();
		Long actualBalance = getSearchResponse(userId).amount().longValue();
		assertEquals(expectedBalance, actualBalance, "잔액이 예상 값과 다릅니다.");
	}
}