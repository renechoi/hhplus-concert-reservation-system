package io.queuemanagement.api.infrastructure.persistence.querydsl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

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
			.dateEquals(processingQueueTokenEntity.createdAt, searchCommand.getCreatedAt());

		if (searchCommand.getValidUntil() != null && searchCommand.getDateSearchCondition() != null) {
			builder= builder.notEmptyAnd(this::createDatePredicate, searchCommand.getDateSearchCondition(), searchCommand.getValidUntil().toString());
		}

		return builder.build();
	}

	private BooleanExpression createDatePredicate(String valueCondition, String value) {
		LocalDateTime dateTime = LocalDateTime.parse(value);
		if ("after".equals(valueCondition)) {
			return processingQueueTokenEntity.validUntil.after(dateTime);
		} else if ("before".equals(valueCondition)) {
			return processingQueueTokenEntity.validUntil.before(dateTime);
		} else if ("on".equals(valueCondition)) {
			return processingQueueTokenEntity.validUntil.eq(dateTime);
		} else {
			return null;
		}
	}

}
