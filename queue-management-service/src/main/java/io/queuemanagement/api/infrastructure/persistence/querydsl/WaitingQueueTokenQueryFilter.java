package io.queuemanagement.api.infrastructure.persistence.querydsl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
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
			.dateEquals(waitingQueueTokenEntity.createdAt, searchCommand.getCreatedAt());

		if (searchCommand.getValidUntil() != null && searchCommand.getDateSearchCondition() != null) {
			builder = builder.notEmptyAnd(this::createDatePredicate, searchCommand.getDateSearchCondition(), searchCommand.getValidUntil().toString());
		}

		if (searchCommand.getStatuses() != null && !searchCommand.getStatuses().isEmpty()) {
			builder = builder.and(createStatusPredicate(searchCommand.getStatuses()));
		}

		return builder.build();
	}


	private BooleanExpression createDatePredicate(String valueCondition, String value) {
		LocalDateTime dateTime = LocalDateTime.parse(value);
		if ("after".equals(valueCondition)) {
			return waitingQueueTokenEntity.validUntil.after(dateTime);
		} else if ("before".equals(valueCondition)) {
			return waitingQueueTokenEntity.validUntil.before(dateTime);
		} else if ("on".equals(valueCondition)) {
			return waitingQueueTokenEntity.validUntil.eq(dateTime);
		} else {
			return null;
		}
	}

	private static BooleanExpression createStatusPredicate(List<QueueStatus> statuses) {
		return waitingQueueTokenEntity.status.in(statuses);
	}


}
