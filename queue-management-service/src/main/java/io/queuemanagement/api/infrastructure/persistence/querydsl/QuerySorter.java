package io.queuemanagement.api.infrastructure.persistence.querydsl;


import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPQLQuery;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface QuerySorter <T>{
	JPQLQuery<T> applySorting(JPQLQuery<T> query, Pageable pageable, EntityPathBase<T> entityPathBase);

	JPQLQuery<T> applySorting(JPQLQuery<T> query, String orderBy, String orderDirection, EntityPathBase<T> entityPathBase);
}
