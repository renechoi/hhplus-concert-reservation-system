package io.apiorchestrationservice.testhelpers.feignexecutor.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author : Rene Choi
 * @since : 2024/07/05
 */
@Data
@SuperBuilder
public class AbstractCommonRequestInfo{

	@NotNull
	private LocalDateTime requestAt;
}
