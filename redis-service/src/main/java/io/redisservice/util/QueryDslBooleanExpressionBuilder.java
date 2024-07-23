package io.redisservice.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */

public class QueryDslBooleanExpressionBuilder {
	private final BooleanExpression booleanExpression;

	public QueryDslBooleanExpressionBuilder(BooleanExpression booleanExpression) {
		this.booleanExpression = booleanExpression;
	}

	public <T> QueryDslBooleanExpressionBuilder notNullAnd(Function<T, BooleanExpression> expressionFunction, T value) {
		if (value != null) {
			return new QueryDslBooleanExpressionBuilder(this.booleanExpression.and(expressionFunction.apply(value)));
		}
		return this;
	}

	public <T extends Number> QueryDslBooleanExpressionBuilder notZeroAnd(Function<T, BooleanExpression> expressionFunction, T value) {
		if (value != null && value.intValue() != 0) {
			return new QueryDslBooleanExpressionBuilder(this.booleanExpression.and(expressionFunction.apply(value)));
		}
		return this;
	}

	public QueryDslBooleanExpressionBuilder notEmptyAnd(
		Function<String, BooleanExpression> expressionFunction, String value) {
		if (!StringUtils.isEmpty(value)) {
			return new QueryDslBooleanExpressionBuilder(
				this.booleanExpression.and(expressionFunction.apply(value)));
		}
		return this;
	}

	public QueryDslBooleanExpressionBuilder notEmptyAnd(
		Function<String, BooleanExpression> expressionFunction, String valueCondition,
		String value) {
		if (!StringUtils.isEmpty(valueCondition)) {
			return new QueryDslBooleanExpressionBuilder(
				this.booleanExpression.and(expressionFunction.apply(value)));
		}
		return this;
	}

	public <T> QueryDslBooleanExpressionBuilder notEmptyAnd(
		Function<Collection<T>, BooleanExpression> expressionFunction, Collection<T> collection) {
		if (!CollectionUtils.isEmpty(collection)) {
			return new QueryDslBooleanExpressionBuilder(
				this.booleanExpression.and(expressionFunction.apply(collection)));
		}
		return this;
	}

	public QueryDslBooleanExpressionBuilder notEmptyAnd(BiFunction<String, String, BooleanExpression> expressionFunction,
		String condition, String value) {
		if (condition != null && value != null) {
			return new QueryDslBooleanExpressionBuilder(this.booleanExpression.and(expressionFunction.apply(condition, value)));
		}
		return this;
	}


	public QueryDslBooleanExpressionBuilder andAnyOf(BooleanExpression... conditions) {
		if (conditions == null || conditions.length == 0) {
			return this;
		}

		// null이 아닌 조건들만 필터링
		BooleanExpression combinedCondition = Arrays.stream(conditions)
			.filter(Objects::nonNull)
			.reduce(BooleanExpression::or)
			.orElse(null);

		if (combinedCondition != null) {
			return new QueryDslBooleanExpressionBuilder(this.booleanExpression.and(combinedCondition));
		}

		return this;
	}


	public QueryDslBooleanExpressionBuilder dateEquals(DateTimePath<LocalDateTime> dateTimePath, LocalDateTime value) {
		if (value != null) {
			LocalDate date = value.toLocalDate();
			return new QueryDslBooleanExpressionBuilder(this.booleanExpression
				.and(dateTimePath.year().eq(date.getYear())
					.and(dateTimePath.month().eq(date.getMonthValue()))
					.and(dateTimePath.dayOfMonth().eq(date.getDayOfMonth()))
				));
		}
		return this;
	}

	public QueryDslBooleanExpressionBuilder and(BooleanExpression newCondition) {
		if (newCondition != null) {
			return new QueryDslBooleanExpressionBuilder(this.booleanExpression.and(newCondition));
		}
		return this;
	}



	public BooleanExpression build() {
		return this.booleanExpression;
	}
}
