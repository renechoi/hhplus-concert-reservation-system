

# 낙관 락 사용에서 데드락 이슈 트러블 슈팅 

## 데드락 발생 상황

두 개의 요청이 같은 시점에 동일한 좌석에 접근하고 이를 업데이트하려 하는 동시성 상황에서 낙관 락을 이용하여 제어하였다.
이때 데드락이 발생하는 상황이다. 


#### 코드 분석
```java
@Override
@Transactional
public TemporalReservationCreateInfo createTemporalReservation(ReservationCreateCommand command) {
    ConcertOption concertOption = concertOptionRepository.findById(command.getConcertOptionId());
    Seat seat = seatRepository.findSingleByCondition(onConcertOptionSeat(concertOption, command.getSeatNumber()));
    seatRepository.save(seat.doReserve());
    return TemporalReservationCreateInfo.from(temporalReservationRepository.save(create(command, concertOption, seat)));
}
```

`Seat` 엔티티는 `version` 필드를 사용하여 낙관 락을 적용하고 있다.
각 트랜잭션은 `Seat`을 조회한 후, 해당 `Seat`의 상태를 업데이트하고 버전을 증가시킨다. 
하지만, 두 트랜잭션이 동시에 동일한 `Seat`를 조회하고, 업데이트하려고 할 때 충돌이 발생한다.

아래는 데드락이 발생한 상황을 보여주는 로그입니다.

#### 로그 분석
```
02:11:56.257 [INFO ] [http-nio-auto-1-exec-3] - [71840de5] --> Request: POST /reservation/api/reservations Body: {"concertOptionId":1,"seatNumber":1,"userId":60859472}
02:11:56.257 [INFO ] [http-nio-auto-1-exec-4] - [4e70616f] --> Request: POST /reservation/api/reservations Body: {"concertOptionId":1,"seatNumber":1,"userId":26044833}
02:11:56.291 [INFO ] [http-nio-auto-1-exec-3] - SeatQueryDslCustomRepositoryImpl.findSingleByCondition time=22ms
02:11:56.291 [INFO ] [http-nio-auto-1-exec-4] - SeatQueryDslCustomRepositoryImpl.findSingleByCondition time=22ms
02:11:56.293 [INFO ] [http-nio-auto-1-exec-3] - CrudRepository.save time=1ms
02:11:56.293 [INFO ] [http-nio-auto-1-exec-4] - CrudRepository.save time=1ms
02:11:56.316 [ERROR] [http-nio-auto-1-exec-4] - Deadlock found when trying to get lock; try restarting transaction
```


동시에 두 개의 트랜잭션이 동일한 좌석(Seat)을 예약하려고 시도하면서 데드락이 발생했다. 
`02:11:56.257`에 두 요청이 접수되었고, `02:11:56.291`에 두 트랜잭션이 좌석을 조회했다. 
이후 두 트랜잭션 모두 좌석을 저장하려고 시도했으나, `02:11:56.316`에 하나의 트랜잭션에서 데드락 오류가 발생했다.


## 데드락 발생 원인

먼저 엔티티의 관계를 살펴보면 다음과 같다. 각 엔티티는 `Jpa` 애노테이션을 이용한 직접 참조 방식을 사용하고 있다. 

#### 엔티티 관계

- **`Seat` 엔티티**
  `Seat` 엔티티는 특정 콘서트 옵션(`ConcertOption`)과 연관되어 있으며, 좌석의 상태(예약 여부)를 관리한다. `@ManyToOne` 관계를 통해 `ConcertOption` 엔티티와 연결되어 있고, 좌석 번호와 예약 여부(`occupied` 필드)를 가지고 있다. 

- **`ConcertOption` 엔티티**
  `ConcertOption` 엔티티는 여러 좌석(`Seat`)과 연관되어 있으며, 콘서트의 세부 정보를 포함한다. `@ManyToOne` 관계를 통해 `Concert` 엔티티와 연결되어 있다. 

- **`TemporalReservation` 엔티티**
  `TemporalReservation` 엔티티는 특정 좌석(`Seat`)과 콘서트 옵션(`ConcertOption`)에 대한 임시 예약 정보를 관리한다. `@ManyToOne` 관계를 통해 `Seat`와 `ConcertOption` 엔티티와 연결되어 있다.


#### 데드락 타임라인 및 관계 분석
1. **동시 요청**
    - `Transaction1`과 `Transaction2`가 동시에 동일한 `Seat`에 대해 예약 요청을 보낸다.

2. **락 획득**
    - `Transaction1`과 `Transaction2`는 각각 `Seat`을 불러오고, 이를 업데이트하기 위해 `ConcertOption` 행에 S-Lock을 건다.
    - `Seat`와 `ConcertOption`은 서로 연관된 엔티티로, 한 엔티티에 대한 작업이 다른 엔티티에도 영향을 미친다.

3. **업데이트 시도**
    - `Transaction1`과 `Transaction2`는 각각 `Seat` 업데이트를 위해 S-Lock을 X-Lock으로 올리려 한다.
    - 이 과정에서 두 트랜잭션이 서로의 S-Lock을 기다리면서 데드락이 발생한다.

4. **데드락 발생**
    - `Transaction1`이 `Seat`의 버전을 올리기 위해 X-Lock을 요청할 때, `Transaction2`가 이미 해당 `ConcertOption`에 대해 S-Lock을 보유하고 있어 X-Lock으로 전환되지 않는다.
    - 반대로, `Transaction2`도 X-Lock을 요청하지만, `Transaction1`이 해당 `ConcertOption`에 S-Lock을 보유하고 있어 전환되지 않는다.

#### 타임라인 요약
1. `02:11:56.257`: 두 요청이 동시에 `ReservationController.createTemporalReservation` 메서드를 호출
2. `02:11:56.265`: 두 요청이 `ConcertOptionCoreRepository.findById`를 호출하여 동일한 `ConcertOption`을 조회하고 S-Lock을 획득
3. `02:11:56.268`: 두 요청이 `SeatCoreRepository.findSingleByCondition`를 호출하여 동일한 `Seat`을 조회하고 S-Lock을 획득
4. `02:11:56.291`: 두 요청이 각각 `Seat` 업데이트를 시도하여 S-Lock을 X-Lock으로 전환하려고 함
5. `02:11:56.316`: `Transaction1`과 `Transaction2`가 서로의 S-Lock을 대기하면서 데드락 발생

### 분석 내용 종합

결론적으로 데드락 발생 원인을 다음과 같이 요약할 수 있다. 

동시에 여러 개의 스레드가 동일한 `Seat`를 업데이트하려고 할 때, `Seat`이 참조하는 `ConcertOption`에 S-Lock을 건다. 
`Seat`을 업데이트하여 버전을 올리기 위해 S-Lock을 X-Lock으로 전환하려 할 때, 다른 스레드가 이미 동일한 `ConcertOption`에 S-Lock을 걸고 있어 데드락이 발생한다.





## 해결 

데드락 문제를 해결하기 위해 `Seat` 엔티티에 비관 락을 사용하고, `ConcertOption` 엔티티에는 공유 락(S-Lock)을 적용하는 방식으로 해결할 수 있다. 

단순 순차성을 이용한 것이다. 비관 락을 사용하면 트랜잭션이 자원을 독점하여 다른 트랜잭션이 접근하지 못하게 하므로 데드락이 발생하지 않는다.

단, 이때 주의할 점은 `ConcertOption` 까지 베타 락으로 걸면 안 된다는 점이다. 

`ConcertOption`에 대해서는 S-Lock을 건다. 

`ConcertOption`은 주로 조회(read) 작업이 많고, 여러 트랜잭션이 동시에 읽어도 문제가 없다. S-Lock은 이러한 읽기 작업이 동시에 수행되도록 허용한다.
그러나 읽기 전용으로서의 락이 필요한 이유는 `ConcertOption` 엔티티의 변경이 `Seat` 엔티티에 영향을 미치기 때문이다.
예를 들어, `Seat` 에 대한 수정이 발생하는 시점에 `ConcertOption` 수정이 발생한다면 정합성이 틀어지게 된다. 
따라서 `ConcertOption`에 대해서는 읽기 전용으로 조회할 수 있도록 S-Lock을 건다. 

```java
	@Override
	@Transactional
	public TemporalReservationCreateInfo createTemporalReservation(ReservationCreateCommand command) {
		ConcertOption concertOption = concertOptionRepository.findByIdWithSLock(command.getConcertOptionId()); // read / write 분리를 고려한 s lock
		Seat seat = seatRepository.findSingleByConditionWithLock(onConcertOptionSeat(concertOption, command.getSeatNumber()));
		seatRepository.save(seat.doReserve());
		return TemporalReservationCreateInfo.from(temporalReservationRepository.save(create(command, concertOption, seat)));
	}
```


데드락 문제를 해결한 후, 트랜잭션이 정상적으로 처리되는 로그를 확인할 수 있다. 
첫 번째 트랜잭션은 정상적으로 좌석 예약을 완료하고 응답을 반환한다. 
두 번째 트랜잭션은 `seat already reserved` 예외를 발생시키며, 의도한 대로 동일 좌석에 대해 중복 예약을 방지한다. 
이로써 비관 락과 공유 락(S-Lock)을 사용한 동시성 제어가 기대한 대로 작동함을 확인할 수 있다.

```
09:50:35.100 [INFO ] [http-nio-auto-1-exec-1] [i.r.c.logtrace.ThreadLocalLogTracer] - [b3c75a53] |<--ReservationController.createTemporalReservation(..) time=116ms
09:50:35.102 [INFO ] [http-nio-auto-1-exec-1] [i.r.c.l.filter.RequestResponseLogger] - [b2bf800b] <-- Response: status=201 duration=122ms
09:50:35.102 [INFO ] [http-nio-auto-1-exec-1] [i.r.c.l.filter.RequestResponseLogger] - Response Body: {"temporalReservationId":1,"userId":83950468,"concertOption":{"concertOptionId":1,"concert":{"concertId":1,"title":"콘서트 제목","createdAt":"2024-07-25T09:50:34.548138","requestAt":"2024-07-25T09:50:33.897129"},"concertDate":"2024-07-10T20:00:00","concertDuration":"PT2H","title":"옵션 제목","description":"옵션 설명","price":10000.00,"createdAt":"2024-07-25T09:50:34.709638","requestAt":"2024-07-25T09:50:34.640156"},"seat":{"seatId":49,"section":null,"seatRow":null,"seatNumber":"49","occupied":true,"createdAt":"2024-07-25T09:50:34.826365"},"isConfirmed":false,"reserveAt":"2024-07-25T09:50:35.04436","createdAt":"2024-07-25T09:50:35.048743","requestAt":null}
HTTP/1.1 201 
// ... 
09:50:35.237 [INFO ] [http-nio-auto-1-exec-7] [i.r.c.logtrace.ThreadLocalLogTracer] - [a2b72846] |   |   |   |<--SeatCoreRepository.findSingleByConditionWithLock(..) time=241ms
09:50:35.237 [INFO ] [http-nio-auto-1-exec-7] [i.r.c.logtrace.ThreadLocalLogTracer] - [a2b72846] |   |   |<X-SimpleReservationCrudService.createTemporalReservation(..) time=249ms ex=io.reservationservice.common.exception.definitions.ReservationUnAvailableException: seat already reserved
09:50:35.241 [ERROR] [http-nio-auto-1-exec-10] [i.r.c.e.a.ApiControllerAdvice] - ServerException: seat already reserved
09:50:35.242 [INFO ] [http-nio-auto-1-exec-10] [i.r.c.l.filter.RequestResponseLogger] - [aff0cfbf] <-- Response: status=400 duration=261ms
09:50:35.242 [INFO ] [http-nio-auto-1-exec-10] [i.r.c.l.filter.RequestResponseLogger] - Response Body: 
HTTP/1.1 400 
```