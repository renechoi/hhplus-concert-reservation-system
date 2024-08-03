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

import io.queuemanagement.api.application.facade.ProcessingQueueFacade;

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
	private ProcessingQueueFacade processingQueueFacade;

	@Test
	@DisplayName("대기열 -> 처리열 이동 스케줄러 테스트")
	void testProcessScheduledQueueTransfer() {
		Awaitility.await()
			.atMost(2, TimeUnit.SECONDS)
			.untilAsserted(() -> verify(processingQueueFacade, atLeastOnce()).processQueueTransfer());
	}

}