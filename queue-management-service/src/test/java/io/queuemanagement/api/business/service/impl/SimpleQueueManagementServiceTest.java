package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand.*;
import static io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.persistence.ProcessingQueueRepository;
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
	private ProcessingQueueRepository processingQueueRepository;


	@Mock
	private WaitingQueueTokenCounterCrudRepository waitingQueueTokenCounterCrudRepository;



	@Test
	@DisplayName("만료된 처리 대기열 토큰을 만료 상태로 업데이트")
	@Disabled("RDB 방식이 아닌 Redis 방식으로 변경되어 테스트 불가")
	public void testExpireProcessingQueueTokens() {
		ProcessingQueueToken token = mock(ProcessingQueueToken.class);
		when(processingQueueRetrievalRepository.findAllByCondition(onProcessingTokensExpiring(any())))
			.thenReturn(List.of(token));

		simpleQueueManagementService.expireProcessingQueueTokens();

		verify(processingQueueRepository, times(1)).store(any());
	}


	@Test
	@DisplayName("만료된 대기열 토큰을 만료 상태로 업데이트")
	public void testExpireWaitingQueueTokens() {
		WaitingQueueToken token = mock(WaitingQueueToken.class);
		when(waitingQueueTokenRetrievalRepository.findAllByCondition(onWaitingTokensExpiring( any())))
			.thenReturn(List.of(token));

		simpleQueueManagementService.expireWaitingQueueTokens();

		verify(waitingQueueTokenManagementRepository, times(1)).updateStatus(any());
	}

	@Test
	@DisplayName("사용자의 처리 대기열 토큰을 완료 상태로 업데이트")
	@Disabled("RDB 방식이 아닌 Redis 방식으로 변경되어 테스트 불가")
	public void testCompleteProcessingQueueToken() {
		ProcessingQueueToken token = mock(ProcessingQueueToken.class);
		when(processingQueueRetrievalRepository.findSingleByCondition(any())).thenReturn(token);

		simpleQueueManagementService.completeProcessingQueueToken("userId");

		verify(processingQueueRepository, times(1)).store(any());
	}

	@Test
	@DisplayName("사용자의 대기열 토큰을 완료 상태로 업데이트")
	public void testCompleteWaitingQueueTokenByUserId() {
		WaitingQueueToken token = mock(WaitingQueueToken.class);
		when(waitingQueueTokenRetrievalRepository.findAllByCondition(onActiveWaiting("userId"))).thenReturn(List.of(token));

		simpleQueueManagementService.completeWaitingQueueTokenByUserId("userId");

		verify(waitingQueueTokenManagementRepository, times(1)).updateStatus(any());
	}
}
