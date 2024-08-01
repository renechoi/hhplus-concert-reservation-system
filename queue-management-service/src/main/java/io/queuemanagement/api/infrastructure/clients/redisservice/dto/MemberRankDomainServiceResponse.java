package io.queuemanagement.api.infrastructure.clients.redisservice.dto;

import io.queuemanagement.api.business.dto.outport.QueueMemberRankInfo;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
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
public class MemberRankDomainServiceResponse {
    private String queueName;
    private String member;
    private int rank;

    public QueueMemberRankInfo toQueueMemberRankInfo() {
        return ObjectMapperBasedVoMapper.convert(this, QueueMemberRankInfo.class);
    }
}