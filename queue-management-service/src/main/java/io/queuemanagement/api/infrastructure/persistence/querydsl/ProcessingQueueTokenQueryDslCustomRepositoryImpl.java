package io.queuemanagement.api.infrastructure.persistence.querydsl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.queuemanagement.api.business.dto.inport.ProcessingQueueTokenSearchCommand;
import io.queuemanagement.api.infrastructure.entity.ProcessingQueueTokenEntity;
import io.queuemanagement.api.infrastructure.entity.QProcessingQueueTokenEntity;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Component
@RequiredArgsConstructor
public class ProcessingQueueTokenQueryDslCustomRepositoryImpl implements ProcessingQueueTokenQueryDslCustomRepository {
	private final JPAQueryFactory queryFactory;
	private final QueryFilter<ProcessingQueueTokenSearchCommand> queryFilter;
	private final QuerySorter<ProcessingQueueTokenEntity> querySorter;

	@Override
	public Optional<ProcessingQueueTokenEntity> findSingleByCondition(ProcessingQueueTokenSearchCommand searchCommand){
		QProcessingQueueTokenEntity processingQueueToken = QProcessingQueueTokenEntity.processingQueueTokenEntity;

		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		return Optional.ofNullable(
			queryFactory.selectFrom(processingQueueToken)
				.where(searchPredicate)
				.fetchOne()
		);
	}

	@Override
	public List<ProcessingQueueTokenEntity> findAllByCondition(ProcessingQueueTokenSearchCommand searchCommand) {
		QProcessingQueueTokenEntity processingQueueToken = QProcessingQueueTokenEntity.processingQueueTokenEntity;
		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		return queryFactory.selectFrom(processingQueueToken)
			.where(searchPredicate)
			.fetch();
	}
}
