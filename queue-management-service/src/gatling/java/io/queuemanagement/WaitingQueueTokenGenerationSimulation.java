package io.queuemanagement;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.queuemanagement.util.YmlLoader;

public class WaitingQueueTokenGenerationSimulation extends Simulation {

	private static final String BASE_URL = "http://localhost:" + YmlLoader.getConfigMap("server.port") + YmlLoader.ymlLoader().getContextPath();
	private final HttpProtocolBuilder httpProtocolBuilder = http
		.baseUrl(BASE_URL)
		.acceptHeader("application/json")
		.contentTypeHeader("application/json");

	private static final AtomicInteger userIdCounter = new AtomicInteger(1);
	private static final int MAX_USER_ID = 10000 * 100;

	{
		setUp(createScenario()
			.injectOpen(getOpenInjectionSteps())
			.protocols(httpProtocolBuilder))
			.maxDuration(Duration.ofMinutes(1))
			.assertions(global().requestsPerSec().gte(1.0));
	}

	private OpenInjectionStep[] getOpenInjectionSteps() {
		return new OpenInjectionStep[]{
			rampUsersPerSec(50).to(150).during(Duration.ofSeconds(20)),
			constantUsersPerSec(150).during(Duration.ofSeconds(20)),
			rampUsersPerSec(150).to(1).during(Duration.ofSeconds(20))
		};
	}

	private ScenarioBuilder createScenario() {
		return scenario("Waiting Queue Token Generation Scenario")
			.asLongAs(session -> userIdCounter.get() <= MAX_USER_ID)
			.on(exec(session -> {
					int userId = userIdCounter.getAndIncrement();
					return session.set("userId", "user" + userId);
				})
					.exec(http("대기열 토큰 생성")
						.post("/api/waiting-queue-token")
						.body(StringBody(session -> {
							LocalDateTime now = LocalDateTime.now();
							return String.format("{ \"userId\": \"%s\", \"requestAt\": \"%s\" }", session.getString("userId"), now.toString());
						}))
						.check(status().is(201))
					)
					.pause(2)
			);
	}
}
