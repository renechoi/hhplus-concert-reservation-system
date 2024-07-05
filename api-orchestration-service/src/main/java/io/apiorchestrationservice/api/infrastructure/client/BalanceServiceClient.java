package io.apiorchestrationservice.api.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@FeignClient(
	name = "balance-service",
	url= "${custom.feign.direct.host:}",
	path = "/balance-service",
	value = "balance-service"

)
public interface BalanceServiceClient  {
}
