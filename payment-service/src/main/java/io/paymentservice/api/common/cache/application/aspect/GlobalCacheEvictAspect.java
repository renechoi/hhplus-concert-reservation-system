package io.paymentservice.api.common.cache.application.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import io.paymentservice.api.common.cache.business.dto.command.EvictCacheCommand;
import io.paymentservice.api.common.cache.business.service.RedisCacheService;
import io.paymentservice.common.annotation.GlobalCacheEvict;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalCacheEvictAspect {

	private final RedisCacheService cacheService;
	@Pointcut("@annotation(globalCacheEvict)")
	public void evictPointcut(GlobalCacheEvict globalCacheEvict) {
	}

	@Before(value = "evictPointcut(globalCacheEvict)", argNames = "joinPoint,globalCacheEvict")
	public void handleCacheEvict(JoinPoint joinPoint, GlobalCacheEvict globalCacheEvict) {
		String cacheKey = resolveKey(joinPoint, globalCacheEvict);
		evictCache(cacheKey);
	}

	private void evictCache(String cacheKey) {
		try {
			cacheService.evict(EvictCacheCommand.builder().cacheKey(cacheKey).build());
			log.info("Evicted cache with key: {}", cacheKey);
		} catch (Exception e) {
			log.error("Failed to evict cache for key: {}", cacheKey, e);
		}
	}

	private String resolveKey(JoinPoint joinPoint, GlobalCacheEvict globalCacheEvict) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String[] parameterNames = signature.getParameterNames();
		Object[] parameterValues = joinPoint.getArgs();

		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < parameterNames.length; i++) {
			context.setVariable(parameterNames[i], parameterValues[i]);
		}

		StringBuilder keyBuilder = new StringBuilder(globalCacheEvict.prefix());
		for (String keyExpression : globalCacheEvict.keys()) {
			String keyPart = parser.parseExpression(keyExpression).getValue(context, String.class);
			keyBuilder.append("-").append(keyPart);
		}

		return keyBuilder.toString();
	}
}