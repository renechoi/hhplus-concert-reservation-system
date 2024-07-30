package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.domainmodel.QueueStatus.*;
import static io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.dto.outport.ProcessingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.persistence.ProcessingQueueRetrievalRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("SimpleProcessingQueueService 테스트")
public class SimpleProcessingQueueServiceTest {

	@Mock
	private ProcessingQueueRetrievalRepository processingQueueRetrievalRepository;

	@InjectMocks
	private SimpleProcessingQueueService simpleProcessingQueueTokenService;


	private ProcessingQueueToken mockProcessingQueueToken;

	@BeforeEach
	void setUp() {
		mockProcessingQueueToken = ProcessingQueueToken.builder()
			.processingQueueTokenId(1L)
			.userId("user123")
			.tokenValue("token123")
			.validUntil(LocalDateTime.now().plusHours(1))
			.status(PROCESSING)
			.build();
	}

	@Test
	@DisplayName("토큰과 사용자 ID로 처리 중인 토큰의 가용성 확인")
	@Disabled("RDB 유효 테스트 - Redis 변경시 불필요")
	void testCheckProcessingQueueTokenAvailability_withTokenAndUserId() {
		String tokenValue = "token123";
		String userId = "user123";

		when(processingQueueRetrievalRepository.findSingleByCondition(onProcessing(tokenValue, userId)))
			.thenReturn(mockProcessingQueueToken);

		ProcessingQueueTokenGeneralInfo result = simpleProcessingQueueTokenService.checkProcessingTokenAvailability(tokenValue, userId);

		assertNotNull(result);
		assertEquals(mockProcessingQueueToken.getProcessingQueueTokenId(), result.processingQueueTokenId());
		assertEquals(mockProcessingQueueToken.getTokenValue(), result.tokenValue());
		assertEquals(mockProcessingQueueToken.getUserId(), result.userId());
		verify(processingQueueRetrievalRepository, times(1)).findSingleByCondition(onProcessing(tokenValue, userId));
	}

	@Test
	@DisplayName("토큰으로 처리 중인 토큰의 가용성 확인")
	@Disabled("RDB 유효 테스트 - Redis 변경시 불필요")
	void testCheckProcessingQueueTokenAvailability_withToken() {
		String tokenValue = "token123";

		when(processingQueueRetrievalRepository.findSingleByCondition(onProcessing(tokenValue)))
			.thenReturn(mockProcessingQueueToken);

		ProcessingQueueTokenGeneralInfo result = simpleProcessingQueueTokenService.checkProcessingTokenAvailability(tokenValue);

		assertNotNull(result);
		assertEquals(mockProcessingQueueToken.getProcessingQueueTokenId(), result.processingQueueTokenId());
		assertEquals(mockProcessingQueueToken.getTokenValue(), result.tokenValue());
		assertEquals(mockProcessingQueueToken.getUserId(), result.userId());
		verify(processingQueueRetrievalRepository, times(1)).findSingleByCondition(onProcessing(tokenValue));
	}
}
