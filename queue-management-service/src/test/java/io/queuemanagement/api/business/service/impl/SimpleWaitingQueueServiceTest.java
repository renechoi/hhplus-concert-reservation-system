package io.queuemanagement.api.business.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.queuemanagement.api.business.domainmodel.WaitingQueuePositionJson;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenGenerateCommand;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGenerateInfo;
import io.queuemanagement.api.business.operators.WaitingQueueTokenDuplicateChecker;
import io.queuemanagement.api.business.persistence.QueuePositionDocumentRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenCounterCrudRepository;
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
	private WaitingQueueTokenEnqueueRepository waitingQueueTokenEnqueueRepository;

	@Mock
	private WaitingQueueTokenCounterCrudRepository waitingQueueTokenCounterCrudRepository;

	@Mock
	private WaitingQueueTokenRetrievalRepository waitingQueueTokenRetrievalRepository;

	@Mock
	private WaitingQueueTokenDuplicateChecker waitingQueueTokenDuplicateChecker;

	@Mock
	private QueuePositionDocumentRepository queuePositionDocumentRepository;

	@InjectMocks
	private SimpleWaitingQueueService simpleWaitingQueueService;

	@Test
	@DisplayName("토큰 생성 및 큐에 삽입 - 중복 토큰이 있는 경우")
	void testGenerateAndEnqueue_withDuplicate() {
		String userId = "user1";
		WaitingQueueTokenGenerateCommand command = new WaitingQueueTokenGenerateCommand(userId);
		WaitingQueueTokenGenerateInfo expectedInfo = new WaitingQueueTokenGenerateInfo(userId, "token123", null, 0, null, null);

		when(waitingQueueTokenDuplicateChecker.checkForDuplicate(userId))
			.thenReturn(Optional.of(expectedInfo));

		WaitingQueueTokenGenerateInfo result = simpleWaitingQueueService.generateAndEnqueue(command);

		assertEquals(expectedInfo, result);
		verify(waitingQueueTokenDuplicateChecker, times(1)).checkForDuplicate(userId);
		verify(waitingQueueTokenCounterCrudRepository, times(0)).getOrInitializeCounter();
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
		String positionJson = "{\"1\":1}";
		WaitingQueuePositionJson positionDocument = new WaitingQueuePositionJson(1L, positionJson);
		WaitingQueueTokenGeneralInfo expectedInfo = WaitingQueueTokenGeneralInfo.from(token);

		when(waitingQueueTokenRetrievalRepository.findTokenByUserIdWithThrows(userId))
			.thenReturn(token);
		when(queuePositionDocumentRepository.findDocumentByIdOrElseDefault(1L))
			.thenReturn(positionDocument);

		WaitingQueueTokenGeneralInfo result = simpleWaitingQueueService.retrieve(userId);

		assertEquals(expectedInfo, result);
		verify(waitingQueueTokenRetrievalRepository, times(1)).findTokenByUserIdWithThrows(userId);
		verify(queuePositionDocumentRepository, times(1)).findDocumentByIdOrElseDefault(1L);
	}
}