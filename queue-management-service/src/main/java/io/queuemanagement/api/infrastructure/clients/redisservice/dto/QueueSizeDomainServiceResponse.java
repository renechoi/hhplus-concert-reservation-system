package io.queuemanagement.api.infrastructure.clients.redisservice.dto;

import io.queuemanagement.api.business.dto.outport.QueueSizeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueSizeDomainServiceResponse {
    private String queueName;
    private long size;

    public QueueSizeInfo toQueueSizeInfo() {
        return new QueueSizeInfo(queueName, size);
    }
}
