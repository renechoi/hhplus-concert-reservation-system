package io.paymentservice.api.balance.infrastructure.persistence.querydsl;

import com.querydsl.core.types.Predicate;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface QueryFilter<T> {

	Predicate createGlobalSearchQuery(T t);
}
