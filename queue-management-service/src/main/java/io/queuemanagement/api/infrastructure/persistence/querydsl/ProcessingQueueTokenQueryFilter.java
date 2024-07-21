package io.queuemanagement.api.infrastructure.persistence.querydsl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;

import io.queuemanagement.api.business.dto.inport.DateSearchCommand;
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
		QueryDslBooleanExpressionBuilder builder = new QueryDslBooleanExpressionBuilder(processingQueueTokenEntity.isNotNull())
			.notNullAnd(processingQueueTokenEntity.processingQueueTokenId::eq, searchCommand.getProcessingQueueTokenId())
			.notEmptyAnd(processingQueueTokenEntity.userId::eq, searchCommand.getUserId())
			.notEmptyAnd(processingQueueTokenEntity.tokenValue::eq, searchCommand.getTokenValue())
			.notNullAnd(processingQueueTokenEntity.status::eq, searchCommand.getStatus())
			.dateEquals(processingQueueTokenEntity.createdAt, searchCommand.getCreatedAt())

			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getValidUntil(), processingQueueTokenEntity.validUntil));

		return builder.build();
	}

	private BooleanExpression createDatePredicate(DateSearchCommand.DateSearchCondition condition, LocalDateTime date, DateTimePath<LocalDateTime> dateTimePath) {
		if (condition == null || date == null) {
			return null;
		}
		switch (condition) {
			case BEFORE:
				return dateTimePath.before(date);
			case AFTER:
				return dateTimePath.after(date);
			case ON:
				return dateTimePath.eq(date);
			default:
				return null;
		}
	}

}
