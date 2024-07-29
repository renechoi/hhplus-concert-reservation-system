package io.redisservice.api.business.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheCommand {
    private String cacheKey;
    private Object cacheValue;
    private long ttl; // Time to live in seconds
}
