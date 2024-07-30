package io.apiorchestrationservice.api.infrastructure.clients.redisservice.dto;

import io.apiorchestrationservice.api.business.dto.outport.DistributedLockInfo;
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
public class LockDomainServiceResponse {
    private String lockKey;
    private Long waitTime;
    private Long leaseTime;
    private Boolean isLocked;

    public DistributedLockInfo toLockInfo() {
        return ObjectMapperBasedVoMapper.convert(this, DistributedLockInfo.class);
    }
}
