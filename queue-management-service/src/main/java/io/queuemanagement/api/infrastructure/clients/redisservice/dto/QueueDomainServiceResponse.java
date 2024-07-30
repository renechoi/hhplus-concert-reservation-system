package io.queuemanagement.api.infrastructure.clients.redisservice.dto;

import static io.queuemanagement.api.business.domainmodel.QueueStatus.*;

import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.dto.outport.QueueInfo;
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
public class QueueDomainServiceResponse {
    private String queueName;
    private String member;
    private double score;
    private boolean success;

    public QueueInfo toQueueInfo() {
        return ObjectMapperBasedVoMapper.convert(this, QueueInfo.class);
    }


    public WaitingQueueToken toQueueToken() {
        return WaitingQueueToken.builder()
            .tokenValue(member)
            .status(success ? WAITING : FAIL)
            .build();
    }
}