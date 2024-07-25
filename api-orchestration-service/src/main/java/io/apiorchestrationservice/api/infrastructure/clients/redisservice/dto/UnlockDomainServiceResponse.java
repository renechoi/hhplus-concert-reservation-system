package io.apiorchestrationservice.api.infrastructure.clients.redisservice.dto;

import io.apiorchestrationservice.api.business.dto.outport.DistributedUnLockInfo;
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
public class UnlockDomainServiceResponse {
    private String lockKey;
    private Boolean isUnlocked;

    public DistributedUnLockInfo toUnlockInfo() {
        return ObjectMapperBasedVoMapper.convert(this, DistributedUnLockInfo.class);
    }
}
