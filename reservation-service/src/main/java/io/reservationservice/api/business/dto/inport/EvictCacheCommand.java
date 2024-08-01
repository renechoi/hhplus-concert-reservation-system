package io.reservationservice.api.business.dto.inport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvictCacheCommand {
	private String cacheKey;
}
