package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.CounterCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounterRequest {
    private String counterKey;
    private long value;

    public CounterCommand toCommand() {
        return new CounterCommand(counterKey, value);
    }
}
