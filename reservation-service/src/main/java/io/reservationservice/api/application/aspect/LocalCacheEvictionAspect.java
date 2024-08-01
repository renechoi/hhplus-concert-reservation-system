package io.reservationservice.api.application.aspect;

import java.util.Arrays;

import io.reservationservice.api.business.dto.inport.PropagateCacheEvictionCommand;
import io.reservationservice.api.business.service.impl.CacheEvictionPropagationService;
import io.reservationservice.common.annotation.LocalCacheEvict;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;


/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LocalCacheEvictionAspect {
	private final CacheEvictionPropagationService propagationService;
	private static final ExpressionParser parser = new SpelExpressionParser();
	@AfterReturning(pointcut = "@annotation(evictLocalCache)", returning = "result")
	public void afterReturning(JoinPoint joinPoint, LocalCacheEvict evictLocalCache, Object result) {
		Arrays.stream(evictLocalCache.keys())
			.forEach(customKey -> propagationService.propagateCacheEviction(PropagateCacheEvictionCommand.of(evictLocalCache.cacheName(), parseKey(result, customKey))));
	}
	private String parseKey(Object result, String customKey) {
		StandardEvaluationContext context = new StandardEvaluationContext(result);
		return parser.parseExpression(customKey).getValue(context, String.class);
	}
}