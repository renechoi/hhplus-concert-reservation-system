package io.queuemanagement.api.application.dto.request;

import io.queuemanagement.api.business.dto.common.AbstractCommonRequestInfo;
import io.queuemanagement.api.business.dto.inport.WaitingQueueTokenGenerateCommand;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaitingQueueTokenGenerateRequest extends AbstractCommonRequestInfo {

	@Schema(example = "userId")
	@NotEmpty
	private String userId;

	@Schema(example = "0", hidden = true)
	private int priority; // 우선순위 (예: 1 - 일반, 2 - VIP)

	@Schema(example = "1", hidden = true)
	private Long waitingQueueId; // 대기열 구분이 필요한 경우 사용 - currently not use

	public WaitingQueueTokenGenerateCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, WaitingQueueTokenGenerateCommand.class);
	}
}
