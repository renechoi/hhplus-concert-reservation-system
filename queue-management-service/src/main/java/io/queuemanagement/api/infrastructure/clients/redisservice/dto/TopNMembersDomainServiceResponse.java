package io.queuemanagement.api.infrastructure.clients.redisservice.dto;

import io.queuemanagement.api.business.dto.outport.TopNMembersInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopNMembersDomainServiceResponse {
    private String queueName;
    private Set<String> members;

    public TopNMembersInfo toTopNMembersInfo() {
        return new TopNMembersInfo(queueName, members);
    }
}
