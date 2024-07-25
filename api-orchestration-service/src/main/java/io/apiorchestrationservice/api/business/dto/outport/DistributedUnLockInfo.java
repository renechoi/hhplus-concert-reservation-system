package io.apiorchestrationservice.api.business.dto.outport;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
public record DistributedUnLockInfo(
	 String lockKey,
	 Boolean isUnlocked
) {
}
