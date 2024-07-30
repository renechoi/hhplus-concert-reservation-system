package io.queuemanagement.api.infrastructure.persistence.querydsl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenSearchCommand;
import io.queuemanagement.api.infrastructure.entity.QWaitingQueueTokenEntity;
import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenEntity;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/15
 */
@Component
@RequiredArgsConstructor
public class WaitingQueueTokenQueryDslCustomRepositoryImpl implements WaitingQueueTokenQueryDslCustomRepository{
	private final JPAQueryFactory queryFactory;
	private final QueryFilter<WaitingQueueTokenSearchCommand> queryFilter;
	private final QuerySorter<WaitingQueueTokenEntity> querySorter;
	private final QWaitingQueueTokenEntity processingQueueToken = QWaitingQueueTokenEntity.waitingQueueTokenEntity;

	@Override
	public Optional<WaitingQueueTokenEntity> findSingleByCondition(WaitingQueueTokenSearchCommand searchCommand){

		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		JPAQuery<WaitingQueueTokenEntity> query = queryFactory.selectFrom(processingQueueToken)
			.where(searchPredicate);

		if (searchCommand.getOrderBy() != null && searchCommand.getOrderDirection() != null) {
			querySorter.applySorting(query, searchCommand.getOrderBy(), searchCommand.getOrderDirection(), processingQueueToken);
		}

		if (searchCommand.getTop() != null && searchCommand.getTop() == 1) {
			query.limit(1);
		}

		return Optional.ofNullable(query.fetchOne());
	}

	@Override
	public List<WaitingQueueTokenEntity> findAllByCondition(WaitingQueueTokenSearchCommand searchCommand) {
		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);


		JPAQuery<WaitingQueueTokenEntity> query = queryFactory.selectFrom(processingQueueToken)
			.where(searchPredicate);

		if (searchCommand.getOrderBy() != null && searchCommand.getOrderDirection() != null) {
			querySorter.applySorting(query, searchCommand.getOrderBy(), searchCommand.getOrderDirection(), processingQueueToken);
		}

		return query.fetch();
	}

	@Override
	public Optional<WaitingQueueTokenEntity> findOptionalByConditionWithLock(WaitingQueueTokenSearchCommand searchCommand) {
		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		JPAQuery<WaitingQueueTokenEntity> query = queryFactory.selectFrom(processingQueueToken)
			.where(searchPredicate)
			.setLockMode(LockModeType.PESSIMISTIC_WRITE);

		return Optional.ofNullable(query.fetchOne());
	}
}
