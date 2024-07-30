package io.apiorchestrationservice.api.business.dto.inport;

import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AbstractCommonRequestInfo;
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
public class DistributedUnLockCommand extends AbstractCommonRequestInfo {
	private String lockKey;

	public static DistributedUnLockCommand from(String resolvedKey) {
		return DistributedUnLockCommand.builder().lockKey(resolvedKey).build();
	}
}
