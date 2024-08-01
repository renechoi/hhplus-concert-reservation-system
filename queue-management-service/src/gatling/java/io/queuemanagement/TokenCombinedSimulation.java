package io.queuemanagement;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.queuemanagement.util.YmlLoader;

/**
 * @author : Rene Choi
 * @since : 2024/08/01
 */
public class TokenCombinedSimulation extends Simulation {

    private static final String BASE_URL = "http://localhost:" + YmlLoader.getConfigMap("server.port") + YmlLoader.ymlLoader().getContextPath();

    private final HttpProtocolBuilder httpProtocolBuilder = http
        .baseUrl(BASE_URL)
        .acceptHeader("application/json")
        .contentTypeHeader("application/json");

    private static final Duration INITIAL_DELAY = Duration.ofSeconds(10);

    // 1만 개의 userId 미리 생성
    private static final List<String> userIds = IntStream.rangeClosed(1, 10000)
        .mapToObj(i -> "user" + i)
        .collect(Collectors.toList());

    private ScenarioBuilder createTokenGenerationScenario() {
        return scenario("Waiting Queue Token Generation Scenario")
            .foreach(userIds, "userId").on(
                exec(http("대기열 토큰 생성")
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

    private ScenarioBuilder createTokenPollingScenario() {
        return scenario("Waiting Queue Token Polling Scenario")
            .foreach(userIds, "userId").on(
                exec(http("대기열 토큰 정보 조회")
                    .get("/api/waiting-queue-token/#{userId}")
                    .check(status().is(200))
                )
                    .pause(Duration.ofSeconds(5))
                    .repeat(5).on(exec(http("대기열 토큰 정보 재조회")
                        .get("/api/waiting-queue-token/#{userId}")
                        .check(status().is(200))
                    ).pause(Duration.ofSeconds(5)))
            );
    }

    {
        setUp(
            createTokenGenerationScenario()
                .injectOpen(new OpenInjectionStep[]{
                    rampUsersPerSec(20).to(100).during(Duration.ofSeconds(20)),
                    constantUsersPerSec(100).during(Duration.ofSeconds(20)),
                    rampUsersPerSec(100).to(1).during(Duration.ofSeconds(20))
                }).protocols(httpProtocolBuilder),

            createTokenPollingScenario()
                .injectOpen(new OpenInjectionStep[]{
                    nothingFor(Duration.ofSeconds(30)), // 30초 지연
                    rampUsersPerSec(1).to(50).during(Duration.ofSeconds(30)),
                    constantUsersPerSec(50).during(Duration.ofSeconds(30)),
                    rampUsersPerSec(50).to(1).during(Duration.ofSeconds(30))
                }).protocols(httpProtocolBuilder)
        );
    }
}
