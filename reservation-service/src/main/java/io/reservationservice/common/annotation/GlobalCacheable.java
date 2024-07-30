package io.reservationservice.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalCacheable {
	String prefix() default "";
	String[] keys();
	long ttl() default 60L; // default cache time-to-live
}