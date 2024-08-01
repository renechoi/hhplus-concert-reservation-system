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
public class AvailableSeatsSimulation extends Simulation {

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
		return scenario("Available Seats Scenario")
			.exec(session -> {
				int concertOptionId = ThreadLocalRandom.current().nextInt(1, 100);
				long requestAt = System.currentTimeMillis();
				return session.set("concertOptionId", concertOptionId)
					.set("requestAt", requestAt);
			})
			.exec(http("예약 가능 좌석 조회")
				.get("/api/availability/seats/#{concertOptionId}/#{requestAt}")
				.check(status().is(200))
			)
			.pause(2);
	}
}
