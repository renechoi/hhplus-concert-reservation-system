package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.domainmodel.QueueStatus.*;
import static io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
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
@DisplayName("SimpleProcessingQueueTokenService 테스트")
public class SimpleProcessingQueueTokenServiceTest {

	@Mock
	private ProcessingQueueRetrievalRepository processingQueueRetrievalRepository;

	@InjectMocks
	private SimpleProcessingQueueTokenService simpleProcessingQueueTokenService;


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
	void testCheckProcessingQueueTokenAvailability_withTokenAndUserId() {
		String tokenValue = "token123";
		String userId = "user123";

		when(processingQueueRetrievalRepository.findSingleBy(tokenAndUserIdAndStatus(tokenValue, userId, PROCESSING)))
			.thenReturn(mockProcessingQueueToken);

		ProcessingQueueTokenGeneralInfo result = simpleProcessingQueueTokenService.checkProcessingQueueTokenAvailability(tokenValue, userId);

		assertNotNull(result);
		assertEquals(mockProcessingQueueToken.getProcessingQueueTokenId(), result.processingQueueTokenId());
		assertEquals(mockProcessingQueueToken.getTokenValue(), result.tokenValue());
		assertEquals(mockProcessingQueueToken.getUserId(), result.userId());
		verify(processingQueueRetrievalRepository, times(1)).findSingleBy(tokenAndUserIdAndStatus(tokenValue, userId, PROCESSING));
	}

	@Test
	@DisplayName("토큰으로 처리 중인 토큰의 가용성 확인")
	void testCheckProcessingQueueTokenAvailability_withToken() {
		String tokenValue = "token123";

		when(processingQueueRetrievalRepository.findSingleBy(tokenAndStatus(tokenValue, PROCESSING)))
			.thenReturn(mockProcessingQueueToken);

		ProcessingQueueTokenGeneralInfo result = simpleProcessingQueueTokenService.checkProcessingQueueTokenAvailability(tokenValue);

		assertNotNull(result);
		assertEquals(mockProcessingQueueToken.getProcessingQueueTokenId(), result.processingQueueTokenId());
		assertEquals(mockProcessingQueueToken.getTokenValue(), result.tokenValue());
		assertEquals(mockProcessingQueueToken.getUserId(), result.userId());
		verify(processingQueueRetrievalRepository, times(1)).findSingleBy(tokenAndStatus(tokenValue, PROCESSING));
	}
}
