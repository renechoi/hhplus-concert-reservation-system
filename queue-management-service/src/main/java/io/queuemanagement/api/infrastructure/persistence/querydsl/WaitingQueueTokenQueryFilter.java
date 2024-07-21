package io.queuemanagement.api.infrastructure.persistence.querydsl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.dto.inport.DateSearchCommand;
import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand;
import io.queuemanagement.api.infrastructure.entity.QWaitingQueueTokenEntity;
import io.queuemanagement.util.QueryDslBooleanExpressionBuilder;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Component
@RequiredArgsConstructor
public class WaitingQueueTokenQueryFilter implements QueryFilter<WaitingQueueTokenSearchCommand> {

	private static final QWaitingQueueTokenEntity waitingQueueTokenEntity = QWaitingQueueTokenEntity.waitingQueueTokenEntity;

	@Override
	public Predicate createGlobalSearchQuery(WaitingQueueTokenSearchCommand searchCommand) {
		QueryDslBooleanExpressionBuilder builder = new QueryDslBooleanExpressionBuilder(waitingQueueTokenEntity.isNotNull())
			.notNullAnd(waitingQueueTokenEntity.waitingQueueTokenId::eq, searchCommand.getWaitingQueueTokenId())
			.notEmptyAnd(waitingQueueTokenEntity.userId::eq, searchCommand.getUserId())
			.notEmptyAnd(waitingQueueTokenEntity.tokenValue::eq, searchCommand.getTokenValue())
			.notNullAnd(waitingQueueTokenEntity.status::eq, searchCommand.getStatus())
			.dateEquals(waitingQueueTokenEntity.createdAt, searchCommand.getCreatedAt())
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getValidUntil(), waitingQueueTokenEntity.validUntil));

		if (searchCommand.getStatuses() != null && !searchCommand.getStatuses().isEmpty()) {
			builder = builder.and(createStatusPredicate(searchCommand.getStatuses()));
		}

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

	private BooleanExpression createStatusPredicate(List<QueueStatus> statuses) {
		return waitingQueueTokenEntity.status.in(statuses);
	}

}
