package io.queuemanagement.api.business.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.domainmodel.WaitingQueueTokenCounter;
import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenGenerateCommand;
import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.operators.WaitingQueueTokenCounterManager;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenEnqueueRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenRetrievalRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("SimpleWaitingQueueService 단위 테스트")
public class SimpleWaitingQueueServiceTest {

	@Mock
	private WaitingQueueTokenEnqueueRepository enqueueRepository;


	@Mock
	private WaitingQueueTokenRetrievalRepository waitingQueueRepository;



	@InjectMocks
	private SimpleWaitingQueueService simpleWaitingQueueService;


	@Mock
	private WaitingQueueTokenCounterManager counterManager;


	@Test
	@DisplayName("토큰 생성 및 큐에 삽입 - 중복 토큰이 있는 경우")
	void testGenerateAndEnqueue_withDuplicate() {
		String userId = "user1";
		WaitingQueueTokenGenerateCommand command = new WaitingQueueTokenGenerateCommand(userId);

		WaitingQueueTokenCounter mockCounter = mock(WaitingQueueTokenCounter.class);
		when(counterManager.getWithLockAndIncreaseCounter(anyLong())).thenReturn(mockCounter);

		when(enqueueRepository.enqueue(any(WaitingQueueToken.class))).thenThrow(new DataIntegrityViolationException("Unique constraint violation"));

		DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
			simpleWaitingQueueService.generateAndEnqueue(command);
		});

		assertEquals("Unique constraint violation", exception.getMessage());
	}


	@Test
	@DisplayName("대기열 토큰 조회 - 정상 케이스")
	void testRetrieve() throws Exception {
		String userId = "user1";
		WaitingQueueToken token = WaitingQueueToken.builder()
			.waitingQueueTokenId(1L)
			.userId(userId)
			.tokenValue("token123")
			.position(1L)
			.build();
		WaitingQueueTokenGeneralInfo expectedInfo = WaitingQueueTokenGeneralInfo.from(token);

		when(waitingQueueRepository.findSingleByCondition(any(WaitingQueueTokenSearchCommand.class)))
			.thenReturn(token);

		WaitingQueueTokenGeneralInfo result = simpleWaitingQueueService.retrieveByAiAtOnceCalculation(userId);

		assertEquals(expectedInfo, result);
	}
}
