package io.clientchannelservice.api.presentation.controller;

import static io.clientchannelservice.fixture.WaitingQueueTokenFixture.*;
import static io.clientchannelservice.testhelpers.apiexecutor.WaitingQueueTokenPollingApiExecutor.*;
import static io.clientchannelservice.testhelpers.parser.WaitingQueueTokenPollingResponseParser.*;
import static io.clientchannelservice.testutils.ResponseMatcher.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.clientchannelservice.api.CommonAcceptanceTest;
import io.clientchannelservice.api.infrastructure.clients.dto.WaitingQueueTokenDomainServiceResponse;
import io.clientchannelservice.testutils.WireMockHelper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;

@DisplayName("Polling API 인수테스트")
class WaitingQueueTokenPollingControllerAcceptanceTest extends CommonAcceptanceTest {

	@SneakyThrows
	@Test
	@DisplayName("사용자 ID에 대한 대기열 토큰 정보 조회 성공")
	void testRetrieveWaitingQueueTokenApi() {
		// given
		String userId = "123";
		WaitingQueueTokenDomainServiceResponse expectedResponse = createWaitingQueueTokenFixtureResponse(userId);
		WireMockHelper.stubFeignClientResponse(userId, expectedResponse);

		// when
		ExtractableResponse<Response> response = retrieveWaitingQueueToken(userId);

		// then
		assertThat(response.statusCode()).isEqualTo(200);
		assertFieldsEqual(expectedResponse, parseWaitingQueueTokenPollingResponse(response));
	}
}