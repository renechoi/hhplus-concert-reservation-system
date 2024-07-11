package io.clientchannelservice.api.infrastructure.clients;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@AutoConfigureWireMock(port = 0) // 포트를 0으로 설정하여 무작위 포트를 사용
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class WireMockSupport {
}