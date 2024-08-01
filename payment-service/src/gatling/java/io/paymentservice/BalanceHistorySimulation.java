package io.paymentservice;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.paymentservice.util.YmlLoader;

/**
 * @author : Rene Choi
 * @since : 2024/07/29
 */

public class BalanceHistorySimulation extends Simulation {

	private static final String BASE_URL = "http://localhost:" + YmlLoader.getConfigMap("server.port") + YmlLoader.ymlLoader().getContextPath();

	private final HttpProtocolBuilder httpProtocolBuilder = http
		.baseUrl(BASE_URL)
		.acceptHeader("application/json")
		.contentTypeHeader("application/json");

	private static final Duration INITIAL_DELAY = Duration.ofSeconds(10);

	{
		setUp(createScenario()
			.injectOpen(getOpenInjectionSteps()))
			.protocols(httpProtocolBuilder);
	}

	private OpenInjectionStep[] getOpenInjectionSteps() {
		return new OpenInjectionStep[] {
			rampUsersPerSec(1).to(5).during(Duration.ofSeconds(30)),
			constantUsersPerSec(5).during(Duration.ofSeconds(30)),
			rampUsersPerSec(5).to(1).during(Duration.ofSeconds(30))
		};
	}

	private ScenarioBuilder createScenario() {
		return scenario("Balance History Scenario")
			.exec(session -> {
				Map<String, Object> params = generateParams();
				return session.setAll(params);
			})
			.exec(http("잔액 내역 조회")
				.get("/api/user-balance/histories/#{userId}")
				.check(status().is(200))
			)
			.pause(2);
	}

	private Map<String, Object> generateParams() {
		Map<String, Object> params = new HashMap<>();
		int randomId = ThreadLocalRandom.current().nextInt(1, 1000001); // 1부터 1,000,000 사이의 랜덤 ID 생성
		params.put("userId", randomId);
		return params;
	}
}
