package io.apiorchestrationservice.testhelpers.feignexecutor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class WaitingQueueTokenGenerateRequest extends AbstractCommonRequestInfo {

	@Schema(example = "userId")
	@NotEmpty
	private String userId;

	@Schema(example = "0")
	private int priority; // 우선순위 (예: 1 - 일반, 2 - VIP)


}
