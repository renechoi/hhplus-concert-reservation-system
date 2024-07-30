package io.reservationservice.api.business.service.impl;

import static io.reservationservice.api.business.domainentity.TemporalReservation.*;
import static io.reservationservice.api.business.dto.inport.ReservationSearchCommand.*;
import static io.reservationservice.api.business.dto.inport.SeatSearchCommand.*;
import static io.reservationservice.api.business.dto.inport.TemporalReservationSearchCommand.*;
import static java.time.LocalDateTime.*;
import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.api.business.domainentity.TemporalReservation;
import io.reservationservice.api.business.dto.inport.ReservationCreateCommand;
import io.reservationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.reservationservice.api.business.dto.outport.ReservationStatusInfo;
import io.reservationservice.api.business.dto.outport.ReservationStatusInfos;
import io.reservationservice.api.business.dto.outport.TemporalReservationCreateInfo;
import io.reservationservice.api.business.persistence.ConcertOptionRepository;
import io.reservationservice.api.business.persistence.ReservationRepository;
import io.reservationservice.api.business.persistence.SeatRepository;
import io.reservationservice.api.business.persistence.TemporalReservationRepository;
import io.reservationservice.api.business.service.ReservationCrudService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Service
@RequiredArgsConstructor
public class SimpleReservationCrudService implements ReservationCrudService {

	private final ReservationRepository reservationRepository;
	private final TemporalReservationRepository temporalReservationRepository;
	private final ConcertOptionRepository concertOptionRepository;
	private final SeatRepository seatRepository;

	/**
	 * 임시 예약을 생성합니다.
	 * <p>
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
	public TemporalReservationCreateInfo createTemporalReservation(ReservationCreateCommand command) {
		ConcertOption concertOption = concertOptionRepository.findByIdWithSLock(command.getConcertOptionId()); // read / write 분리를 고려한 s lock
		Seat seat = seatRepository.findSingleByConditionWithLock(onConcertOptionSeat(concertOption, command.getSeatNumber()));
		seatRepository.save(seat.doReserve());
		return TemporalReservationCreateInfo.from(temporalReservationRepository.save(create(command, concertOption, seat)));
	}

	/**
	 * 임시 예약을 확정된 예약으로 변환
	 *
	 * @param temporalReservationId 임시 예약 ID
	 * @return 확정된 예약 엔티티
	 */
	@Override
	@Transactional
	public ReservationConfirmInfo confirmReservation(Long temporalReservationId) {
		TemporalReservation temporalReservation = temporalReservationRepository.findById(temporalReservationId);
		temporalReservation.confirm();
		return ReservationConfirmInfo.from(reservationRepository.save(temporalReservation.toConfirmedReservation()));
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(key = "#userId" + "-" + "#concertOptionId", value= "reservationStatus")
	public ReservationStatusInfos getReservationStatus(Long userId, Long concertOptionId) {
		List<Reservation> reservations = reservationRepository.findMultipleByCondition(onMatchingReservation(userId, concertOptionId));

		List<TemporalReservation> temporalReservations = temporalReservationRepository.findMultipleByCondtion(onMatchingTemporalReservaion(userId, concertOptionId));

		return Stream.concat(reservations.stream().map(ReservationStatusInfo::from), temporalReservations.stream().map(ReservationStatusInfo::from))
			.collect(collectingAndThen(toList(), ReservationStatusInfos::new))
			.withValidated();
	}

	/**
	 * 임시 예약 취소
	 *
	 * @param temporalReservationId 임시 예약 ID
	 */
	@Override
	@Transactional
	public void cancelTemporalReservation(Long temporalReservationId) {
		TemporalReservation temporalReservation = temporalReservationRepository.findById(temporalReservationId);
		temporalReservation.cancel();
	}

	@Override
	@Transactional
	public void cancelExpiredTemporalReservations() {
		temporalReservationRepository.findMultipleByCondtion(expireAt(now())).forEach(TemporalReservation::expire);
	}

}
