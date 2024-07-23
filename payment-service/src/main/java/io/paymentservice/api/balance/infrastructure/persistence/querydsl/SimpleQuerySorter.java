package io.paymentservice.api.balance.infrastructure.persistence.querydsl;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;

import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Component
@RequiredArgsConstructor
public class SimpleQuerySorter<T> implements QuerySorter<T> {
	@Override
	public JPQLQuery<T> applySorting(JPQLQuery<T> query, Pageable pageable, EntityPathBase<T> entityPathBase) {
		for (Sort.Order order : pageable.getSort()){
			PathBuilder<T> pathBuilder = new PathBuilder<>(entityPathBase.getType(), entityPathBase.getMetadata());
			PathBuilder<Object> objectPathBuilder = pathBuilder.get(order.getProperty());
			query = query.orderBy(new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC, objectPathBuilder));
		}
		return query;
	}
}
