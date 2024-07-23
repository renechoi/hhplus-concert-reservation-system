package io.paymentservice.api.balance.infrastructure.persistence.querydsl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.paymentservice.api.balance.business.dto.inport.BalanceSearchCommand;
import io.paymentservice.api.balance.business.entity.Balance;
import io.paymentservice.api.balance.business.entity.QBalance;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
@Component
@RequiredArgsConstructor
public class BalanceQueryDslCustomRepositoryImpl implements BalanceQueryDslCustomRepository{

	private final JPAQueryFactory queryFactory;
	private final QueryFilter<BalanceSearchCommand> queryFilter;

	@Override
	public Optional<Balance> findSingleWithLock(BalanceSearchCommand searchCommand) {
		QBalance balance = QBalance.balance;

		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		return Optional.ofNullable(queryFactory.selectFrom(balance)
			.where(searchPredicate)
			.fetchOne());
	}
}
