package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.domainmodel.QueueStatus.*;
import static io.queuemanagement.api.business.dto.inport.DateSearchCommand.DateSearchCondition.*;
import static io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand.*;
import static io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.persistence.ProcessingQueueEnqueueRepository;
import io.queuemanagement.api.business.persistence.ProcessingQueueRetrievalRepository;
import io.queuemanagement.api.business.persistence.ProcessingQueueStoreRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenCounterCrudRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenManagementRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenRetrievalRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SimpleQueueManagementService 단위 테스트")
public class SimpleQueueManagementServiceTest {

	@InjectMocks
	private SimpleQueueManagementService simpleQueueManagementService;

	@Mock
	private WaitingQueueTokenRetrievalRepository waitingQueueTokenRetrievalRepository;

	@Mock
	private WaitingQueueTokenManagementRepository waitingQueueTokenManagementRepository;

	@Mock
	private ProcessingQueueRetrievalRepository processingQueueRetrievalRepository;

	@Mock
	private ProcessingQueueEnqueueRepository processingQueueEnqueueRepository;

	@Mock
	private ProcessingQueueStoreRepository processingQueueStoreRepository;

	@Mock
	private WaitingQueueTokenCounterCrudRepository waitingQueueTokenCounterCrudRepository;



	@Test
	@DisplayName("만료된 처리 대기열 토큰을 만료 상태로 업데이트")
	public void testExpireProcessingQueueTokens() {
		ProcessingQueueToken token = mock(ProcessingQueueToken.class);
		when(processingQueueRetrievalRepository.findAllByCondition(statusAndValidUntil(QueueStatus.PROCESSING, any(), BEFORE)))
			.thenReturn(List.of(token));

		simpleQueueManagementService.expireProcessingQueueTokens();

		verify(processingQueueStoreRepository, times(1)).store(any());
	}

	@Test
	@DisplayName("만료된 대기열 토큰을 만료 상태로 업데이트")
	public void testExpireWaitingQueueTokens() {
		WaitingQueueToken token = mock(WaitingQueueToken.class);
		when(waitingQueueTokenRetrievalRepository.findAllByCondition(statusesAndValidUntil(List.of(WAITING, PROCESSING), any(), BEFORE)))
			.thenReturn(List.of(token));

		simpleQueueManagementService.expireWaitingQueueTokens();

		verify(waitingQueueTokenManagementRepository, times(1)).updateStatus(any());
	}

	@Test
	@DisplayName("사용자의 처리 대기열 토큰을 완료 상태로 업데이트")
	public void testCompleteProcessingQueueToken() {
		ProcessingQueueToken token = mock(ProcessingQueueToken.class);
		when(processingQueueRetrievalRepository.findSingleByCondition(any())).thenReturn(token);

		simpleQueueManagementService.completeProcessingQueueToken("userId");

		verify(processingQueueStoreRepository, times(1)).store(any());
	}

	@Test
	@DisplayName("사용자의 대기열 토큰을 완료 상태로 업데이트")
	public void testCompleteWaitingQueueTokenByUserId() {
		WaitingQueueToken token = mock(WaitingQueueToken.class);
		when(waitingQueueTokenRetrievalRepository.findAllByCondition(conditionOnUserIdAndStatus("userId", any()))).thenReturn(List.of(token));

		simpleQueueManagementService.completeWaitingQueueTokenByUserId("userId");

		verify(waitingQueueTokenManagementRepository, times(1)).updateStatus(any());
	}
}
