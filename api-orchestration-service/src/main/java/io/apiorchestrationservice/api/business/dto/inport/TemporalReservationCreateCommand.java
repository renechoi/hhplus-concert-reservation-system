package io.apiorchestrationservice.api.business.dto.inport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemporalReservationCreateCommand {
    private Long userId;
    private Long concertOptionId;
    private Long seatNumber;

}
