package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.GetTopNMembersCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTopNMembersRequest {
    private String queueName;
    private int n;

    public GetTopNMembersCommand toCommand() {
        return new GetTopNMembersCommand(queueName, n);
    }
}