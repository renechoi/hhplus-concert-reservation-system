package io.clientchannelservice.testutils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public class WireMockHelper {

	private static WireMockServer wireMockServer;

	public static void startWireMockServer() {
		if (wireMockServer == null) {
			wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
			wireMockServer.start();
		}
	}

	public static void stopWireMockServer() {
		if (wireMockServer != null) {
			wireMockServer.stop();
			wireMockServer = null;
		}
	}

	public static int getWireMockPort() {
		if (wireMockServer != null) {
			return wireMockServer.port();
		} else {
			throw new IllegalStateException("WireMock server is not running.");
		}
	}

	public static void resetWireMock() {
		if (wireMockServer != null) {
			wireMockServer.resetAll();
		}
	}

	public static void stubFeignClientResponse(String userId, Object responseObject) {
		try {
			String responseBody = TestObjectMapper.getInstance().writeValueAsString(responseObject);
			wireMockServer.stubFor(get(urlPathMatching("/queue-management-service/api/waiting-queue-token/" + userId))
				.willReturn(aResponse()
					.withStatus(200)
					.withHeader("Content-Type", "application/json")
					.withBody(responseBody)));
		} catch (Exception e) {
			throw new RuntimeException("Failed to stub Feign client response", e);
		}
	}
}
