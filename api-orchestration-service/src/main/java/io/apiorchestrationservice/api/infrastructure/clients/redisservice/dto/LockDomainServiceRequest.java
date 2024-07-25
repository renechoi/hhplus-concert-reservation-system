package io.apiorchestrationservice.api.infrastructure.clients.redisservice.dto;

import io.apiorchestrationservice.api.business.dto.inport.DistributedLockCommand;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockDomainServiceRequest {
    private String lockKey;
    private long waitTime;
    private long leaseTime;
    private TimeUnit timeUnit;

    public static LockDomainServiceRequest from(DistributedLockCommand command) {
        return ObjectMapperBasedVoMapper.convert(command, LockDomainServiceRequest.class);
    }
}
