package io.queuemanagement.api.business.persistence;

import java.util.List;
import java.util.Optional;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
public interface WaitingQueueTokenRetrievalRepository {

	WaitingQueueToken findSingleBy(WaitingQueueTokenSearchCommand searchCommand);

	Optional<WaitingQueueToken> findOptionalSingleBy(WaitingQueueTokenSearchCommand searchCommand);

	List<WaitingQueueToken> findAllBy(WaitingQueueTokenSearchCommand searchCommand);

}
