package io.queuemanagement.api.infrastructure.persistence.core;

import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.domainmodel.WaitingQueuePositionJson;
import io.queuemanagement.api.business.persistence.QueuePositionDocumentRepository;
import io.queuemanagement.api.infrastructure.entity.WaitingQueuePositionJsonEntity;
import io.queuemanagement.api.infrastructure.persistence.orm.QueuePositionDocumentJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Repository
@RequiredArgsConstructor
public class QueuePositionDocumentCoreRepository implements QueuePositionDocumentRepository {
	private final QueuePositionDocumentJpaRepository queuePositionDocumentJpaRepository;

	@Override
	public void updateQueuePositionJson(WaitingQueuePositionJson waitingQueuePositionJson) {
		queuePositionDocumentJpaRepository.save(queuePositionDocumentJpaRepository
			.findById(waitingQueuePositionJson.getWaitingQueuePositionJsonId())
			.orElse(WaitingQueuePositionJsonEntity.from(waitingQueuePositionJson))
			.updatePositionJson(waitingQueuePositionJson.getPositionJson()));
	}

	@Override
	public WaitingQueuePositionJson findDocumentByIdOrElseDefault(Long id) {
		return queuePositionDocumentJpaRepository.findById(id).map(WaitingQueuePositionJsonEntity::toDomain).orElse(WaitingQueuePositionJson.createDefault());
	}
}
