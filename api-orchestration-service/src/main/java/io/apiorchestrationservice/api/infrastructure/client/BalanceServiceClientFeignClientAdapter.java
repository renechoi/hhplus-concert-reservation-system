package io.apiorchestrationservice.api.infrastructure.client;

import io.apiorchestrationservice.api.business.client.BalanceServiceClientAdapter;
import io.apiorchestrationservice.common.annotation.FeignAdapter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@FeignAdapter
@RequiredArgsConstructor
public class BalanceServiceClientFeignClientAdapter implements BalanceServiceClientAdapter {
	private final BalanceServiceClient balanceServiceClient;
}
