package io.apiorchestrationservice.api.application.dto.response;

import java.time.LocalDate;
import java.util.List;

import io.apiorchestrationservice.api.business.dto.outport.SeatInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */

public record AvailableSeatsResponse(Long concertId, LocalDate date, List<SeatInfo> availableSeats) {}

