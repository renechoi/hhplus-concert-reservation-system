package io.clientchannelservice.api;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import io.clientchannelservice.testhelpers.apiexecutor.DynamicPortHolder;
import io.clientchannelservice.testutils.WireMockHelper;
import io.restassured.RestAssured;

/**
 * @author : Rene Choi
 * @since : 2024/02/02
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommonAcceptanceTest {

	@LocalServerPort
	private int port;

	@DynamicPropertySource
	static void registerWireMockProperties(DynamicPropertyRegistry registry) {
		WireMockHelper.startWireMockServer();
		int wireMockPort = WireMockHelper.getWireMockPort();
		registry.add("wiremock.server.port", () -> wireMockPort);
		registry.add("feign.client.config.default.url", () -> "http://localhost:" + wireMockPort);
	}

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		setDynamicPort(port);
		WireMockHelper.resetWireMock();
	}

	private void setDynamicPort(int port) {
		DynamicPortHolder.setPort(port);
	}

	@BeforeEach
	public void databaseCleanup() {
	}
}