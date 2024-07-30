package io.queuemanagement.api.infrastructure.clients.redisservice.dto;

import static io.queuemanagement.api.business.domainmodel.QueueName.*;

import io.queuemanagement.api.business.domainmodel.QueueName;
import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.dto.inport.EnqueueCommand;
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
public class QueueDomainServiceRequest {
    private String queueName;
    private String member;
    private double score;

    public static QueueDomainServiceRequest from(EnqueueCommand command) {
        return ObjectMapperBasedVoMapper.convert(command, QueueDomainServiceRequest.class);
    }


    public static QueueDomainServiceRequest waitingEnqueueCommand(WaitingQueueToken token) {
        return QueueDomainServiceRequest.builder()
            .queueName(WAITING_QUEUE.getValue())
            .member(token.getTokenValue())
            .score(System.currentTimeMillis() / 1000.0)
            .build();
    }

    public static QueueDomainServiceRequest dequeRequest(WaitingQueueToken token) {
        return QueueDomainServiceRequest.builder()
            .queueName(WAITING_QUEUE.getValue())
            .member(token.getTokenValue())
            .build();
    }
}