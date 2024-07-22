package io.queuemanagement.api.interfaces.scheduler;

import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.queuemanagement.api.application.facade.QueueManagementFacade;

/**
 * @author : Rene Choi
 * @since : 2024/07/17
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
	"scheduler.queueTransferRate=1000",
	"scheduler.expireTokensRate=1000"
})
@ActiveProfiles("test")
class TokenSchedulerMereSchedulingTest {

	@MockBean
	private QueueManagementFacade queueManagementFacade;

	@Test
	@DisplayName("대기열 -> 처리열 이동 스케줄러 테스트")
	void testProcessScheduledQueueTransfer() {
		Awaitility.await()
			.atMost(2, TimeUnit.SECONDS)
			.untilAsserted(() -> verify(queueManagementFacade, atLeastOnce()).processQueueTransfer());
	}

	@Test
	@DisplayName("만료 토큰 처리 스케줄러 테스트")
	void testExpireTokens() {
		Awaitility.await()
			.atMost(2, TimeUnit.SECONDS)
			.untilAsserted(() -> verify(queueManagementFacade, atLeastOnce()).expireQueueTokens());
	}
}