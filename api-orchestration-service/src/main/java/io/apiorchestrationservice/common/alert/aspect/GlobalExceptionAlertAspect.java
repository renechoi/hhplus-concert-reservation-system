package io.apiorchestrationservice.common.alert.aspect;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import io.apiorchestrationservice.common.alert.GlobalExceptionAlertEvent;
import io.apiorchestrationservice.common.alert.publisher.GlobalExceptionAlertInternalPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GlobalExceptionAlertAspect {

	private final GlobalExceptionAlertInternalPublisher globalExceptionAlertInternalPublisher;
	private final Set<Throwable> markedExceptions = Collections.newSetFromMap(new WeakHashMap<>());


	@Pointcut("execution(public * io.apiorchestrationservice.api..*(..))")
	void apiMethods() {
	}

	@Pointcut("@annotation(org.springframework.scheduling.annotation.Async)")
	void async() {}

	@Pointcut("!async()")
	void notAsync() {}

	@AfterThrowing(pointcut = "apiMethods() && notAsync()", throwing = "throwable")
	public void alertApplicationWideMethods(JoinPoint jp, Throwable throwable) {
		if (!isMarkedException(throwable)) {
			log.debug("(alertApplicationWideMethods) Class = {}, Method = {}, Cause = {}", jp.getSignature().getDeclaringTypeName(), jp.getSignature().getName(), throwable.getLocalizedMessage());
			globalExceptionAlertInternalPublisher.publish(new GlobalExceptionAlertEvent(jp.getSignature().getName(), throwable));
			markException(throwable);
		}
	}

	private boolean isMarkedException(Throwable throwable) {
		return markedExceptions.contains(throwable);
	}

	private void markException(Throwable throwable) {
		markedExceptions.add(throwable);
	}

}
