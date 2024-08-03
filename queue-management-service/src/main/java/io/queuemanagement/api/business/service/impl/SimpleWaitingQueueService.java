package io.queuemanagement.api.business.service.impl;

import static io.queuemanagement.api.business.domainmodel.WaitingQueueToken.*;

import org.springframework.stereotype.Service;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenGenerateCommand;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGeneralInfo;
import io.queuemanagement.api.business.dto.outport.WaitingQueueTokenGenerateInfo;
import io.queuemanagement.api.business.persistence.WaitingQueueRepository;
import io.queuemanagement.api.business.service.WaitingQueueService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Service
@RequiredArgsConstructor
public class SimpleWaitingQueueService implements WaitingQueueService {
	private final WaitingQueueRepository repository;

	/**
	 * Redis 방식에서는 비동기 호출을 통해 성능 개선을 기대할 수 있으며,
	 * 필요 시 서비스 반환 스펙을 CompletableFuture로 변경할 수 있습니다.
	 */
	@Override
	public WaitingQueueTokenGenerateInfo enqueue(WaitingQueueTokenGenerateCommand command) {
		WaitingQueueToken newToken = createToken(command);
		repository.enqueue(newToken);
		return WaitingQueueTokenGenerateInfo.from(newToken);
	}

	@Override
	public WaitingQueueTokenGeneralInfo retrieve(String userId) {
		WaitingQueueToken token = repository.retrieveToken(userId);
		return WaitingQueueTokenGeneralInfo.from(token);
	}

}
