package io.redisservice.api.business.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
@Data
@AllArgsConstructor
public class GetTopNMembersCommand {
    private String queueName;
    private int n;
}
