package io.clientchannelservice.api;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.clientchannelservice.fixture.WaitingQueueTokenFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.clientchannelservice.api.business.dto.outport.WaitingQueueTokenPollingInfo;
import io.clientchannelservice.api.infrastructure.clients.WaitingQueueTokenClientFeignAdapter;
import io.clientchannelservice.api.infrastructure.clients.WireMockSupport;
import io.clientchannelservice.api.infrastructure.clients.dto.WaitingQueueTokenDomainServiceResponse;
import io.clientchannelservice.testutils.TestObjectMapper;
import lombok.SneakyThrows;

public class ClientWireMockBasicTest extends WireMockSupport {

    @Autowired
    private WaitingQueueTokenClientFeignAdapter waitingQueueTokenClientFeignAdapter;

    @SneakyThrows
    @Test
    @DisplayName("Mocking된 값으로 사용자 ID에 대한 대기열 토큰 정보를 성공적으로 조회")
    void retrieveMockedWaitingQueueToken_FromQueueManagementService() {

        // given
        WaitingQueueTokenDomainServiceResponse tokenResponse = createWaitingQueueTokenDefaultFixtureResponse();

        String responseBody = TestObjectMapper.getInstance().writeValueAsString(tokenResponse);

        stubFor(get(urlPathMatching("/queue-management-service/api/waiting-queue-token/123"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)));

        // when
        Optional<WaitingQueueTokenPollingInfo> response = waitingQueueTokenClientFeignAdapter.retrieveToken("123");

        // then
        assertThat(response).isPresent();
        WaitingQueueTokenPollingInfo pollingInfo = response.get();
        assertThat(pollingInfo.waitingQueueTokenId()).isEqualTo(tokenResponse.getWaitingQueueTokenId());
        assertThat(pollingInfo.userId()).isEqualTo(tokenResponse.getUserId());
        assertThat(pollingInfo.tokenValue()).isEqualTo(tokenResponse.getTokenValue());
        assertThat(pollingInfo.position()).isEqualTo(tokenResponse.getPosition());
        assertThat(pollingInfo.validUntil()).isEqualTo(tokenResponse.getValidUntil());
        assertThat(pollingInfo.status()).isEqualTo(tokenResponse.getStatus());
        assertThat(pollingInfo.requestAt()).isEqualTo(tokenResponse.getRequestAt());

    }
}
