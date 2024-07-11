package io.apiorchestrationservice.api.business.dto.outport;

import java.util.List;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record PaymentHistoryInfos(
	List<PaymentHistoryInfo> paymentHistoryInfos
) {
}
