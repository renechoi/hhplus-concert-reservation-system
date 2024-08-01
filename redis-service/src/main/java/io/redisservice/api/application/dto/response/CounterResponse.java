package io.redisservice.api.application.dto.response;

import io.redisservice.api.business.dto.info.CounterInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Data
@AllArgsConstructor
public class CounterResponse {
    private String counterKey;
    private long value;

    public static CounterResponse from(CounterInfo counterInfo) {
        return new CounterResponse(counterInfo.counterKey(), counterInfo.value());
    }
}
