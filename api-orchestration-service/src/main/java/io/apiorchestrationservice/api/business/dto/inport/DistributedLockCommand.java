package io.apiorchestrationservice.api.business.dto.inport;

import java.util.concurrent.TimeUnit;

import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AbstractCommonRequestInfo;
import io.apiorchestrationservice.common.annotation.DistributedLock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistributedLockCommand extends AbstractCommonRequestInfo {
	private String lockKey;
	private long waitTime;
	private long leaseTime;
	private TimeUnit timeUnit;

	public static DistributedLockCommand of(String lockKey, DistributedLock distributedLock) {
		return DistributedLockCommand.builder()
			.lockKey(lockKey)
			.waitTime(distributedLock.waitTime())
			.leaseTime(distributedLock.leaseTime())
			.timeUnit(distributedLock.timeUnit())
			.build();
	}
}
