package io.paymentservice.api.common.cache.application.aspect;


import static io.paymentservice.common.mapper.ObjectMapperBasedVoMapper.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import io.paymentservice.api.common.cache.business.dto.command.CacheCommand;
import io.paymentservice.api.common.cache.business.dto.info.CacheInfo;
import io.paymentservice.api.common.cache.business.service.RedisCacheService;
import io.paymentservice.common.annotation.GlobalCacheable;
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
public class GlobalCacheAspect {
	private final RedisCacheService cacheService;

	@Pointcut("@annotation(globalCacheable)")
	public void cachePointcut(GlobalCacheable globalCacheable) {
	}

	@Around(value = "cachePointcut(globalCacheable)", argNames = "joinPoint,globalCacheable")
	public Object handleCache(ProceedingJoinPoint joinPoint, GlobalCacheable globalCacheable) throws Throwable {
		String cacheKey = resolveKey(joinPoint, globalCacheable);
		Object cachedValue = getCachedValue(cacheKey, ((MethodSignature) joinPoint.getSignature()).getReturnType());

		if (cachedValue != null) {
			return cachedValue;
		}

		Object result = joinPoint.proceed();
		cacheResult(cacheKey, result, globalCacheable.ttl());

		return result;
	}

	private <T> T getCachedValue(String cacheKey, Class<T> returnType) {
		try {
			CacheInfo cacheInfo = cacheService.get(cacheKey);
			if (cacheInfo != null) {
				return convert(readTree(String.valueOf(cacheInfo.cacheValue())) ,returnType );
			}
		} catch (Exception e) {
			log.error("Failed to get cache for key: {}", cacheKey, e);
		}
		return null;
	}


	private void cacheResult(String cacheKey, Object result, long ttl) {
		try {
			cacheService.cache(CacheCommand.of(cacheKey, result, ttl));
		} catch (Exception e) {
			log.error("Failed to cache result for key: {}", cacheKey, e);
		}
	}

	private String resolveKey(ProceedingJoinPoint joinPoint, GlobalCacheable globalCacheable) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String[] parameterNames = signature.getParameterNames();
		Object[] parameterValues = joinPoint.getArgs();

		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < parameterNames.length; i++) {
			context.setVariable(parameterNames[i], parameterValues[i]);
		}

		StringBuilder keyBuilder = new StringBuilder(globalCacheable.prefix());
		for (String keyExpression : globalCacheable.keys()) {
			String keyPart = parser.parseExpression(keyExpression).getValue(context, String.class);
			keyBuilder.append("-").append(keyPart);
		}

		return keyBuilder.toString();
	}
}