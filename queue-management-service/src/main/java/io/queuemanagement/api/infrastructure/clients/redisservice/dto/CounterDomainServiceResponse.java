package io.queuemanagement.api.infrastructure.clients.redisservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounterDomainServiceResponse {
    private String counterKey;
    private long value;
}