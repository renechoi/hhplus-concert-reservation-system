package io.queuemanagement.api.business.dto.inport;

import io.queuemanagement.api.business.dto.common.AbstractCommonRequestInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaitingQueueTokenGenerateCommand extends AbstractCommonRequestInfo{
	private String userId;

}
