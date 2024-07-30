package io.redisservice.api.business.dto.info;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public record CounterInfo(
	String counterKey,
	long value) {

	public static CounterInfo of(String counterKey, long value) {
		return new CounterInfo(counterKey, value);
	}
}
