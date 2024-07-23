package io.paymentservice.api.balance.infrastructure.persistence.querydsl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;

import io.paymentservice.api.balance.business.entity.QBalance;
import io.paymentservice.api.balance.business.dto.inport.BalanceSearchCommand;
import io.paymentservice.api.balance.business.dto.inport.DateSearchCommand;
import io.paymentservice.util.QueryDslBooleanExpressionBuilder;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
@Component
@RequiredArgsConstructor
public class BalanceQueryFilter implements QueryFilter<BalanceSearchCommand> {

    private static final QBalance balance = QBalance.balance;

    @Override
    public Predicate createGlobalSearchQuery(BalanceSearchCommand searchCommand) {
        QueryDslBooleanExpressionBuilder builder = new QueryDslBooleanExpressionBuilder(balance.isNotNull())
            .notNullAnd(balance.balanceId::eq, searchCommand.getBalanceId())
            .notNullAnd(balance.userId::eq, searchCommand.getUserId())
            .notNullAnd(balance.amount::eq, searchCommand.getAmount())
            .and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getCreatedAt(), balance.createdAt))
            .and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getUpdatedAt(), balance.updatedAt));

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
