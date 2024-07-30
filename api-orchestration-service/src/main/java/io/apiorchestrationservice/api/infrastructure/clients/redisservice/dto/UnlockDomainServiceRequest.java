package io.apiorchestrationservice.api.infrastructure.clients.redisservice.dto;

import io.apiorchestrationservice.api.business.dto.inport.DistributedUnLockCommand;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnlockDomainServiceRequest {
    private String lockKey;

	public static UnlockDomainServiceRequest from(DistributedUnLockCommand request) {
		return ObjectMapperBasedVoMapper.convert(request, UnlockDomainServiceRequest.class);
	}
}
