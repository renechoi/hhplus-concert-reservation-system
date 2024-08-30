package io.reservationservice;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.reservationservice.util.YmlLoader;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public class AvailableDatesSimulation extends Simulation {

	private static final String BASE_URL = "http://localhost:" + YmlLoader.getConfigMap("server.port") + YmlLoader.ymlLoader().getContextPath();

	private final HttpProtocolBuilder httpProtocolBuilder = http
		.baseUrl(BASE_URL)
		.acceptHeader("application/json")
		.contentTypeHeader("application/json");

	private static final Duration INITIAL_DELAY = Duration.ofSeconds(10);

	{
		setUp(createScenario()
			.injectOpen(getOpenInjectionSteps()))
			.protocols(httpProtocolBuilder)
			.maxDuration(Duration.ofMinutes(1))
			.assertions(global().requestsPerSec().gte(50.0));
	}

	private OpenInjectionStep[] getOpenInjectionSteps() {
		return new OpenInjectionStep[] {
			rampUsersPerSec(20).to(80).during(Duration.ofSeconds(30)),
			constantUsersPerSec(80).during(Duration.ofSeconds(30)),
			rampUsersPerSec(80).to(20).during(Duration.ofSeconds(30))
		};
	}

	private ScenarioBuilder createScenario() {
		return scenario("Available Dates Scenario")
			.exec(session -> {
				int concertId = ThreadLocalRandom.current().nextInt(1, 100);
				return session.set("concertId", concertId);
			})
			.exec(http("예약 가능 날짜 조회")
				.get("/api/availability/dates/#{concertId}")
				.check(status().is(200))
			)
			.pause(2);
	}
}
