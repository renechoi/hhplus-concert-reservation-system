package io.paymentservice.api.common.cache.application.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import io.paymentservice.api.common.cache.business.dto.command.PropagateCacheEvictionCommand;
import io.paymentservice.api.common.cache.business.service.CacheEvictionPropagationService;
import io.paymentservice.common.annotation.LocalCacheEvict;
import lombok.RequiredArgsConstructor;


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