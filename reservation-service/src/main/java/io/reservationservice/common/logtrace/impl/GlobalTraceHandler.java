package io.reservationservice.common.logtrace.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import io.reservationservice.common.logtrace.LogTracer;
import io.reservationservice.common.logtrace.TraceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GlobalTraceHandler {

	private final LogTracer logTracer;
	private static final String EXCEPTION_LOGGED_KEY = "exceptionLogged";

	@Around("all()")
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		TraceStatus status = null;
		Object result = null;

		try {
			status = logTracer.begin(joinPoint.getSignature().toShortString());

			if (isAnnotationPresent(joinPoint, RestController.class)) {
				result = logWithParameters(joinPoint, status);
			} else {
				result = joinPoint.proceed();
				logTracer.end(result, status);
			}
			return result;
		} catch (Exception exception) {
			if (shouldLogException(joinPoint)) {
				logTracer.exception(null, status, exception);
				markExceptionAsLogged(joinPoint);
			}
			throw exception;
		}
	}

	private Object logWithParameters(ProceedingJoinPoint joinPoint, TraceStatus status) throws Throwable {
		log.info("Incoming Request Body: {}", Arrays.toString(joinPoint.getArgs())); // 들어오는 DTO 로깅
		Object result = joinPoint.proceed();
		log.info("Outgoing Response Body: {}", result); // 나가는 DTO 로깅
		logTracer.end(result, status);
		return result;
	}

	@SafeVarargs
	private static boolean isAnnotationPresent(ProceedingJoinPoint joinPoint, Class<? extends Annotation>... annotationClasses) {
		Class<?> targetClass = identifyClasses(joinPoint);
		return Arrays.stream(annotationClasses)
			.anyMatch(targetClass::isAnnotationPresent);
	}

	private static Class<?> identifyClasses(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		return method.getDeclaringClass();
	}

	private boolean shouldLogException(ProceedingJoinPoint joinPoint) {
		return Arrays.stream(joinPoint.getArgs()).noneMatch(arg -> arg instanceof Map &&
			((Map<?, ?>)arg).containsKey(EXCEPTION_LOGGED_KEY));
	}

	private void markExceptionAsLogged(ProceedingJoinPoint joinPoint) {
		for (Object arg : joinPoint.getArgs()) {
			if (arg instanceof Map) {
				((Map<String, Object>)arg).put(EXCEPTION_LOGGED_KEY, true);
				break;
			}
		}
	}

	@Pointcut("execution(* io.reservationservice.api..*(..)) ")
	public void all() {
	}

}



