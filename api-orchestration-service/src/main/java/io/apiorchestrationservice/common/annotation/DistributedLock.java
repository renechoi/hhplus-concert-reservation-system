package io.apiorchestrationservice.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import org.hibernate.id.factory.IdentifierGeneratorFactory;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
	String prefix() default "";
	String[] keys();
	TimeUnit timeUnit() default TimeUnit.SECONDS;
	long waitTime() default 0;  // 기본 대기 시간
	long leaseTime() default 2; // 기본 잠금 유지 시간
}
