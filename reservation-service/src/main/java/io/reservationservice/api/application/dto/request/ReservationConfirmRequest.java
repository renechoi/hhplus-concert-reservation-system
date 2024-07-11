package io.reservationservice.api.application.dto.request;

import io.reservationservice.api.business.dto.common.AbstractCommonRequestInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationConfirmRequest extends AbstractCommonRequestInfo {
	@NotNull
	private Long temporaryReservationId;

	private Long concertOptionId; // concertoptionId와 userId만 있어도 되지 않을까?
	private Long userId;


}