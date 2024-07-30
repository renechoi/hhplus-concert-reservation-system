package io.paymentservice.cucumber.steps;

import static io.paymentservice.api.payment.business.entity.PaymentStatus.*;
import static io.paymentservice.testhelpers.apiexecutor.PaymentApiExecutor.*;
import static io.paymentservice.testhelpers.contextholder.PaymentContextHolder.*;
import static io.paymentservice.testhelpers.parser.PaymentResponseParser.*;
import static io.paymentservice.util.FieldMapper.*;
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
import io.paymentservice.api.payment.interfaces.dto.request.PaymentRequest;
import io.paymentservice.api.payment.interfaces.dto.response.PaymentResponse;
import io.paymentservice.testhelpers.apiexecutor.PaymentApiExecutor;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public class PaymentProcessConcurrencyStepDef implements En {

	public PaymentProcessConcurrencyStepDef() {
		Given("동일한 사용자가 다음과 같은 결제 요청을 동시에 시도한다", this::sendConcurrentPaymentRequests);
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
					try{
						ExtractableResponse<Response> response = processPayment(updateFields(new PaymentRequest(), paymentData));
						putPaymentResponse(parsePaymentResponse(response));
					} catch (Exception ignored){}
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
