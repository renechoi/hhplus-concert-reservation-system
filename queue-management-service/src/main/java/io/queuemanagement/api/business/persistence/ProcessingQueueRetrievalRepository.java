package io.queuemanagement.api.business.persistence;

import java.util.List;
import java.util.Optional;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public interface ProcessingQueueRetrievalRepository {
	long countByStatus(QueueStatus queueStatus);

	ProcessingQueueToken findSingleBy(ProcessingQueueTokenSearchCommand searchCommand);

	Optional<ProcessingQueueToken> findOptionalSingleBy(ProcessingQueueTokenSearchCommand searchCommand);

	List<ProcessingQueueToken> findAllBy(ProcessingQueueTokenSearchCommand searchCommand);
}
