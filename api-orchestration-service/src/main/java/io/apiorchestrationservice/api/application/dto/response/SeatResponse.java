package io.apiorchestrationservice.api.application.dto.response;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public record SeatResponse(Long seatId, String section, String row, String seatNumber, Boolean occupied) {}
