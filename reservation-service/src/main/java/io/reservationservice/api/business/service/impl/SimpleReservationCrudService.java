package io.reservationservice.api.business.service.impl;

import static io.reservationservice.api.business.dto.inport.ReservationSearchCommand.*;
import static io.reservationservice.api.business.dto.inport.SeatSearchCommand.*;
import static io.reservationservice.api.business.dto.inport.TemporaryReservationSearchCommand.*;
import static io.reservationservice.common.model.GlobalResponseCode.*;
import static io.reservationservice.util.YmlLoader.*;
import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.api.business.domainentity.TemporaryReservation;
import io.reservationservice.api.business.dto.inport.ReservationCreateCommand;
import io.reservationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.reservationservice.api.business.dto.outport.ReservationStatusInfo;
import io.reservationservice.api.business.dto.outport.ReservationStatusInfos;
import io.reservationservice.api.business.dto.outport.TemporaryReservationCreateInfo;
import io.reservationservice.api.business.persistence.ConcertOptionRepository;
import io.reservationservice.api.business.persistence.ReservationRepository;
import io.reservationservice.api.business.persistence.SeatRepository;
import io.reservationservice.api.business.persistence.TemporaryReservationRepository;
import io.reservationservice.api.business.service.ReservationCrudService;
import io.reservationservice.common.exception.definitions.ReservationUnAvailableException;
import io.reservationservice.common.model.GlobalResponseCode;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Service
@RequiredArgsConstructor
public class SimpleReservationCrudService implements ReservationCrudService {

	private final ReservationRepository reservationRepository;
	private final TemporaryReservationRepository temporaryReservationRepository;
	private final ConcertOptionRepository concertOptionRepository;
	private final SeatRepository seatRepository;

	/**
	 * 임시 예약을 생성합니다.
	 *
	 * 1. concertOption을 찾습니다.
	 * 2. 좌석을 생성하거나 이미 존재하는 좌석을 사용합니다.
	 * 3. 해당 유저가 맞는지 확인하고, 좌석의 점유 여부를 확인합니다. (user 부분은 아직 구현 x)
	 * 4. 좌석이 사용 가능한 상태라면, 임시 예약을 생성합니다.
	 *
	 * @param command 예약 생성 명령 객체
	 * @return 생성된 임시 예약 정보
	 */
	@Override
	@Transactional
	public TemporaryReservationCreateInfo createTemporaryReservation(ReservationCreateCommand command) {
		ConcertOption concertOption = concertOptionRepository.findByIdWithThrows(command.getConcertOptionId());

		Seat seat = seatRepository.findSingleByConditionWithThrows(searchByConcertOptionAndSeatNumber(concertOption, command.getSeatNumber()));

		// todo -> seat에서 userId 반정규화 필요성?
		if( seat.isOccupied()){
			throw new ReservationUnAvailableException(SEAT_ALREADY_RESERVED);
		}

		seatRepository.save(seat.doReserve());

		LocalDateTime expireAt = now().plusSeconds(ymlLoader().getTemporaryReservationExpireSeconds());

		return TemporaryReservationCreateInfo.from(temporaryReservationRepository.save(TemporaryReservation.of(command, concertOption, seat, expireAt )));
	}



	/**
	 * 임시 예약을 확정된 예약으로 변환
	 *
	 * @param temporaryReservationId 임시 예약 ID
	 * @return 확정된 예약 엔티티
	 */
	@Override
	@Transactional
	public ReservationConfirmInfo confirmReservation(Long temporaryReservationId) {
		TemporaryReservation temporaryReservation = temporaryReservationRepository.findByIdWithThrows(temporaryReservationId);
		temporaryReservation.confirm();

		return ReservationConfirmInfo.from(reservationRepository.save(temporaryReservation.toConfirmedReservation()));
	}




	@Override
	@Transactional(readOnly = true)
	public ReservationStatusInfos getReservationStatus(Long userId, Long concertOptionId) {
		List<Reservation> reservations = reservationRepository.findMultipleByCondition(searchByUserIdAndConcertOptionId(userId, concertOptionId));

		List<TemporaryReservation> temporaryReservations = temporaryReservationRepository
			.findMultipleByCondition(searchTemporaryReservationByUserIdAndConcertOptionId(userId, concertOptionId));

		List<ReservationStatusInfo> statusResponses = Stream.concat(
			reservations.stream().map(ReservationStatusInfo::from),
			temporaryReservations.stream().map(ReservationStatusInfo::from)
		).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(statusResponses)) {
			throw new ReservationUnAvailableException(GlobalResponseCode.RESERVATION_RETRIEVAL_NO_CONTENT);
		}

		return new ReservationStatusInfos(statusResponses);
	}



	/**
	 * 임시 예약 취소
	 *
	 * @param temporaryReservationId 임시 예약 ID
	 */
	@Override
	@Transactional
	public void cancelTemporaryReservation(Long temporaryReservationId) {
		TemporaryReservation temporaryReservation = temporaryReservationRepository.findByIdWithThrows(temporaryReservationId);
		temporaryReservation.doCancelSeat();
		temporaryReservation.cancelConfirm();
	}

	@Override
	@Transactional
	public void cancelExpiredTemporalReservations() {
		temporaryReservationRepository.findMultipleByCondition(searchByExpireAt(now(), "before"))
		.forEach(temporaryReservation -> {
			temporaryReservation.doCancelSeat();
			temporaryReservation.cancelConfirm();
		});
	}

}
