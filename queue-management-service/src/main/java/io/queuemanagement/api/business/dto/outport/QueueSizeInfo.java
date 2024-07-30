package io.queuemanagement.api.business.dto.outport;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public record QueueSizeInfo(
     String queueName,
     long size) {

}
