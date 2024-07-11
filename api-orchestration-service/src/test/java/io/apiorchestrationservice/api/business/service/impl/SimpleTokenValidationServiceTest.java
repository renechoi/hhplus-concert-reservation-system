package io.apiorchestrationservice.api.business.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.apiorchestrationservice.api.business.client.QueueManagementClientAdapter;
import io.apiorchestrationservice.api.business.domainmodel.QueueStatus;
import io.apiorchestrationservice.api.business.dto.outport.ProcessingQueueTokenInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@DisplayName("SimpleTokenValidationService 단위 테스트")
class SimpleTokenValidationServiceTest {

	@Mock
	private QueueManagementClientAdapter queueManagementClientAdapter;

	@InjectMocks
	private SimpleTokenValidationService simpleTokenValidationService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("유효한 토큰을 검증할 때 true를 반환")
	void validateQueueToken_ValidToken_ReturnsTrue() {
		// given
		String validToken = "validToken";
		ProcessingQueueTokenInfo tokenInfo = new ProcessingQueueTokenInfo(
			1L,
			"user1",
			validToken,
			1L,
			LocalDateTime.now().plusHours(1),
			QueueStatus.PROCESSING
		);

		when(queueManagementClientAdapter.retrieveToken(anyString())).thenReturn(Optional.of(tokenInfo));

		// when
		boolean isValid = simpleTokenValidationService.validateQueueToken(validToken);

		// then
		assertTrue(isValid);
	}

	@Test
	@DisplayName("유효하지 않은 토큰을 검증할 때 false를 반환")
	void validateQueueToken_InvalidToken_ReturnsFalse() {
		// given
		String invalidToken = "invalidToken";

		when(queueManagementClientAdapter.retrieveToken(anyString())).thenReturn(Optional.empty());

		// when
		boolean isValid = simpleTokenValidationService.validateQueueToken(invalidToken);

		// then
		assertFalse(isValid);
	}

	@Test
	@DisplayName("토큰 값이 불일치할 때 false를 반환")
	void validateQueueToken_TokenMismatch_ReturnsFalse() {
		// given
		String tokenValue = "tokenValue";
		String differentTokenValue = "differentTokenValue";
		ProcessingQueueTokenInfo tokenInfo = new ProcessingQueueTokenInfo(
			1L,
			"user1",
			differentTokenValue,
			1L,
			LocalDateTime.now().plusHours(1),
			QueueStatus.PROCESSING
		);

		when(queueManagementClientAdapter.retrieveToken(anyString())).thenReturn(Optional.of(tokenInfo));

		// when
		boolean isValid = simpleTokenValidationService.validateQueueToken(tokenValue);

		// then
		assertFalse(isValid);
	}
}
