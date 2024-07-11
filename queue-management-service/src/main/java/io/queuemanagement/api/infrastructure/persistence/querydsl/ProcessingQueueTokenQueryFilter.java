package io.queuemanagement.api.infrastructure.persistence.querydsl;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;

import io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand;
import io.queuemanagement.api.infrastructure.entity.QProcessingQueueTokenEntity;
import io.queuemanagement.util.QueryDslBooleanExpressionBuilder;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Component
@RequiredArgsConstructor
public class ProcessingQueueTokenQueryFilter implements QueryFilter<ProcessingQueueTokenSearchCommand> {

	private static final QProcessingQueueTokenEntity processingQueueTokenEntity = QProcessingQueueTokenEntity.processingQueueTokenEntity;

	@Override
	public Predicate createGlobalSearchQuery(ProcessingQueueTokenSearchCommand searchCommand) {
		return new QueryDslBooleanExpressionBuilder(processingQueueTokenEntity.isNotNull())
			.notNullAnd(processingQueueTokenEntity.processingQueueTokenId::eq, searchCommand.getProcessingQueueTokenId())
			.notEmptyAnd(processingQueueTokenEntity.userId::eq, searchCommand.getUserId())
			.notEmptyAnd(processingQueueTokenEntity.tokenValue::eq, searchCommand.getTokenValue())
			.notNullAnd(processingQueueTokenEntity.validUntil::eq, searchCommand.getValidUntil())
			.notNullAnd(processingQueueTokenEntity.status::eq, searchCommand.getStatus())
			.dateEquals(processingQueueTokenEntity.createdAt, searchCommand.getCreatedAt())
			.build();
	}

}
