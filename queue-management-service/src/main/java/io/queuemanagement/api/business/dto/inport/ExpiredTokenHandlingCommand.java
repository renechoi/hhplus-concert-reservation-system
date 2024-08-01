package io.queuemanagement.api.business.dto.inport;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/08/01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpiredTokenHandlingCommand {
	private List<String> keys;
	private int size;
}
