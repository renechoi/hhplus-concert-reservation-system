package io.apiorchestrationservice.api.application.aspect;

import static io.apiorchestrationservice.common.model.CommonApiResponse.*;
import static io.apiorchestrationservice.common.model.GlobalResponseCode.*;

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

import io.apiorchestrationservice.api.business.dto.inport.DistributedLockCommand;
import io.apiorchestrationservice.api.business.dto.inport.DistributedUnLockCommand;
import io.apiorchestrationservice.api.business.dto.outport.DistributedLockInfo;
import io.apiorchestrationservice.api.business.service.DistributedLockService;
import io.apiorchestrationservice.common.annotation.DistributedLock;
import io.apiorchestrationservice.common.model.GlobalResponseCode;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {
	private final DistributedLockService lockService;

	@Pointcut("@annotation(distributedLock)")
	public void lockPointcut(DistributedLock distributedLock) {
	}


	@Around(value = "lockPointcut(distributedLock)", argNames = "joinPoint,distributedLock")
	public Object handleDistributedLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
		String lockKey = resolveKey(joinPoint, distributedLock);

		lockService.lock(DistributedLockCommand.of(lockKey, distributedLock));

		try {
			return joinPoint.proceed();
		} finally {
			lockService.unlock(DistributedUnLockCommand.from(lockKey));
		}
	}

	private String resolveKey(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String[] parameterNames = signature.getParameterNames();
		Object[] parameterValues = joinPoint.getArgs();

		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < parameterNames.length; i++) {
			context.setVariable(parameterNames[i], parameterValues[i]);
		}

		StringBuilder keyBuilder = new StringBuilder(distributedLock.prefix());
		for (String keyExpression : distributedLock.keys()) {
			String keyPart = parser.parseExpression(keyExpression).getValue(context, String.class);
			keyBuilder.append("-").append(keyPart);
		}

		return keyBuilder.toString();
	}
}
