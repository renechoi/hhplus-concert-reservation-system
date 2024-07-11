package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.util.List;

import io.apiorchestrationservice.api.business.dto.outport.AvailableSeatInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record AvailableSeatsInfos(List<AvailableSeatInfo> availableSeatInfos) {
}
