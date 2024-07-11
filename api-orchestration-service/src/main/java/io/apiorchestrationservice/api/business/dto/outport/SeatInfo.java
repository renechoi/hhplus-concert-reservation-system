package io.apiorchestrationservice.api.business.dto.outport;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public record SeatInfo(Long seatId, String section, String row, String seatNumber, Boolean occupied) {}
