package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.domainmodel.WaitingQueueToken.*;
import static io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand.*;
import static io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper.*;
import static io.queuemanagement.util.YmlLoader.*;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.queuemanagement.api.business.domainmodel.WaitingQueuePositionJson;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.domainmodel.WaitingQueueTokenCounter;
import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenGenerateCommand;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGenerateInfo;
import io.queuemanagement.api.business.operators.WaitingQueueTokenDuplicateChecker;
import io.queuemanagement.api.business.persistence.QueuePositionDocumentRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenCounterCrudRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenEnqueueRepository;
import io.queuemanagement.api.business.persistence.WaitingQueueTokenRetrievalRepository;
import io.queuemanagement.api.business.service.WaitingQueueService;
import io.queuemanagement.common.exception.definitions.WaitingQueueMaxLimitExceededException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Service
@RequiredArgsConstructor
public class SimpleWaitingQueueService implements WaitingQueueService {
	private final WaitingQueueTokenEnqueueRepository waitingQueueTokenEnqueueRepository;
	private final WaitingQueueTokenCounterCrudRepository waitingQueueTokenCounterCrudRepository;
	private final WaitingQueueTokenRetrievalRepository waitingQueueTokenRetrievalRepository;
	private final WaitingQueueTokenDuplicateChecker waitingQueueTokenDuplicateChecker;
	private final QueuePositionDocumentRepository queuePositionDocumentRepository;

	@Override
	@Transactional
	public WaitingQueueTokenGenerateInfo generateAndEnqueue(WaitingQueueTokenGenerateCommand command) {
		Optional<WaitingQueueTokenGenerateInfo> checkForDuplicate = waitingQueueTokenDuplicateChecker.checkForDuplicate(command.getUserId());
		if (checkForDuplicate.isPresent()) {
			return checkForDuplicate.get();
		}

		WaitingQueueTokenCounter counter = waitingQueueTokenCounterCrudRepository.getOrInitializeCounter();

		if (counter.isNotAvailable(getMaxWaitingTokens())) { // 대기열에 인입 불가능한 경우
			throw new WaitingQueueMaxLimitExceededException();
		}

		waitingQueueTokenCounterCrudRepository.save(counter.withIncreasedCount());

		return WaitingQueueTokenGenerateInfo.from(waitingQueueTokenEnqueueRepository.enqueue(createToken(command).withValidUntil(getTokenExpiryInSeconds()).withPositionValue(counter.getCount())));
	}

	/**
	 * JSON 가져오기 및 파싱을 통해 사용자의 현재 대기열 위치를 검색합니다.
	 * Json 데이터는 스케줄링에 따라 백그라운드로 동기화되어야 합니다.
	 *
	 * @param userId 대기열 위치를 검색할 사용자의 ID
	 * @return 대기열 토큰의 일반 정보
	 * @throws Exception JSON 문서를 검색하거나 파싱하는 중 문제가 발생한 경우
	 *
	 * @deprecated 이 메서드는 높은 계산 비용과 성능 문제로 인해 사용되지 않습니다.
	 * 이 메서드는 데이터베이스에서 큰 JSON 문서를 가져와서 이를 역직렬화하는데, 높은 동시 요청 시 비효율적이고 느림을 확인하였습니다.
	 * 대신 {@link #retrieveByAiAtOnceCalculation(String)}를 사용합니다.
	 */
	@SneakyThrows
	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public WaitingQueueTokenGeneralInfo retrieveByJsonFetchingCalculation(String userId) {

		WaitingQueueToken token = waitingQueueTokenRetrievalRepository.findSingleByConditionWithThrows(searchActiveByUserIdAndOrderByRequestAtAsc(userId));

		WaitingQueuePositionJson positionDocument = queuePositionDocumentRepository.findDocumentByIdOrElseDefault(1L);
		Map<Long, Long> positionMap = getObjectMapper().readValue(positionDocument.getPositionJson(), Map.class);

		if(positionMap.isEmpty()){
			return WaitingQueueTokenGeneralInfo.from(token.withPositionValue(0L));
		}

		return WaitingQueueTokenGeneralInfo.from(
			token.withPositionValue(positionMap.get(token.getWaitingQueueTokenId()) != null ? positionMap.get(token.getWaitingQueueTokenId()) : token.getPosition()));
	}

	/**
	 * 실시간 Auto Increment Pk 기반 계산을 통해 사용자의 현재 대기열 위치를 검색합니다.
	 *
	 * @param userId 대기열 위치를 검색할 사용자의 ID
	 * @return 대기열 토큰의 일반 정보
	 *
	 * 이 메서드는 데이터베이스에서 토큰과 최소 토큰 ID를 검색하여 실시간으로 대기열 위치를 계산합니다.
	 * 위의 json 파싱 방식에 비해 계산 오버헤드가 상대적으로 적습니다.
	 */
	@Override
	@Transactional(readOnly = true)
	public WaitingQueueTokenGeneralInfo retrieveByAiAtOnceCalculation(String userId) {
		WaitingQueueToken token = waitingQueueTokenRetrievalRepository.findSingleByConditionWithThrows(searchActiveByUserIdAndOrderByRequestAtAsc(userId));
		Long position = token.getWaitingQueueTokenId() - waitingQueueTokenRetrievalRepository.findSingleByConditionWithThrows(searchMinTokenId()).getWaitingQueueTokenId() + 1;
		return WaitingQueueTokenGeneralInfo.from(token.withPositionValue(position));
	}

}
