package io.queuemanagement.api.business.operators;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGenerateInfo;
import io.queuemanagement.api.business.persistence.ProcessingQueueRetrievalRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenRetrievalRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Disabled
@ExtendWith(MockitoExtension.class)
@DisplayName("WaitingQueueTokenDuplicateChecker 단위 테스트")
public class WaitingQueueTokenDuplicateCheckerTest {

	@Mock
	private WaitingQueueTokenRetrievalRepository waitingQueueTokenRetrievalRepository;

	@Mock
	private ProcessingQueueRetrievalRepository processingQueueRetrievalRepository;

	@InjectMocks
	private WaitingQueueTokenDuplicateChecker waitingQueueTokenDuplicateChecker;

	private WaitingQueueToken waitingQueueToken;
	private ProcessingQueueToken processingQueueToken;

	@BeforeEach
	void setUp() {
		waitingQueueToken = WaitingQueueToken.builder()
			.userId("testUser")
			.tokenValue("waitingTokenValue")
			.position(1L)
			.validUntil(LocalDateTime.now().plusMinutes(5))
			.status(QueueStatus.WAITING)
			.requestAt(LocalDateTime.now())
			.build();

		processingQueueToken = ProcessingQueueToken.builder()
			.userId("testUser")
			.tokenValue("processingTokenValue")
			.status(QueueStatus.PROCESSING)
			.build();
	}

	@Test
	@DisplayName("대기열에 토큰이 존재할 때 중복 체크")
	void testCheckForDuplicate_whenTokenExistsInWaitingQueue() {
		WaitingQueueTokenSearchCommand searchCommand = WaitingQueueTokenSearchCommand.builder()
			.userId("testUser")
			.status(QueueStatus.WAITING)
			.build();

		when(waitingQueueTokenRetrievalRepository.findOptionalByCondition(refEq(searchCommand)))
			.thenReturn(Optional.of(waitingQueueToken));

		Optional<WaitingQueueTokenGenerateInfo> result = waitingQueueTokenDuplicateChecker.check("testUser");

		assertEquals(WaitingQueueTokenGenerateInfo.from(waitingQueueToken), result.get());
	}

	@Test
	@DisplayName("처리열에 토큰이 존재할 때 중복 체크")
	void testCheckForDuplicate_whenTokenExistsInProcessingQueue() {
		WaitingQueueTokenSearchCommand waitingSearchCommand = WaitingQueueTokenSearchCommand.builder()
			.userId("testUser")
			.status(QueueStatus.WAITING)
			.build();

		when(waitingQueueTokenRetrievalRepository.findOptionalByCondition(refEq(waitingSearchCommand)))
			.thenReturn(Optional.empty());
		when(processingQueueRetrievalRepository.findOptionalByCondition(any()))
			.thenReturn(Optional.of(processingQueueToken));

		Optional<WaitingQueueTokenGenerateInfo> result = waitingQueueTokenDuplicateChecker.check("testUser");

		assertEquals(WaitingQueueTokenGenerateInfo.from(processingQueueToken), result.get());
	}

	@Test
	@DisplayName("대기열 및 처리열에 토큰이 존재하지 않을 때 중복 체크")
	void testCheckForDuplicate_whenTokenDoesNotExist() {
		WaitingQueueTokenSearchCommand waitingSearchCommand = WaitingQueueTokenSearchCommand.builder()
			.userId("testUser")
			.status(QueueStatus.WAITING)
			.build();

		when(waitingQueueTokenRetrievalRepository.findOptionalByCondition(refEq(waitingSearchCommand)))
			.thenReturn(Optional.empty());
		when(processingQueueRetrievalRepository.findOptionalByCondition(any()))
			.thenReturn(Optional.empty());

		Optional<WaitingQueueTokenGenerateInfo> result = waitingQueueTokenDuplicateChecker.check("testUser");

		assertEquals(Optional.empty(), result);
	}
}
