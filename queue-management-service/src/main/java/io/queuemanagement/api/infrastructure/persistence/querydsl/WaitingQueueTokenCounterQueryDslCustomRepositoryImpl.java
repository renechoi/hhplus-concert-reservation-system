package io.queuemanagement.api.infrastructure.persistence.querydsl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.queuemanagement.api.infrastructure.entity.QWaitingQueueTokenCounterEntity;
import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenCounterEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/24
 */
@Component
@RequiredArgsConstructor
public class WaitingQueueTokenCounterQueryDslCustomRepositoryImpl implements WaitingQueueTokenCounterQueryDslCustomRepository {
	private final JPAQueryFactory queryFactory;
	private final EntityManager entityManager;

	@Override
	public Optional<WaitingQueueTokenCounterEntity> getAndLockCounter() {
		QWaitingQueueTokenCounterEntity counter = QWaitingQueueTokenCounterEntity.waitingQueueTokenCounterEntity;

		JPQLQuery<WaitingQueueTokenCounterEntity> query = queryFactory.selectFrom(counter)
			.where(counter.waitingQueueTokenCounterId.eq(1L))
			.setLockMode(LockModeType.PESSIMISTIC_WRITE);

		return Optional.ofNullable(query.fetchOne());
	}


}
