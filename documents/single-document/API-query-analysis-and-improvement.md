

<details>
<summary><b>API 쿼리 분석 및 개선</b></summary>

 
# 예약 및 콘서트 API 


1. 예약 가능 날짜 조회: `GET ` - `/reservation/api/availability/dates/{concertId}`
2. 예약 가능 좌석 조회: `GET` - `/reservation/api/availability/seats/{concertOptionId}/{requestAt}`
3. 예약 요청: `POST` - `/api/reservations`
4. 임시 예약 확정: `PUT` - `/api/reservations/confirm`
4. 예약 상태 조회: `GET` - `/api/reservations/status/{reservationId}`
5. 콘서트 생성: `POST` - `/api/concerts`
6. 콘서트 옵션 생성: `POST` - `/api/concerts/{concertId}/options`



## AS-IS 사용되는 쿼리


##### 예약 가능 날짜 조회 

- 설명: 사용자가 특정 콘서트의 예약 가능 날짜를 확인하기 위해 사용된다. ConcertOption 테이블과 concert 테이블에서 concertId와 concertDate 조건에 맞는 데이터를 조회한다.
- 빈도: ★★★★★ 
  - 사용자가 예약 가능 날짜를 조회할 때마다 호출되므로 자주 사용될 수 있다.
- 복잡도: ★★★☆☆ 
  - 두 개의 테이블을 조회하며, 날짜와 ID 조건을 사용하여 필터링한다.
  - 특히, concertId를 기준으로 ConcertOption 테이블과 Concert 테이블을 조인한다.

**쿼리 1:** `Seat` 테이블에서 `concertOptionId`로 특정 콘서트 옵션에 해당하는 좌석을 조회하는 쿼리
```sql
SELECT
         co1_0.concert_option_id,
         co1_0.concert_id,
         co1_0.concert_date,
         co1_0.concert_duration,
         co1_0.created_at,
         co1_0.description,
         co1_0.price,
         co1_0.request_at,
         co1_0.title
      FROM
         concert_option co1_0
      WHERE
         co1_0.concert_option_id IS NOT NULL
        AND co1_0.concert_id = ?
        AND co1_0.concert_date > ?
```

**쿼리 2:** `ConcertOption` 테이블에서 `concertOptionId`로 특정 콘서트 옵션과 관련된 콘서트 정보를 조회하는 쿼리
```sql
select
    c1_0.concert_id,
    c1_0.created_at,
    c1_0.request_at,
    c1_0.title 
from
    concert c1_0 
where
    c1_0.concert_id=?
```

##### 예약 가능 좌석 조회
- **설명:** 특정 콘서트 옵션에 대해 예약 가능한 좌석을 조회한다. `Seat` 테이블과 `ConcertOption` 테이블을 조인하여 `concertOptionId`와 `requestAt` 조건에 맞는 데이터를 가져온다.
- **빈도:** ★★★★☆
    - 예약 가능한 좌석 정보를 조회할 때마다 호출되며, 사용 빈도가 높다.
- **복잡도:** ★★★☆☆
    - 좌석의 예약 가능 여부를 필터링해야 하므로 단순한 조회보다 복잡하다. 좌석이 예약되었는지 여부를 확인하기 위해 `occupied` 필드를 검사하며, 또한 `concertOptionId`를 기준으로 `Seat` 테이블과 `ConcertOption` 테이블을 조인하여 관련 데이터를 가져온다. 이러한 과정에서 여러 조건을 사용하여 데이터를 필터링하고, 조인 연산을 통해 테이블 간의 관계를 처리해야 한다. 특히, 좌석의 예약 가능 상태를 정확히 판별하기 위해 `requestAt` 매개변수를 사용하여 요청 시점에서의 예약 가능 상태를 확인한다. 
    - 따라서 단순한 조회 쿼리보다 더 많은 조건과 연산을 포함하게 되어 복잡도가 증가한다.

```sql
select
        seat 
from
     Seat seat 
where
     seat is not null 
     and seat.concertOption.concertOptionId = ?
```

```sql
select
    co1_0.concert_option_id,
    c1_0.concert_id,
    c1_0.created_at,
    c1_0.request_at,
    c1_0.title,
    co1_0.concert_date,
    co1_0.concert_duration,
    co1_0.created_at,
    co1_0.description,
    co1_0.price,
    co1_0.request_at,
    co1_0.title 
from
    concert_option co1_0 
left join
    concert c1_0 
        on c1_0.concert_id=co1_0.concert_id 
where
    co1_0.concert_option_id=?
```

##### 예약 요청

- **설명:** 특정 콘서트 옵션에 대한 좌석 정보를 조회하고, 좌석 예약을 처리한다. `ConcertOption` 테이블과 `Seat` 테이블을 조인하여 필요한 데이터를 가져오고, 필요한 경우 임시 예약 테이블에 데이터를 삽입하거나 기존 데이터를 업데이트한다.

- **빈도:** ★★★★☆
    - 예약 요청은 사용자가 특정 콘서트의 좌석을 예약할 때마다 발생하므로 자주 사용된다.
    - 다만 1명당 거의 1:1로 대응되는 만큼 발생 빈도수는 한정적이다.

- **복잡도:** ★★★★☆
    - 여러 테이블을 조회하고, 데이터를 삽입하거나 업데이트한다. 좌석의 예약 가능 여부를 확인하고, 임시 예약을 처리하는 과정에서 동시성 제어와 데이터 무결성을 유지해야 한다. 또한, 여러 조건을 만족하는 데이터를 조회하고, 필요한 경우 행 잠금을 사용하여 동시성 문제를 해결한다.


**쿼리 1:** `ConcertOption` 테이블에서 `concertOptionId`로 조회하는 쿼리.
```sql
select concertOption
from ConcertOption concertOption
where concertOption.concertOptionId = ?1

select co1_0.concert_option_id,
     co1_0.concert_id,
     co1_0.concert_date,
     co1_0.concert_duration,
     co1_0.created_at,
     co1_0.description,
     co1_0.price,
     co1_0.request_at,
     co1_0.title
from concert_option co1_0
where co1_0.concert_option_id=?
for share
```

**쿼리 2:** `Seat` 테이블에서 특정 조건을 만족하는 좌석 데이터를 조회하는 쿼리.
```sql
select seat
from Seat seat
where seat is not null
and seat.concertOption = ?1
and seat.seatNumber = ?2

select s1_0.seat_id,
     s1_0.concert_option_id,
     s1_0.created_at,
     s1_0.occupied,
     s1_0.seat_number
from seat s1_0
where s1_0.seat_id is not null
and s1_0.concert_option_id=?
and s1_0.seat_number=?
for update
```

**쿼리 3:** `TemporalReservation` 테이블에 새로운 임시 예약 데이터를 삽입하는 쿼리.
```sql
insert into temporal_reservation (concert_option_id, created_at, expire_at, is_canceled, is_confirmed, request_at, reserve_at, seat_id, user_id)
values (?, ?, ?, ?, ?, ?, ?, ?, ?)
```

**쿼리 4:** `Seat` 테이블에서 특정 좌석의 상태를 업데이트하는 쿼리.
```sql
update seat
set concert_option_id=?,
  occupied=?,
  seat_number=?
where seat_id=?
```



##### 임시 예약 확정

- **설명:** 임시 예약 확정을 처리하는 쿼리들로, 사용자가 임시로 예약한 좌석을 확정된 예약으로 전환하는 작업을 수행한다. `temporal_reservation`, `concert_option`, `concert`, `seat` 테이블을 조회하여 필요한 정보를 가져오고, 임시 예약을 확정하거나 새로운 예약을 삽입한다.

- **빈도:** ★★★☆☆
    - 임시 예약 확정은 사용자가 예약을 최종적으로 확정할 때마다 발생하는 중요한 작업이므로 비즈니스가 수행된다는 가정하에 반드시 n개에 데이터만큼 발생한다.
    - 다만 1명당 거의 1:1로 대응되는 만큼 발생 빈도수는 한정적이다.

- **복잡도:** ★★★★★
    - 이 쿼리들은 여러 테이블 간의 조인과 데이터 삽입 및 업데이트 작업을 포함하며, 특히 동시성 제어와 데이터 무결성을 유지해야 한다. 임시 예약을 확정하는 과정에서 여러 조건을 만족해야 하며, 데이터의 일관성을 유지하기 위해 행 잠금을 사용하여 동시성 문제를 해결한다. 이러한 작업은 높은 복잡도를 가진다.

**쿼리 1:** 임시 예약과 관련된 정보를 조회하는 쿼리로, 임시 예약 ID와 연결된 콘서트 옵션, 콘서트, 좌석의 상세 정보를 가져온다. 
```sql
select
  tr1_0.temporal_reservation_id,
  co1_0.concert_option_id,
  c1_0.concert_id,
  c1_0.created_at,
  c1_0.request_at,
  c1_0.title,
  co1_0.concert_date,
  co1_0.concert_duration,
  co1_0.created_at,
  co1_0.description,
  co1_0.price,
  co1_0.request_at,
  co1_0.title,
  tr1_0.created_at,
  tr1_0.expire_at,
  tr1_0.is_canceled,
  tr1_0.is_confirmed,
  tr1_0.request_at,
  tr1_0.reserve_at,
  s1_0.seat_id,
  co2_0.concert_option_id,
  co2_0.concert_id,
  co2_0.concert_date,
  co2_0.concert_duration,
  co2_0.created_at,
  co2_0.description,
  co2_0.price,
  co2_0.request_at,
  co2_0.title,
  s1_0.created_at,
  s1_0.occupied,
  s1_0.seat_number,
  tr1_0.user_id 
from
  temporal_reservation tr1_0 
left join
  concert_option co1_0 
      on co1_0.concert_option_id=tr1_0.concert_option_id 
left join
  concert c1_0 
      on c1_0.concert_id=co1_0.concert_id 
left join
  seat s1_0 
      on s1_0.seat_id=tr1_0.seat_id 
left join
  concert_option co2_0 
      on co2_0.concert_option_id=s1_0.concert_option_id 
where
  tr1_0.temporal_reservation_id=?
```

**쿼리 2:** 새로운 예약 데이터를 `reservation` 테이블에 삽입하는 쿼리로, 임시 예약 데이터를 기반으로 실제 예약을 생성한다.
```sql
insert into
  reservation (concert_option_id, created_at, is_canceled, request_at, reserve_at, seat_id, temporal_reservation_reserve_at, user_id) 
values
  (?, ?, ?, ?, ?, ?, ?, ?)
```

**쿼리 3:** 기존 임시 예약 데이터를 업데이트하는 쿼리로, 임시 예약의 상태를 확정된 상태로 변경한다.
```sql
update temporal_reservation 
set
  concert_option_id=?,
  expire_at=?,
  is_canceled=?,
  is_confirmed=?,
  request_at=?,
  reserve_at=?,
  seat_id=?,
  user_id=? 
where
  temporal_reservation_id=?
```

##### 예약 상태 조회

- **설명:** 특정 사용자와 콘서트 옵션에 대한 예약 정보를 조회하며, 예약된 좌석의 세부 정보를 가져온다. `Reservation`, `ConcertOption`, `Seat`, 그리고 `TemporalReservation` 테이블을 사용하여 예약 및 임시 예약 상태를 조회한다.

- **빈도:** ★★★★☆
    - 예약 상태 조회는 사용자가 자신의 예약 현황을 확인할 때마다 발생하며, 특정 이벤트 전후에 빈번하게 사용될 것이다.

- **복잡도:** ★★★★☆
    - 이 쿼리들은 여러 테이블을 조인하여 데이터를 조회하고, 사용자와 콘서트 옵션에 대한 다양한 조건을 만족해야 한다. 또한, 데이터 무결성을 유지하고 최신 상태를 반영해야 하므로 복잡도가 높다.


**쿼리 1:** `Reservation` 테이블에서 특정 사용자와 콘서트 옵션에 대한 예약 데이터를 조회하는 쿼리.
```sql
select
  reservation1 
from
  reservation reservation1 
where
  reservation1 is not null 
  and reservation1.userId = ?1 
  and reservation1.concertOption.concertOptionId = ?2

select
  r1_0.reservation_id,
  r1_0.concert_option_id,
  r1_0.created_at,
  r1_0.is_canceled,
  r1_0.request_at,
  r1_0.reserve_at,
  r1_0.seat_id,
  r1_0.temporal_reservation_reserve_at,
  r1_0.user_id 
from
  reservation1 r1_0 
where
  r1_0.reservation_id is not null 
  and r1_0.user_id=? 
  and r1_0.concert_option_id=?
```

**쿼리 2:** `ConcertOption` 테이블에서 특정 콘서트 옵션에 대한 세부 정보를 조회하는 쿼리.
```sql
select
  co1_0.concert_option_id,
  c1_0.concert_id,
  c1_0.created_at,
  c1_0.request_at,
  c1_0.title,
  co1_0.concert_date,
  co1_0.concert_duration,
  co1_0.created_at,
  co1_0.description,
  co1_0.price,
  co1_0.request_at,
  co1_0.title 
from
  concert_option co1_0 
left join
  concert c1_0 
      on c1_0.concert_id=co1_0.concert_id 
where
  co1_0.concert_option_id=?
```

**쿼리 3:** `Seat` 테이블에서 특정 좌석에 대한 정보를 조회하는 쿼리이다.
```sql
select
  s1_0.seat_id,
  co1_0.concert_option_id,
  c1_0.concert_id,
  c1_0.created_at,
  c1_0.request_at,
  c1_0.title,
  co1_0.concert_date,
  co1_0.concert_duration,
  co1_0.created_at,
  co1_0.description,
  co1_0.price,
  co1_0.request_at,
  co1_0.title,
  s1_0.created_at,
  s1_0.occupied,
  s1_0.seat_number 
from
  seat s1_0 
left join
  concert_option co1_0 
      on co1_0.concert_option_id=s1_0.concert_option_id 
left join
  concert c1_0 
      on c1_0.concert_id=co1_0.concert_id 
where
  s1_0.seat_id=?
```

**쿼리 4:** `TemporalReservation` 테이블에서 특정 사용자와 콘서트 옵션에 대한 임시 예약 데이터를 조회하는 쿼리.
```sql
select
  temporalReservation 
from
  TemporalReservation temporalReservation 
where
  temporalReservation is not null 
  and temporalReservation.userId = ?1 
  and temporalReservation.concertOption.concertOptionId = ?2
   select
     tr1_0.temporal_reservation_id,
     tr1_0.concert_option_id,
     tr1_0.created_at,
     tr1_0.expire_at,
     tr1_0.is_canceled,
     tr1_0.is_confirmed,
     tr1_0.request_at,
     tr1_0.reserve_at,
     tr1_0.seat_id,
     tr1_0.user_id 
   from
     temporal_reservation tr1_0 
   where
     tr1_0.temporal_reservation_id is not null 
     and tr1_0.user_id=? 
     and tr1_0.concert_option_id=?
```



##### 콘서트 생성

- **설명:** `concert` 테이블에 새로운 콘서트를 삽입한다. 

- **빈도:** ★☆☆☆☆
    - 콘서트 생성 쿼리는 관리자나 운영자가 새로운 콘서트를 등록할 때 사용되며, 일반 사용자가 자주 호출하지는 않는다. 따라서 빈도는 낮다.

- **복잡도:** ★☆☆☆☆
    - 단일 테이블에 데이터를 삽입하는 단순한 구조로, 입력 값의 유효성만 검증하면 되므로 복잡도는 낮다.

```sql
insert for
        io.reservationservice.api.business.domainentity.Concert 
    into
        concert (created_at, request_at, title) 
    values
        (?, ?, ?)
```

##### 콘서트 옵션 생성

- **설명:** 특정 콘서트에 대한 다양한 옵션을 설정하고 저장하며, 각 콘서트 옵션에 대한 세부 정보를 데이터베이스에 추가한다. 또한, 콘서트 옵션과 관련된 좌석 정보를 함께 저장하여 사용자가 선택할 수 있도록 한다.

- **빈도:** ★★★☆☆
    - 콘서트 옵션 생성은 새로운 콘서트가 추가될 때마다 발생하며, 이벤트나 프로모션을 기획할 때 사용된다. 빈도는 상대적으로 낮지만, 이벤트 계획 시 집중적으로 발생할 수 있다.

- **복잡도:** ★★★★☆
    - 이 쿼리들은 여러 테이블에 데이터를 삽입해야 하며, 콘서트 옵션과 관련된 다양한 정보를 처리한다. 특히, 좌석 정보와 관련된 테이블을 함께 업데이트해야 하므로 복잡도가 높다.

**쿼리 1:** `concert` 테이블에서 콘서트 ID로 콘서트를 조회하는 쿼리.
```sql
select
    c1_0.concert_id,
    c1_0.created_at,
    c1_0.request_at,
    c1_0.title 
from
    concert c1_0 
where
    c1_0.concert_id=?
```

**쿼리 2:** `concert_option` 테이블에 새로운 콘서트 옵션을 삽입하는 쿼리.
```sql
insert into
    concert_option (concert_id, concert_date, concert_duration, created_at, description, price, request_at, title) 
values
    (?, ?, ?, ?, ?, ?, ?, ?)
```

**쿼리 3:** `seat` 테이블에 새로운 좌석 정보를 삽입하는 쿼리.
```sql
insert into
    seat (concert_option_id, created_at, occupied, seat_number) 
values
    (?, ?, ?, ?)
```


## 인덱스 적절성 판단 및 설정

### Concert 

현재 API 분석 결과, Concert 엔티티에 대한 검색 조건이 존재하지 않기 때문에 인덱스는 불필요하다. 

다만, Concert 엔티티의 특성을 고려할 때, 향후 검색 요구가 발생할 가능성을 대비해볼 수 있다. 
Concert 엔티티는 생성 빈도가 낮아 인덱스 생성이 큰 부담이 되지 않기 때문에 이와 같은 접근이 가능하다.

1. **title 컬럼 인덱스**:
   - **필요성**: Concert의 제목으로 검색하는 경우가 발생할 수 있다. title은 중복 값이 적고, 검색 성능 향상에 기여할 수 있다. 
   - **카디널리티**: 중간 (제목은 비교적 중복이 적음)
   - **변경 빈도**: 낮음 (한번 설정된 제목은 자주 변경되지 않음)

2. **createdAt**
   - **필요성**: 생성 날짜를 기준으로 하는 쿼리의 성능을 향상시키기 위해 필요하다.
   - **카디널리티**: 중간에서 높음 (시간 단위에 따라 다르지만, 일반적으로 많은 고유 값을 가짐)
   - **변경 빈도**: 낮음 (생성된 후 변경되지 않음)
   

##### 종합 결론

따라서, 현재 검색 조건이 없더라도 `title`과 `createdAt` 컬럼에 인덱스를 설정하여 향후 검색 요구에 대비할 수 있다.

```java
@Table(indexes = {
    @Index(name = "idx_concert_title", columnList = "title"),
    @Index(name = "idx_concert_createdAt", columnList = "createdAt")
})
public class Concert implements EntityRecordable{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long concertId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime requestAt;
}
```




### ConcertOption

#### 단일 인덱스 검토

1. **concertId**
   - **필요성**: 대부분의 쿼리에서 concertId를 사용하여 ConcertOption 테이블을 조회한다. 특히 `예약 가능 날짜 조회`와 `예약 가능 좌석 조회`에서 자주 사용된다. ConcertOption이 특정 콘서트와 연관된 데이터를 필터링할 때 가장 중요한 컬럼이다.
   - **카디널리티**: 중간 (여러 ConcertOption이 동일한 concertId를 가질 수 있음)
   - **변경 빈도**: 낮음 (콘서트가 생성된 후 변경되지 않음)
   
2. **concertDate**
   - **필요성**: `예약 가능 날짜 조회`에서 자주 사용된다. 사용자가 특정 콘서트의 예약 가능 날짜를 확인할 때 매우 중요하다. 날짜 범위로 조회되는 경우가 많다.
   - **카디널리티**: 중간에서 높음 (여러 ConcertOption이 동일한 concertDate를 가질 수 있으나, 특정 날짜에 여러 고유 값이 존재할 수 있음)
   - **변경 빈도**: 낮음 (콘서트 일정이 정해진 후 변경되지 않음)

3. **concertOptionId**
   - **필요성**: 각 콘서트 옵션을 식별하기 위해 자주 사용된다. `예약 요청`, `임시 예약 확정`, `예약 상태 조회` 등에서 사용된다.
   - **카디널리티**: 높음 (각 콘서트 옵션마다 고유 값)
   - **변경 빈도**: 낮음 (생성 후 변경되지 않음)

#### 복합 인덱스 검토

1. **concertId, concertDate**
   - **필요성**: `예약 가능 날짜 조회`에서 자주 사용된다. 두 컬럼을 함께 사용하여 특정 콘서트의 예약 가능한 날짜를 조회하는 쿼리에 최적화된다.
   - **카디널리티**: 중간 (두 컬럼의 조합으로 유일한 식별자가 될 수 있음)
   - **변경 빈도**: 낮음 (두 값 모두 생성 후 변경되지 않음)
   
##### 종합 결론


1. **단일 인덱스**
    - `concertDate`
    - `concertOptionId`

2. **복합 인덱스**
    - `concertId, concertDate`


```java
@Table(
    indexes = {
        @Index(name = "idx_concert_date", columnList = "concertDate"),
        @Index(name = "idx_concert_option_id", columnList = "concertOptionId"),
        @Index(name = "idx_concert_id_date", columnList = "concert_id, concertDate"),
    }
)
public class ConcertOption implements EntityRecordable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long concertOptionId;
    @ManyToOne @JoinColumn(name = "concert_id") private Concert concert;
    private LocalDateTime concertDate;
    private Duration concertDuration;
    private String title;
    private String description;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime requestAt;
}
```


### Seat

#### 단일 인덱스 검토

1. **concertOptionId**
   - **필요성**: 대부분의 쿼리에서 `concertOptionId`를 사용하여 `Seat` 테이블을 조회한다. 특히, 예약 가능 좌석 조회와 관련된 쿼리에서 자주 사용된다.
   - **카디널리티**: 중간 (여러 좌석이 동일한 `concertOptionId`를 가질 수 있음)
   - **변경 빈도**: 낮음 (좌석이 속한 콘서트 옵션은 자주 변경되지 않음)

2. **seatNumber**
   - **필요성**: 좌석 번호를 기준으로 조회할 때 사용된다. 특정 콘서트 옵션 내에서 좌석을 식별하는 데 필요하다.
   - **카디널리티**: 중간 (각 콘서트 옵션 내에서 유일함)
   - **변경 빈도**: 낮음 (좌석 번호는 생성 후 변경되지 않음)

#### 복합 인덱스 검토

1. **concertOptionId, seatNumber**
   - **필요성**: 예약 가능 좌석 조회와 관련된 쿼리에서 `concertOptionId`와 `seatNumber`를 함께 사용하여 특정 콘서트 옵션 내의 좌석을 조회하는 데 최적화된다.
   - **카디널리티**: 중간 (두 컬럼의 조합으로 유일한 식별자가 됨)
   - **변경 빈도**: 낮음 (좌석 번호와 콘서트 옵션은 변경되지 않음)

2. **concertOptionId, occupied**
   - **필요성**: 좌석 예약 여부를 확인할 때 자주 사용된다. 특히, 특정 콘서트 옵션 내에서 예약 가능한 좌석을 조회하는 쿼리에 유용하다.
   - **카디널리티**: 중간 (여러 좌석이 동일한 `concertOptionId`와 `occupied` 값을 가질 수 있음)
   - **변경 빈도**: 중간 (좌석의 예약 상태는 자주 변경될 수 있음)

##### 종합 결론


1. **단일 인덱스**
    - `concertOptionId`
    - `seatNumber`

2. **복합 인덱스**
    - `concertOptionId, seatNumber`
    - `concertOptionId, occupied`

- **예약 가능 좌석 조회** 쿼리는 `concertOptionId`와 `seatNumber`를 조건으로 사용하여 특정 콘서트 옵션 내의 좌석을 조회하기 때문에 이 두 컬럼에 대한 복합 인덱스가 필요하다.
- **좌석 예약 여부 확인** 쿼리는 `concertOptionId`와 `occupied` 컬럼을 자주 사용하므로 이들에 대한 복합 인덱스가 유용하다.
- 단일 인덱스는 `concertOptionId`와 `seatNumber`에 설정하여 기본적인 조회 성능을 향상시킬 수 있다. 다만, `concertOption`에 대한 복합 인덱스가 이미 존재하므로 `concertOption`에 대한 단일 인덱스는 불필요하다.


```java
@Table(
    indexes = {
        @Index(name = "idx_seat_number", columnList = "seatNumber"),
        @Index(name = "idx_concert_option_seat_number", columnList = "concertOptionId, seatNumber"),
        @Index(name = "idx_concert_option_occupied", columnList = "concertOptionId, occupied"),
    }
)
public class Seat extends AbstractAggregateRoot<Seat> {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long seatId;
    @ManyToOne @JoinColumn(name = "concert_option_id") private ConcertOption concertOption;
    private Long seatNumber;
    private Boolean occupied;
    private LocalDateTime createdAt;
}
```


### TemporalReservation
#### 단일 인덱스 검토

1. **userId**
   - **필요성**: 사용자와 관련된 예약을 조회할 때 자주 사용된다. 예를 들어, 사용자의 예약 현황을 확인하는 쿼리에서 많이 사용된다.
   - **카디널리티**: 높음 (사용자 ID는 유니크함)
   - **변경 빈도**: 낮음 (예약 생성 시 설정되며, 이후 거의 변경되지 않음)

2. **concertOptionId**
   - **필요성**: 특정 콘서트 옵션에 대한 예약을 조회할 때 자주 사용된다. 콘서트 옵션에 대한 예약 내역을 확인하는 데 중요한 컬럼이다.
   - **카디널리티**: 중간 (여러 예약에서 중복될 수 있음)
   - **변경 빈도**: 낮음 (예약 생성 시 설정되며, 이후 잘 변경되지 않음)
   
3. **reserveAt**
   - **필요성**: 예약 시간을 기준으로 정렬하거나 조회할 때 사용된다. 특히, 특정 시간대의 예약 내역을 확인하는 데 유용하다.
   - **카디널리티**: 중간에서 높음 (시간 단위에 따라 다르지만, 일반적으로 많은 고유 값을 가짐)
   - **변경 빈도**: 낮음 (예약 생성 시 설정되며, 이후 변경되지 않음)

4. **isConfirmed**
   - **필요성**: 예약 확정 여부를 조회할 때 사용된다. 예약 확정 상태를 필터링하는 데 자주 사용된다.
   - **카디널리티**: 낮음 (이진 값)
   - **변경 빈도**: 낮음 (예약 상태가 변경될 때만 업데이트됨)

5. **isCanceled**
   - **필요성**: 예약 취소 여부를 조회할 때 사용된다. 취소된 예약을 필터링하거나 확인하는 데 사용된다. 
   - **카디널리티**: 낮음 (이진 값)
   - **변경 빈도**: 낮음 (예약 상태가 변경될 때만 업데이트됨)

#### 복합 인덱스 검토

1. **userId, concertOptionId**
   - **필요성**: 사용자의 특정 콘서트 옵션에 대한 예약을 조회할 때 최적화된다. 사용자가 특정 콘서트에 대해 예약한 내역을 빠르게 찾을 수 있다.
   - **카디널리티**: 높음 (두 컬럼의 조합으로 유니크한 식별자 가능)
   - **변경 빈도**: 낮음 (예약 생성 시 설정되며, 이후 거의 변경되지 않음)

2. **concertOptionId, reserveAt**
   - **필요성**: 특정 콘서트 옵션의 예약 내역을 시간 순으로 정렬하여 조회할 때 유용하다. 예약 시각을 기준으로 예약 내역을 빠르게 찾을 수 있다.
   - **카디널리티**: 중간 (시간의 정밀도에 따라 다름, range 검색 고려)
   - **변경 빈도**: 낮음 (예약 생성 시 설정되며, 이후 잘 변경되지 않음)
   
##### 종합 결론


1. **단일 인덱스**
    - `userId`
    - `concertOptionId`

2. **복합 인덱스**
    - `userId, concertOptionId`
    - `concertOptionId, reserveAt`


- **사용자 예약 현황 조회** 쿼리는 `userId`와 `concertOptionId`를 조건으로 사용하여 특정 사용자와 콘서트 옵션에 대한 예약 내역을 조회하기 때문에 이 두 컬럼에 대한 복합 인덱스가 필요하다.
- **예약 내역 시간 순 조회** 쿼리는 `concertOptionId`와 `reserveAt`를 조건으로 사용하여 특정 콘서트 옵션의 예약 내역을 시간 순으로 조회하기 때문에 이 두 컬럼에 대한 복합 인덱스가 유용하다.
- 단일 인덱스는 `userId`와 `concertOptionId`에 설정하여 기본적인 조회 성능을 향상시킬 수 있다. 다만, 두 필드 모두 복합 인덱스로 사용되고 있으므로, 단일 인덱스는 제외한다. 



```java
@Table(
    indexes = {
        @Index(name = "idx_user_concert_option", columnList = "userId, concertOptionId"),
        @Index(name = "idx_concert_option_reserve_at", columnList = "concertOptionId, reserveAt")
    }
)
public class TemporalReservation extends AbstractAggregateRoot<TemporalReservation> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long temporalReservationId;
    private Long userId;
    private ConcertOption concertOption;
    private Seat seat;
    private Boolean isConfirmed;
    private LocalDateTime reserveAt;
    private LocalDateTime expireAt;
    private LocalDateTime createdAt;
    private LocalDateTime requestAt;
    private Boolean isCanceled;
}
```




### Reservation


##### 단일 인덱스 검토


1. **userId**
   - **필요성**: 사용자와 관련된 모든 예약을 조회하는 데 사용된다. 사용자의 예약 내역을 확인할 때 자주 사용된다.
   - **카디널리티**: 높음 (각 사용자마다 고유 값)
   - **변경 빈도**: 낮음 (예약 생성 시 설정되며 이후 거의 변경되지 않음)

2. **concertOptionId**
   - **필요성**: 특정 콘서트 옵션에 대한 모든 예약을 조회하는 데 사용된다. 예약 가능 좌석 조회, 예약 요청 처리 등에서 빈번히 사용된다.
   - **카디널리티**: 중간 (여러 예약이 동일한 `concertOptionId`를 가질 수 있음)
   - **변경 빈도**: 낮음 (예약 생성 시 설정되며 이후 거의 변경되지 않음)

##### 복합 인덱스 검토

1. **userId, concertOptionId**
   - **필요성**: 특정 사용자가 특정 콘서트 옵션에 대해 예약한 내역을 조회할 때 유용하다. 예약 내역 조회, 예약 상태 확인에서 빈번히 사용된다.
   - **카디널리티**: 중간 (두 컬럼의 조합으로 유일한 식별자가 될 가능성이 높음)
   - **변경 빈도**: 낮음 (예약 생성 시 설정되며 이후 거의 변경되지 않음)

2. **concertOptionId, seatId**
   - **필요성**: 특정 콘서트 옵션 내에서 특정 좌석에 대한 예약 내역을 조회할 때 유용하다. 예약 가능 좌석 조회, 좌석 예약 여부 확인에 유용하다.
   - **카디널리티**: 중간 (두 컬럼의 조합으로 유일한 식별자가 됨)
   - **변경 빈도**: 낮음 (예약 생성 시 설정되며 이후 거의 변경되지 않음)


##### 종합 결론

```java
@Table(
    indexes = {
        @Index(name = "idx_user_concert_option", columnList = "userId, concertOptionId"),
        @Index(name = "idx_concert_option_seat", columnList = "concertOptionId, seatId"),
    }
)
public class Reservation extends AbstractAggregateRoot<Reservation> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long reservationId;
    private Long userId;
	@ManyToOne @JoinColumn(name = "concert_option_id") private ConcertOption concertOption;
    @ManyToOne @JoinColumn(name = "seat_id") private Seat seat;
    private Boolean isCanceled;
    private LocalDateTime temporalReservationReserveAt;
    private LocalDateTime reserveAt;
    private LocalDateTime createdAt;
    private LocalDateTime requestAt;
}
```




## BEFORE & AFTER 

인덱스 적용 전과 후를 비교한다. 


### 예약 가능 날짜 조회 API

**`Seat` 테이블에서 `concertOptionId`로 특정 콘서트 옵션에 해당하는 좌석을 조회하는 쿼리**

```sql
EXPLAIN ANALYZE
SELECT
    co1_0.concert_option_id,
    co1_0.concert_id,
    co1_0.concert_date,
    co1_0.concert_duration,
    co1_0.created_at,
    co1_0.description,
    co1_0.price,
    co1_0.request_at,
    co1_0.title
FROM
    concert_option co1_0
WHERE
    co1_0.concert_option_id IS NOT NULL
  AND co1_0.concert_id = 123
  AND co1_0.concert_date > '2024-01-01'
```

**before**

```sql
1,SIMPLE,co1_0,,ALL,,,,,992058,3.33,Using where
```

```sql
-> Filter: ((co1_0.concert_id = 123) and (co1_0.concert_date > TIMESTAMP'2024-01-01 00:00:00'))  (cost=107117 rows=33065) (actual time=1.52..2063 rows=100 loops=1)
    -> Table scan on co1_0  (cost=107117 rows=992058) (actual time=1.43..1998 rows=1e+6 loops=1)
```

**after**

```sql
1,SIMPLE,co1_0,,range,"idx_concert_date,idx_concert_id_date",idx_concert_id_date,18,,100,100,Using index condition
```


```sql
-> Index range scan on co1_0 using idx_concert_id_date over (concert_id = 123 AND '2024-01-01 00:00:00.000000' < concert_date), with index condition: ((co1_0.concert_id = 123) and (co1_0.concert_date > TIMESTAMP'2024-01-01 00:00:00'))  (cost=119 rows=100) (actual time=0.916..28.9 rows=100 loops=1)
```
##### 결과 분석

| 항목              | Before                                                                                                      | After                                                                                                            |
|-------------------|-------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| **인덱스 사용**   | 없음 (`FULL` 스캔)                                                                                         | `idx_concert_id_date` 인덱스를 사용하여 `concert_id`와 `concert_date`로 인덱스 룩업 수행                          |
| **필터링 단계**   | 테이블 스캔 후 `concert_id`와 `concert_date`에 대한 필터링 수행                                              | 인덱스 룩업 단계에서 `concert_id`와 `concert_date` 조건을 만족하는 레코드를 효율적으로 검색                       |
| **비용**          | 107117                                                                                                      | 119                                                                                                              |
| **예상 행 수**    | 992058                                                                                                      | 100                                                                                                              |
| **실제 실행 시간**| 1.52..2063 ms                                                                                               | 0.916..28.9 ms                                                                                                   |
| **실제 처리된 행 수**| 1e+6                                                                                                       | 100                                                                                                              |

Before의 쿼리는 인덱스를 사용하지 않고 전체 테이블을 스캔하여 데이터를 필터링하기 때문에 실행 시간이 길고 비용이 높다. After의 쿼리는 적절한 인덱스를 사용하여 `concert_id`와 `concert_date`에 대한 조건을 만족하는 데이터를 효율적으로 검색하여 실행 시간을 대폭 단축시킨다.


**`ConcertOption` 테이블에서 `concertOptionId`로 특정 콘서트 옵션과 관련된 콘서트 정보를 조회하는 쿼리**
```sql
EXPLAIN ANALYZE
SELECT
    c1_0.concert_id,
    c1_0.created_at,
    c1_0.request_at,
    c1_0.title 
FROM
    concert c1_0 
WHERE
    c1_0.concert_id = 123
```

**before**

```sql
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=250e-6..333e-6 rows=1 loops=1)
```

**after**

```sql
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=167e-6..250e-6 rows=1 loops=1)
```

##### 결과 분석

큰 변화 없음 


### 예약 가능 좌석 조회

**Seat 테이블에서 concert_option_id로 특정 콘서트 옵션에 해당하는 좌석을 조회하는 쿼리**
```sql
EXPLAIN ANALYZE
SELECT
   s.seat_id,
   s.concert_option_id,
   s.created_at,
   s.occupied,
   s.seat_number
FROM
   seat s
WHERE
   s.concert_option_id = 123;
```

**before**
```sql
1,SIMPLE,s,,ALL,,,,,996761,10,Using where
```


```sql
-> Filter: (s.concert_option_id = 123)  (cost=103169 rows=99676) (actual time=28.2..1983 rows=1 loops=1)
    -> Table scan on s  (cost=103169 rows=996761) (actual time=4.96..1896 rows=1.01e+6 loops=1)
```

**after**

```sql
1,SIMPLE,s,,ref,"idx_concert_option_seat_number,idx_concert_option_occupied",idx_concert_option_seat_number,9,const,1,100,
```

```sql
-> Index lookup on s using idx_concert_option_seat_number (concert_option_id=123)  (cost=1.1 rows=1) (actual time=0.534..0.539 rows=1 loops=1)
```

##### 결과 분석

| 항목              | Before                                                                                                 | After                                                                                                     |
|-------------------|--------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| **인덱스 사용**   | 없음 (`FULL` 스캔)                                                                                     | `idx_concert_option_seat_number` 인덱스를 사용하여 `concert_option_id`로 인덱스 룩업 수행                  |
| **필터링 단계**   | 테이블 스캔 후 `concert_option_id`에 대한 필터링 수행                                                  | 인덱스 룩업 단계에서 `concert_option_id` 조건을 만족하는 레코드를 효율적으로 검색                          |
| **비용**          | 103169                                                                                                 | 1.1                                                                                                       |
| **예상 행 수**    | 996761                                                                                                 | 1                                                                                                         |
| **실제 실행 시간**| 4.96..1896 ms                                                                                          | 0.534..0.539 ms                                                                                           |
| **실제 처리된 행 수**| 1.01e+6                                                                                                 | 1                                                                                                         |

Before의 쿼리는 인덱스를 사용하지 않고 전체 테이블을 스캔하여 데이터를 필터링하기 때문에 실행 시간이 길고 비용이 높다. 
After의 쿼리는 적절한 인덱스를 사용하여 `concert_option_id`에 대한 조건을 만족하는 데이터를 효율적으로 검색하여 실행 시간을 대폭 단축시킨다.


**ConcertOption 테이블과 Concert 테이블을 조인하여 concert_option_id로 특정 콘서트 옵션과 관련된 콘서트 정보를 조회하는 쿼리**
```sql
EXPLAIN ANALYZE
select
    co1_0.concert_option_id,
    c1_0.concert_id,
    c1_0.created_at,
    c1_0.request_at,
    c1_0.title,
    co1_0.concert_date,
    co1_0.concert_duration,
    co1_0.created_at,
    co1_0.description,
    co1_0.price,
    co1_0.request_at,
    co1_0.title 
from
    concert_option co1_0 
left join
    concert c1_0 
        on c1_0.concert_id=co1_0.concert_id 
where
    co1_0.concert_option_id= 123
```

**before**


```sql
1,SIMPLE,co1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```


```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=83e-6..125e-6 rows=1 loops=1)
```

**after**

```sql
1,SIMPLE,co1_0,,const,"PRIMARY,idx_concert_option_id",PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```


```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=42e-6..125e-6 rows=1 loops=1)
```

##### 결과 분석

### 결과 분석

| 항목              | Before                                                                                                  | After                                                                                                   |
|-------------------|---------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| **인덱스 사용**   | `PRIMARY` 인덱스를 사용하여 `concert_option_id`와 `concert_id`로 인덱스 룩업 수행                          | `PRIMARY`와 `idx_concert_option_id` 인덱스를 사용하여 `concert_option_id`로 인덱스 룩업 수행             |
| **필터링 단계**   | 인덱스 룩업 후 `concert_option_id`에 대한 필터링을 수행                                                   | `idx_concert_option_id` 인덱스를 사용하여 `concert_option_id`에 대한 필터링을 수행                       |
| **비용**          | 0.0                                                                                                     | 0.0                                                                                                    |
| **예상 행 수**    | 1                                                                                                       | 1                                                                                                      |
| **실제 실행 시간**| 83e-6..125e-6 sec                                                                                       | 42e-6..125e-6 sec                                                                                      |
| **실제 처리된 행 수**| 1                                                                                                       | 1                                                                                                      |

인덱스 `idx_concert_option_id`를 사용하여, 필터링 단계에서 효율적으로 `concert_option_id`에 대한 조건을 만족하는 레코드를 검색하였고, 이에 따라 쿼리 실행 시간이 감소하였다. 



### 예약 요청

**`ConcertOption` 테이블에서 `concertOptionId`로 조회하는 쿼리.**
```sql
EXPLAIN ANALYZE
SELECT *
FROM concert_option concert_option
WHERE concert_option.concert_option_id = 123
    FOR SHARE;
```
**before**

```sql
1,SIMPLE,concert_option,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=84e-6..125e-6 rows=1 loops=1)
```

**after**

```sql
1,SIMPLE,concert_option,,const,"PRIMARY,idx_concert_option_id",PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=83e-6..124e-6 rows=1 loops=1)
```

##### 결과 분석

큰 차이 없음


**`Seat` 테이블에서 특정 조건을 만족하는 좌석 데이터를 조회하는 쿼리.**
```sql
EXPLAIN ANALYZE
SELECT
   s.seat_id,
   s.concert_option_id,
   s.created_at,
   s.occupied,
   s.seat_number
FROM
   seat s
WHERE
   s.concert_option_id = 123 AND s.seat_number = 1 FOR UPDATE
```

**before**

```sql
1,SIMPLE,s,,ALL,,,,,996761,1,Using where
```

```sql
-> Filter: ((s.seat_number = 1) and (s.concert_option_id = 123))  (cost=101189 rows=9968) (actual time=3932..3932 rows=0 loops=1)
    -> Table scan on s  (cost=101189 rows=996761) (actual time=14.6..3810 rows=1.01e+6 loops=1)
```

**after**

```sql
1,SIMPLE,s,,ref,"idx_seat_number,idx_concert_option_seat_number,idx_concert_option_occupied",idx_concert_option_seat_number,18,"const,const",1,100,
```


```sql
-> Index lookup on s using idx_concert_option_seat_number (concert_option_id=123, seat_number=1)  (cost=1.1 rows=1) (actual time=0.0417..0.0417 rows=0 loops=1)
```

##### 결과 분석

| 항목              | Before                                                                                                  | After                                                                                                   |
|-------------------|---------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| **인덱스 사용**   | 없음 (`FULL` 스캔)                                                                                     | `idx_concert_option_seat_number` 인덱스를 사용하여 `concert_option_id`와 `seat_number`로 인덱스 룩업 수행  |
| **필터링 단계**   | 테이블 스캔 후 `concert_option_id`와 `seat_number`에 대한 필터링 수행                                    | 인덱스 룩업 단계에서 `concert_option_id`와 `seat_number` 조건을 만족하는 레코드를 효율적으로 검색          |
| **비용**          | 101189                                                                                                  | 1.1                                                                                                      |
| **예상 행 수**    | 996761                                                                                                  | 1                                                                                                        |
| **실제 실행 시간**| 14.6..3810 ms                                                                                           | 0.0417..0.0417 ms                                                                                        |
| **실제 처리된 행 수**| 1.01e+6                                                                                                | 0                                                                                                        |

Before의 쿼리는 인덱스를 사용하지 않고 전체 테이블을 스캔하여 데이터를 필터링하기 때문에 실행 시간이 길고 비용이 높다. 
After의 쿼리는 적절한 인덱스를 사용하여 `concert_option_id`와 `seat_number`에 대한 조건을 만족하는 데이터를 효율적으로 검색하여 실행 시간을 대폭 단축시킨다.


**`Seat` 테이블에서 특정 좌석의 상태를 업데이트하는 쿼리.**
```sql
EXPLAIN 
update seat
set concert_option_id=123,
  occupied=1,
  seat_number=1
where seat_id=123
```

**before**

```sql
1,UPDATE,seat,,range,PRIMARY,PRIMARY,8,const,1,100,Using where
```

**after**

```sql
1,UPDATE,seat,,range,PRIMARY,PRIMARY,8,const,1,100,Using where
```


##### 결과 분석

큰 차이 없음




### 임시 예약 확정 


**임시 예약 ID와 연결된 콘서트 옵션, 콘서트, 좌석의 상세 정보를 가져오는 쿼리**
```sql
EXPLAIN ANALYZE
select
  tr1_0.temporal_reservation_id,
  co1_0.concert_option_id,
  c1_0.concert_id,
  c1_0.created_at,
  c1_0.request_at,
  c1_0.title,
  co1_0.concert_date,
  co1_0.concert_duration,
  co1_0.created_at,
  co1_0.description,
  co1_0.price,
  co1_0.request_at,
  co1_0.title,
  tr1_0.created_at,
  tr1_0.expire_at,
  tr1_0.is_canceled,
  tr1_0.is_confirmed,
  tr1_0.request_at,
  tr1_0.reserve_at,
  s1_0.seat_id,
  co2_0.concert_option_id,
  co2_0.concert_id,
  co2_0.concert_date,
  co2_0.concert_duration,
  co2_0.created_at,
  co2_0.description,
  co2_0.price,
  co2_0.request_at,
  co2_0.title,
  s1_0.created_at,
  s1_0.occupied,
  s1_0.seat_number,
  tr1_0.user_id 
from
  temporal_reservation tr1_0 
left join
  concert_option co1_0 
      on co1_0.concert_option_id=tr1_0.concert_option_id 
left join
  concert c1_0 
      on c1_0.concert_id=co1_0.concert_id 
left join
  seat s1_0 
      on s1_0.seat_id=tr1_0.seat_id 
left join
  concert_option co2_0 
      on co2_0.concert_option_id=s1_0.concert_option_id 
where
  tr1_0.temporal_reservation_id=123
```

**before**

```sql
1,SIMPLE,tr1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,co1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,s1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,co2_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```



```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=125e-6..167e-6 rows=1 loops=1)
```

**after**

```sql
1,SIMPLE,tr1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,co1_0,,const,"PRIMARY,idx_concert_option_id",PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,s1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,co2_0,,const,"PRIMARY,idx_concert_option_id",PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=41e-6..82e-6 rows=1 loops=1)
```


##### 결과 분석


| 항목              | Before                                                                                                  | After                                                                                                   |
|-------------------|--------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| **인덱스 사용**   | 모든 조인 테이블에서 기본 키 인덱스(`PRIMARY`) 사용                                   | `concert_option` 테이블에서 추가 인덱스(`PRIMARY, idx_concert_option_id`) 사용            |
| **필터링 단계**   | 인덱스 룩업 후 필터링 추가로 수행                                                     | 복합 인덱스 사용으로 필터링 단계에서 조건을 모두 만족하는 레코드를 효율적으로 검색                |
| **비용**          | 0..0                                                                                                         | 0..0                                                                                                           |
| **예상 행 수**    | 1                                                                                                         | 1                                                                                                           |
| **실제 실행 시간**| 125e-6..167e-6 ms                                                                                                | 41e-6..82e-6 ms                                                                                                |
| **실제 처리된 행 수**| 1                                                                                                         | 1                                                                                                           |

**분석 요약**
- **인덱스 사용**: Before에서는 모든 조인 테이블에서 기본 키 인덱스(`PRIMARY`)만 사용되었지만, After에서는 `concert_option` 테이블에서 추가 인덱스(`PRIMARY, idx_concert_option_id`)가 사용되었다. 이는 특정 조건에 대해 더 효율적인 검색이 가능하게 한다.
- **예상 행 수 및 실제 실행 시간**: 예상 행 수는 동일하게 1이었으나, 실제 실행 시간은 After에서 더 짧아졌다.



**임시 예약의 상태를 확정된 상태로 변경하는 쿼리**
```sql
EXPLAIN
UPDATE temporal_reservation
SET
   concert_option_id = 456,
   expire_at = '2024-08-06 12:00:00',
   is_canceled = false,
   is_confirmed = true,
   request_at = '2024-08-06 10:00:00',
   reserve_at = '2024-08-06 11:00:00',
   seat_id = 789,
   user_id = 123
WHERE
   temporal_reservation_id = 123
```


**before**



```sql
1,UPDATE,temporal_reservation,,range,PRIMARY,PRIMARY,8,const,1,100,Using where
```


**after**

```sql
1,UPDATE,temporal_reservation,,range,PRIMARY,PRIMARY,8,const,1,100,Using where
```

##### 결과 분석

큰 차이 없음



### 예약 상태 조회 

**`Reservation` 테이블에서 특정 사용자와 콘서트 옵션에 대한 예약 데이터를 조회하는 쿼리**
```sql
EXPLAIN
SELECT
   *
FROM
   reservation r
WHERE r.user_id = 123
  AND r.concert_option_id = 123
```

**before**

```sql
1,SIMPLE,r,,ALL,,,,,994703,1,Using where
```


```sql
-> Filter: ((r.concert_option_id = 123) and (r.user_id = 123))  (cost=105075 rows=9947) (actual time=1.62..2717 rows=1 loops=1)
    -> Table scan on r  (cost=105075 rows=994703) (actual time=1.49..2615 rows=1e+6 loops=1)
```


**after**

```sql
1,SIMPLE,r,,ref,"idx_user_concert_option,idx_concert_option_seat",idx_user_concert_option,18,"const,const",1,100,
```

```sql
-> Index lookup on r using idx_user_concert_option (user_id=123, concert_option_id=123)  (cost=1.1 rows=1) (actual time=6.85..6.85 rows=1 loops=1)
```


##### 결과 분석

| 항목                 | Before                                                                                             | After                                                                                              |
|----------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| **인덱스 사용**      | 없음 (`FULL` 스캔)                                                                                | `idx_user_concert_option` 인덱스를 사용하여 `user_id`와 `concert_option_id`로 인덱스 룩업 수행    |
| **필터링 단계**      | 테이블 스캔 후 `user_id`와 `concert_option_id`에 대한 필터링 수행                                 | 인덱스 룩업 단계에서 `user_id`와 `concert_option_id` 조건을 만족하는 레코드를 효율적으로 검색      |
| **비용**             | 105075                                                                                            | 1.1                                                                                                |
| **예상 행 수**       | 9947                                                                                              | 1                                                                                                  |
| **실제 실행 시간**   | 1.62..2717 ms                                                                                     | 6.85..6.85 ms                                                                                      |
| **실제 처리된 행 수**| 1e+6                                                                                              | 1                                                                                                  |

Before의 쿼리는 인덱스를 사용하지 않고 전체 테이블을 스캔하여 데이터를 필터링하기 때문에 실행 시간이 길고 비용이 높다. 
After의 쿼리는 적절한 인덱스를 사용하여 `user_id`와 `concert_option_id`에 대한 조건을 만족하는 데이터를 효율적으로 검색하여 실행 시간을 대폭 단축시킨다.




**`ConcertOption` 테이블에서 특정 콘서트 옵션에 대한 세부 정보를 조회하는 쿼리**
```sql
EXPLAIN ANALYZE
select
  co1_0.concert_option_id,
  c1_0.concert_id,
  c1_0.created_at,
  c1_0.request_at,
  c1_0.title,
  co1_0.concert_date,
  co1_0.concert_duration,
  co1_0.created_at,
  co1_0.description,
  co1_0.price,
  co1_0.request_at,
  co1_0.title 
from
  concert_option co1_0 
left join
  concert c1_0 
      on c1_0.concert_id=co1_0.concert_id 
where
  co1_0.concert_option_id= 123
```


**before**

```sql
1,SIMPLE,co1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=84e-6..126e-6 rows=1 loops=1)
```

**after**

```sql
1,SIMPLE,co1_0,,const,"PRIMARY,idx_concert_option_id",PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=125e-6..167e-6 rows=1 loops=1)
```

##### 결과 분석


| 항목                | Before                                                                                     | After                                                                                      |
|---------------------|--------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| **인덱스 사용**     | `PRIMARY` 인덱스만 사용하여 `concert_option_id`로 인덱스 룩업 수행                         | `PRIMARY` 및 `idx_concert_option_id` 인덱스를 사용하여 `concert_option_id`로 인덱스 룩업 수행 |
| **필터링 단계**     | 인덱스 룩업 후 추가적인 필터링 없음                                                        | 인덱스 룩업 후 추가적인 필터링 없음                                                        |
| **비용**            | 0..0                                                                                       | 0..0                                                                                       |
| **예상 행 수**      | 1                                                                                          | 1                                                                                          |
| **실제 실행 시간**  | 84e-6..126e-6 ms                                                                           | 125e-6..167e-6 ms                                                                          |
| **실제 처리된 행 수**| 1                                                                                          | 1                                                                                          |

두 쿼리 모두 `concert_option_id`에 대해 `PRIMARY` 인덱스를 사용하여 효율적으로 레코드를 조회하고 있다. 
쿼리 실행 전 후의 인덱스 사용 방식은 동일하나, `after` 쿼리에서는 추가로 `idx_concert_option_id` 인덱스를 사용하였다. 
실제 실행 시간에 소폭의 차이가 발생했다. 
전체적으로 인덱스 사용과 필터링 단계에서 큰 변화가 없고, 비용 및 예상 행 수 또한 동일하다. 




**`Seat` 테이블에서 특정 좌석에 대한 정보를 조회하는 쿼리.**
```sql
EXPLAIN ANALYZE
select
  s1_0.seat_id,
  co1_0.concert_option_id,
  c1_0.concert_id,
  c1_0.created_at,
  c1_0.request_at,
  c1_0.title,
  co1_0.concert_date,
  co1_0.concert_duration,
  co1_0.created_at,
  co1_0.description,
  co1_0.price,
  co1_0.request_at,
  co1_0.title,
  s1_0.created_at,
  s1_0.occupied,
  s1_0.seat_number 
from
  seat s1_0 
left join
  concert_option co1_0 
      on co1_0.concert_option_id=s1_0.concert_option_id 
left join
  concert c1_0 
      on c1_0.concert_id=co1_0.concert_id 
where
  s1_0.seat_id=123
```

**before**

```sql
1,SIMPLE,s1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,co1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=84e-6..126e-6 rows=1 loops=1)
```

**after**

```sql
1,SIMPLE,s1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,co1_0,,const,"PRIMARY,idx_concert_option_id",PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=42e-6..83e-6 rows=1 loops=1)
```

##### 결과 분석


| 항목              | Before                                                                                                  | After                                                                                                   |
|-------------------|--------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| **인덱스 사용**   | `PRIMARY` 인덱스를 사용하여 `concert_option_id`와 `concert_id`로 인덱스 룩업 수행                                   | `PRIMARY` 및 `idx_concert_option_id` 복합 인덱스를 사용하여 `concert_option_id`와 `concert_id`에 대한 인덱스 범위 스캔 수행            |
| **비용**          | 0..0                                                                                                         | 0..0                                                                                                           |
| **예상 행 수**    | 1                                                                                                         | 1                                                                                                           |
| **실제 실행 시간**| 84e-6..126e-6 초                                                                                                | 42e-6..83e-6 초                                                                                                |
| **실제 처리된 행 수**| 1                                                                                                         | 1                                                                                                           |

인덱스 사용 방식에 일부 변화가 있다. `Before`에서는 `concert_option_id`와 `concert_id`에 대해 단일 인덱스 `PRIMARY`를 사용하여 조회했고, `After`에서는 `concert_option_id`와 `concert_id`에 대해 복합 인덱스 `idx_concert_option_id`를 추가적으로 사용했다. 비용과 예상 행 수는 동일하게 유지되었으나, 실제 실행 시간은 소폭 감소했다.



**`TemporalReservation` 테이블에서 특정 사용자와 콘서트 옵션에 대한 임시 예약 데이터를 조회하는 쿼리.**
```sql
EXPLAIN
select
   *
from
   temporal_reservation t
where t.user_id = 123
  and t.concert_option_id = 123
```


**before**


```sql
1,SIMPLE,t,,ALL,,,,,994525,1,Using where
```


```sql
-> Filter: ((t.concert_option_id = 123) and (t.user_id = 123))  (cost=105186 rows=9945) (actual time=1.28..1616 rows=1 loops=1)
    -> Table scan on t  (cost=105186 rows=994525) (actual time=1.19..1530 rows=1e+6 loops=1)
```

**after**


```sql
1,SIMPLE,t,,ref,"idx_user_concert_option,idx_concert_option_reserve_at",idx_user_concert_option,18,"const,const",1,100,
```

```sql
-> Index lookup on t using idx_user_concert_option (user_id=123, concert_option_id=123)  (cost=1.1 rows=1) (actual time=0.337..0.345 rows=1 loops=1)
```

##### 결과 분석


| 항목              | Before                                                                                                  | After                                                                                                   |
|-------------------|---------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| **인덱스 사용**   | 없음 (`FULL` 스캔)                                                                                     | `idx_user_concert_option` 인덱스를 사용하여 `user_id`와 `concert_option_id`로 인덱스 룩업 수행             |
| **필터링 단계**   | 테이블 스캔 후 `user_id`와 `concert_option_id`에 대한 필터링 수행                                        | 인덱스 룩업 단계에서 `user_id`와 `concert_option_id` 조건을 만족하는 레코드를 효율적으로 검색                 |
| **비용**          | 105186                                                                                                  | 1.1                                                                                                      |
| **예상 행 수**    | 994525                                                                                                  | 1                                                                                                        |
| **실제 실행 시간**| 1.28..1616 ms                                                                                           | 0.337..0.345 ms                                                                                          |
| **실제 처리된 행 수**| 1e+6                                                                                                     | 1                                                                                                        |

Before의 쿼리는 인덱스를 사용하지 않고 전체 테이블을 스캔하여 데이터를 필터링하기 때문에 실행 시간이 길고 비용이 높다. 
After의 쿼리는 적절한 인덱스를 사용하여 `user_id`와 `concert_option_id`에 대한 조건을 만족하는 데이터를 효율적으로 검색하여 실행 시간을 대폭 단축시킨다. 




# 잔액 및 결제 API  

1. 잔액 조회: `GET` - `/api/user-balance/{userId}`
2. 잔액 충전/사용 내역 조회: `GET` - `/api/user-balance/histories/{userId}`
3. 잔액 충전: `PUT` - `/api/user-balance/charge/{userId}`
4. 잔액 사용: `PUT` - `/api/user-balance/use/{userId}`
5. 결제 처리: `POST` - `/api/user-balance/payment`
6. 결제 취소: `POST` - `/api/user-balance/payment/cancel/{transactionId}`
7. 결제 내역 조회: `GET` - `/api/user-balance/payment/history/{userId}`






## AS-IS 사용되는 쿼리

##### 잔액 조회 


- **설명:** 잔액 조회는 특정 사용자의 현재 잔액 정보를 조회하는 쿼리이다. 이 쿼리는 `balance` 테이블에서 사용자의 잔액 관련 데이터를 가져온다. 사용자 ID를 조건으로 하여 해당 사용자의 잔액 정보를 반환한다. 반환되는 정보는 잔액 ID, 금액, 생성일자, 수정일자, 사용자 ID 등이다.

- **빈도:** ★★★☆☆
    - 잔액 조회는 사용자가 자신의 잔액을 확인할 때마다 발생하지만, 잔액 사용이나 충전과 같은 다른 주요 활동에 비해 상대적으로 빈도가 낮을 수 있다.

- **복잡도:** ★☆☆☆☆
    - 이 쿼리는 단일 테이블에서 데이터를 조회하며, 간단한 조건(`user_id`)만을 사용하기 때문에 복잡도가 낮다.


**쿼리 1:** `Balance` 테이블에서 `userId`로 특정 사용자의 잔액 정보를 조회하는 쿼리
```sql
select
    b1_0.balance_id,
    b1_0.amount,
    b1_0.created_at,
    b1_0.updated_at,
    b1_0.user_id 
from
    balance b1_0 
where
    b1_0.user_id=?
```

##### 잔액 충전/사용 내역 조회

- **설명:** 특정 사용자의 잔액 충전 및 사용 내역을 조회하는 쿼리이다. 이 쿼리는 `balance_transaction` 테이블을 사용하여 사용자의 거래 ID, 금액, 생성 시간, 거래 사유, 거래 유형, 사용자 ID 및 버전 정보를 가져온다. 주로 사용자의 잔액 내역을 확인하거나 재무 분석에 사용된다.

- **빈도:** ★★★☆☆
    - 잔액 충전/사용 내역 조회는 사용자가 자신의 거래 내역을 확인하거나 관리자가 특정 사용자의 잔액 변동 상황을 파악할 때 주로 사용된다. 빈도는 상대적으로 평균적이며, 이벤트나 프로모션 기간 동안 증가할 수 있다.

- **복잡도:** ★★☆☆☆
    - 이 쿼리는 단일 테이블을 대상으로 하며, 사용자 ID에 기반한 단순한 조건을 사용한다. 조인이 없고 데이터 무결성을 유지하는 복잡한 로직이 포함되지 않아 비교적 단순하다.


**쿼리 2:** `BalanceTransaction` 테이블에서 `userId`로 특정 사용자의 잔액 거래 내역을 조회하는 쿼리
```sql
select
        bt1_0.transaction_id,
        bt1_0.amount,
        bt1_0.created_at,
        bt1_0.transaction_reason,
        bt1_0.transaction_type,
        bt1_0.user_id,
        bt1_0.version 
    from
        balance_transaction bt1_0 
    where
        bt1_0.user_id=?
```

##### 잔액 충전

- **설명:** 이 섹션의 쿼리는 사용자의 잔액을 조회, 충전, 그리고 업데이트하는 데 사용된다. `Balance` 및 `BalanceTransaction` 테이블을 사용하여 사용자의 잔액 상태를 관리하며, 트랜잭션의 무결성을 유지하기 위해 동시성 제어를 포함한다. `select` 쿼리는 사용자의 잔액을 조회하고, `insert` 쿼리는 잔액 충전 트랜잭션을 기록하며, `update` 쿼리는 사용자의 잔액을 업데이트한다.

- **빈도:** ★★★★☆
    - 잔액 조회, 충전, 업데이트는 사용자 활동에 따라 빈번하게 발생한다. 특히 잔액 조회는 사용자가 자신의 잔액을 확인할 때 자주 발생하며, 충전과 업데이트는 결제나 서비스 이용 시 주로 사용된다.

- **복잡도:** ★★★☆☆
    - 쿼리들은 단일 테이블을 대상으로 하며, 비교적 단순한 조건을 사용하여 데이터를 조회, 삽입, 업데이트한다. 그러나 동시성 제어를 위한 `for update` 및 `optimistic locking`의 사용으로 인해 복잡도가 증가할 수 있다.


**쿼리 1:** `Balance` 테이블에서 특정 사용자의 잔액을 조회하는 쿼리.
```sql
select
    balance 
from
    Balance balance 
where
    balance is not null 
    and balance.userId = ?1

select
    b1_0.balance_id,
    b1_0.amount,
    b1_0.created_at,
    b1_0.updated_at,
    b1_0.user_id 
from
    balance b1_0 
where
    b1_0.balance_id is not null 
    and b1_0.user_id=? for update
```

**쿼리 2:** `BalanceTransaction` 테이블에 새로운 잔액 충전 트랜잭션을 기록하는 쿼리.
```sql
insert into
    balance_transaction (amount, created_at, transaction_reason, transaction_type, user_id, version) 
values
    (?, ?, ?, ?, ?, ?)
```

**쿼리 3:** `Balance` 테이블에서 사용자의 잔액을 업데이트하는 쿼리.
```sql
update balance 
set
    amount=?,
    updated_at=?,
    user_id=? 
where
    balance_id=?
```


### 잔액 사용

- **설명:** 잔액 사용과 관련된 쿼리이다. 이 쿼리들은 사용자의 잔액 조회, 잔액 사용 내역 삽입, 그리고 잔액 업데이트 작업을 수행한다. `Balance` 테이블에서 사용자의 현재 잔액을 조회하고, `BalanceTransaction` 테이블에 잔액 사용 내역을 기록하며, `Balance` 테이블의 잔액을 업데이트한다. 이러한 쿼리들은 트랜잭션의 일관성을 보장하기 위해 `for update` 구문을 사용하여 동시성 문제를 방지한다.

- **빈도:** ★★★☆☆
    - 잔액 사용 쿼리는 사용자가 결제를 하거나 잔액을 사용할 때마다 발생한다. 결제 시스템이 자주 사용되는 서비스라면 빈도가 높아질 수 있다. 그러나 일반적인 사용 패턴에서는 조회 쿼리에 비해 빈도가 다소 낮다.

- **복잡도:** ★★★☆☆
    - 잔액 사용 쿼리는 비교적 간단한 구조로, 단일 테이블에 대한 조회 및 업데이트 작업을 포함한다. 그러나 트랜잭션 처리와 동시성 제어를 위한 `for update` 구문이 사용되어 약간의 복잡도가 추가된다.

**쿼리 1:** `Balance` 테이블에서 특정 사용자의 잔액을 조회하는 쿼리.
```sql
select
        balance 
    from
        Balance balance 
    where
        balance is not null 
        and balance.userId = ?1
select
            b1_0.balance_id,
            b1_0.amount,
            b1_0.created_at,
            b1_0.updated_at,
            b1_0.user_id 
        from
            balance b1_0 
        where
            b1_0.balance_id is not null 
            and b1_0.user_id=?
        for update
```

**쿼리 2:** `BalanceTransaction` 테이블에 잔액 사용 내역을 삽입하는 쿼리.
```sql
insert into
        balance_transaction (amount, created_at, transaction_reason, transaction_type, user_id, version) 
    values
        (?, ?, ?, ?, ?, ?)
```

**쿼리 3:** `Balance` 테이블의 특정 사용자의 잔액을 업데이트하는 쿼리.
```sql
update balance 
    set
        amount=?,
        updated_at=?,
        user_id=? 
    where
        balance_id=?
```


#### 결제 처리

- **설명:** 결제 처리와 관련된 다양한 데이터베이스 쿼리를 실행한다. 주요 쿼리로는 잔액 조회, 잔액 충전 내역 삽입, 잔액 업데이트, 그리고 결제 내역 삽입이 있다. 이 쿼리들은 `Balance`, `BalanceTransaction`, `PaymentTransaction` 테이블을 사용하여 데이터의 일관성과 무결성을 유지한다.

- **빈도:** ★★★★★
    - 결제와 잔액 관리는 대부분의 사용자 활동에 필수적인 부분으로, 일상적으로 자주 발생한다. 특히 잔액 조회와 잔액 업데이트는 거의 모든 결제 과정에서 필수적으로 수행된다.

- **복잡도:** ★★★☆☆
    - 결제 처리 쿼리들은 주로 단일 테이블에서 실행되지만, 데이터의 일관성을 유지하고 트랜잭션을 처리해야 하므로 중간 수준의 복잡도를 가진다. 특히 `for update` 절을 포함한 쿼리는 동시성 문제를 해결하는 데 중요하다.

**쿼리 1:** `Balance` 테이블에서 특정 사용자의 잔액을 조회하는 쿼리.
```sql
select
        balance 
    from
        Balance balance 
    where
        balance is not null 
        and balance.userId = ?1 

select
            b1_0.balance_id,
            b1_0.amount,
            b1_0.created_at,
            b1_0.updated_at,
            b1_0.user_id 
        from
            balance b1_0 
        where
            b1_0.balance_id is not null 
            and b1_0.user_id=? for update
```

**쿼리 2:** `BalanceTransaction` 테이블에 잔액 충전 내역을 삽입하는 쿼리.
```sql
insert into
        balance_transaction (amount, created_at, transaction_reason, transaction_type, user_id, version) 
    values
        (?, ?, ?, ?, ?, ?)
```

**쿼리 3:** `Balance` 테이블에서 잔액을 업데이트하는 쿼리.
```sql
update balance 
    set
        amount=?,
        updated_at=?,
        user_id=? 
    where
        balance_id=?
```

**쿼리 4:** `PaymentTransaction` 테이블에 결제 내역을 삽입하는 쿼리.
```sql
insert into
        payment_transaction (amount, created_at, payment_method, payment_status, target_id, user_id) 
    values
        (?, ?, ?, ?, ?, ?)
```



### 결제 취소

- **설명:** 결제 취소를 처리하는 API에서 사용되는 쿼리들을 설명한다. 이 API는 특정 결제 트랜잭션을 취소하는 기능을 제공하며, 관련된 여러 데이터베이스 작업을 수행하여 트랜잭션의 상태를 업데이트하고 잔액을 조정한다.

- **빈도:** ★★★★☆
    - 결제 취소는 다양한 이유로 빈번하게 발생할 수 있다. 예를 들어, 결제 오류, 사용자 요청, 서비스 중단 등의 이유로 결제가 취소될 수 있다. 특히, 특정 기간 동안의 프로모션이나 이벤트 등에서 취소 빈도가 증가할 수 있다.

- **복잡도:** ★★★★☆
    - 결제 취소는 여러 테이블 간의 상호작용을 필요로 하며, 데이터 무결성을 유지하기 위해 트랜잭션 관리가 중요하다. 결제 상태 업데이트, 잔액 조정, 트랜잭션 기록 등 다양한 작업이 포함되어 있어 복잡도가 높다.

**쿼리 1:** `PaymentTransaction` 테이블에서 특정 결제 트랜잭션의 정보를 조회하는 쿼리.
```sql
select
        pt1_0.transaction_id,
        pt1_0.amount,
        pt1_0.created_at,
        pt1_0.payment_method,
        pt1_0.payment_status,
        pt1_0.target_id,
        pt1_0.user_id 
    from
        payment_transaction pt1_0 
    where
        pt1_0.transaction_id=?
```

**쿼리 2:** 잔액이 존재하는지 확인하고, 결제 취소 시 잔액을 업데이트하는 데 사용되는 쿼리.
```sql
select
        balance 
    from
        Balance balance 
    where
        balance is not null 
        and balance.userId = ?1 

select
            b1_0.balance_id,
            b1_0.amount,
            b1_0.created_at,
            b1_0.updated_at,
            b1_0.user_id 
        from
            balance b1_0 
        where
            b1_0.balance_id is not null 
            and b1_0.user_id=? for update
```

**쿼리 3:** `BalanceTransaction` 테이블에 새로운 트랜잭션 기록을 삽입하여 결제 취소 시 잔액 변경을 기록하기 위해 사용되는 쿼리.
```sql
insert into
        balance_transaction (amount, created_at, transaction_reason, transaction_type, user_id, version) 
    values
        (?, ?, ?, ?, ?, ?)
```

**쿼리 4:** `PaymentTransaction` 테이블에서 결제 취소 시 트랜잭션의 상태를 '취소됨'으로 변경하는 쿼리.
```sql
update
        payment_transaction 
    set
        amount=?,
        payment_method=?,
        payment_status=?,
        target_id=?,
        user_id=? 
    where
        transaction_id=?
```

**쿼리 5:** `Balance` 테이블에서 특정 사용자의 잔액을 업데이트하여 결제 취소 시 잔액을 조정하는 쿼리. 
```sql
update
        balance 
    set
        amount=?,
        updated_at=?,
        user_id=? 
    where
        balance_id=?
```



##### 결제 내역 조회


- **설명:** 결제 내역을 조회하는 쿼리이다. 이 쿼리는 특정 사용자의 결제 기록을 가져오며, 트랜잭션 ID, 결제 금액, 생성 일자, 결제 방법, 결제 상태, 타겟 ID, 사용자 ID를 포함한 다양한 정보를 조회한다. `PaymentTransaction` 테이블을 사용하여 특정 사용자 ID에 대한 결제 내역을 가져온다.

- **빈도:** ★★★★☆
    - 결제 내역 조회는 사용자가 자신의 결제 이력을 확인하거나 관리자가 결제 상태를 검토할 때 자주 사용된다. 일반적으로 사용 빈도가 높으며, 특정 프로모션이나 결제 문제가 발생한 경우 빈도가 더욱 증가할 수 있다.

- **복잡도:** ★★☆☆☆
    - 이 쿼리는 단일 테이블에서 데이터를 조회하는 단순한 구조를 가지므로 복잡도가 낮다. 다만, 결제 기록의 양이 많을 수 있으므로 효율적인 인덱스 사용이 필요하다.
  
**쿼리 1:** `payment_transaction` 테이블에서 `user_id`로 특정 사용자의 결제 트랜잭션을 조회하는 쿼리
```sql
select
        pt1_0.transaction_id,
        pt1_0.amount,
        pt1_0.created_at,
        pt1_0.payment_method,
        pt1_0.payment_status,
        pt1_0.target_id,
        pt1_0.user_id 
    from
        payment_transaction pt1_0 
    where
        pt1_0.user_id=?
```




## 인덱스 적절성 판단 및 설정

### Balance
##### 단일 인덱스 검토

1. **userId**
   - **필요성**: 사용자 잔액 조회 및 충전/사용 내역 조회에서 빈번히 사용된다. 사용자의 잔액 정보를 확인하거나 잔액 변동 내역을 조회하는 주요 조건으로 사용된다.
   - **카디널리티**: 높음 (사용자 ID는 유니크하여 카디널리티가 높다)
   - **변경 빈도**: 낮음 (사용자 ID는 잔액 생성 시 설정되며 이후 변경되지 않으므로 인덱스 재정렬 부담이 적다)

2. **createdAt**
   - **필요성**: 잔액 생성일자를 기준으로 조회하는 데 사용된다. 주로 특정 기간 동안의 잔액 변동을 확인할 때 유용하다.
   - **카디널리티**: 중간 (동일한 날짜에 여러 잔액이 생성될 수 있으므로 중복도가 존재할 수 있음)
   - **변경 빈도**: 낮음 (생성된 후 변경되지 않음)

##### 복합 인덱스 검토

1. **userId, createdAt**
   - **필요성**: 특정 사용자의 잔액 변동 내역을 시간순으로 조회할 때 유용하다. 사용자별 잔액 이력 조회에서 빈번히 사용된다.
   - **카디널리티**: 중간 (두 컬럼의 조합은 상당히 유니크할 가능성이 높아 효율적인 인덱스가 될 수 있다)
   - **변경 빈도**: 낮음 (사용자 ID와 생성일자는 변경되지 않으므로 인덱스 재정렬 부담이 적다)

2. **userId, amount**
   - **필요성**: 특정 사용자의 잔액 금액을 조건으로 조회할 때 유용하다. 예를 들어, 잔액이 특정 범위에 속하는 사용자를 조회하는 경우.
   - **카디널리티**: 중간 (두 컬럼의 조합은 특정 금액 범위 내에서 유니크할 가능성이 있다)
   - **변경 빈도**: 중간 (잔액 금액은 변경될 수 있으나 사용자 ID는 변경되지 않으므로 인덱스 재정렬 부담이 적다)




##### 종합 결론

`userId, createdAt`에 대한 복합 인덱스로 자주 사용되는 쿼리의 성능을 최적화한다.

```java
@Table(
    indexes = {
        @Index(name = "idx_user_created_at", columnList = "userId, createdAt")
    }
)
public class Balance extends AbstractAggregateRoot<Balance> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long balanceId;
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### BalanceTransaction

#### 단일 인덱스 검토


1. **userId**
   - **필요성**: 사용자의 거래 내역을 조회하는 데 사용된다. 사용자의 거래 내역을 확인하거나 재무 분석 시 자주 사용된다.
   - **카디널리티**: 높음 (각 사용자마다 고유 값)
   - **변경 빈도**: 낮음 (거래 생성 시 설정되며 이후 변경되지 않음)

2. **transactionType**
   - **필요성**: 특정 유형의 거래를 조회할 때 사용된다. 단, 특정 유형의 거래를 빈번히 조회하지 않는다면, 단일 인덱스는 필요하지 않다.
   - **카디널리티**: 낮음 (거래 유형은 제한된 값의 집합)
   - **변경 빈도**: 낮음 (거래 생성 시 설정되며 이후 변경되지 않음)


#### 복합 인덱스 검토

1. **userId, createdAt**
   - **필요성**: 특정 사용자의 거래 내역을 생성일자 순으로 조회할 때 유용하다. 사용자의 거래 내역 확인, 재무 분석 시 빈번히 사용된다. 
   - **카디널리티**: 중간 (두 컬럼의 조합은 상당히 유니크할 가능성이 높음)
   - **변경 빈도**: 낮음 (거래 생성 시 설정되며 이후 변경되지 않음)

2. **userId, transactionType**
   - **필요성**: 특정 사용자의 특정 유형 거래 내역을 조회할 때 유용하다. 재무 분석이나 특정 유형 거래 확인 시 빈번히 사용될 수 있다.
   - **카디널리티**: 중간 (두 컬럼의 조합으로 유일한 식별자가 될 가능성이 높음)
   - **변경 빈도**: 낮음 (거래 생성 시 설정되며 이후 변경되지 않음)

##### 종합 결론

`userId, createdAt`, `userId, transactionType`에 대한 복합 인덱스는 자주 사용되는 쿼리의 성능을 최적화한다.

```java
@Table(
    indexes = {
        @Index(name = "idx_user_created_at", columnList = "userId, createdAt"),
        @Index(name = "idx_user_transaction_type", columnList = "userId, transactionType")
    }
)
public class BalanceTransaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long transactionId;
    @Version private Long version;
    private Long userId;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING) private TransactionType transactionType;
    @Enumerated(EnumType.STRING) private TransactionReason transactionReason;
    private LocalDateTime createdAt;
}
```

### PaymentTransaction


##### 단일 인덱스 검토


1. **userId**
   - **필요성**: 특정 사용자의 모든 결제 내역을 조회할 때 사용된다. 사용자의 결제 내역을 확인할 때 자주 사용된다.
   - **카디널리티**: 높음 (각 사용자마다 고유 값)
   - **변경 빈도**: 낮음 (결제 생성 시 설정되며 이후 변경되지 않음)

2. **targetId**
   - **필요성**: 특정 예약, 상품, 서비스 등 다양한 분야의 결제 내역을 조회할 때 사용된다.
   - **카디널리티**: 높음 (다양한 타겟 ID 사용)
   - **변경 빈도**: 낮음 (결제 생성 시 설정되며 이후 변경되지 않음)

##### 복합 인덱스 검토

1. **userId, targetId**
   - **필요성**: 특정 사용자가 특정 타겟에 대해 결제한 내역을 조회할 때 유용하다. 결제 내역 조회, 결제 상태 확인 등에서 자주 사용된다.
   - **카디널리티**: 중간 (두 컬럼의 조합으로 유일한 식별자가 됨)
   - **변경 빈도**: 낮음 (결제 생성 시 설정되며 이후 변경되지 않음)

2. **userId, paymentStatus**
   - **필요성**: 특정 사용자의 특정 결제 상태를 조회할 때 유용하다. 결제 상태 별로 사용자의 결제 내역을 확인할 때 자주 사용된다.
   - **카디널리티**: 중간 (결제 상태 값은 제한적이므로 유니크하지 않음)
   - **변경 빈도**: 중간 (결제 상태는 자주 변경될 수 있음)


##### 종합 결론

`userId, targetId`에 대한 복합 인덱스는 자주 사용되는 쿼리의 성능을 최적화한다.


```java
@Table(
    indexes = {
        @Index(name = "idx_user_target", columnList = "userId, targetId"),
    }
)
public class PaymentTransaction extends AbstractAggregateRoot<PaymentTransaction> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long transactionId;
    private Long userId;
    private String targetId; 
    private BigDecimal amount;
    @Enumerated(EnumType.STRING) private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING) private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
}
```





## BEFORE & AFTER

인덱스 적용 전과 후를 비교한다.


### 잔액 조회 API



**`Balance` 테이블에서 `userId`로 특정 사용자의 잔액 정보를 조회하는 쿼리**
```sql
Explain 
select
    b1_0.balance_id,
    b1_0.amount,
    b1_0.created_at,
    b1_0.updated_at,
    b1_0.user_id 
from
    balance b1_0 
where
    b1_0.user_id=123
```

**before**

```sql
1,SIMPLE,b1_0,,ALL,,,,,2378402,10,Using where
```

```sql
-> Filter: (b1_0.user_id = 123)  (cost=248638 rows=237840) (actual time=18.4..2647 rows=1 loops=1)
    -> Table scan on b1_0  (cost=248638 rows=2.38e+6) (actual time=16.4..2466 rows=2.65e+6 loops=1)
```

**after**

```sql
1,SIMPLE,b1_0,,ref,idx_user_created_at,idx_user_created_at,9,const,1,100,
```

```sql
-> Index lookup on b1_0 using idx_user_created_at (user_id=123)  (cost=1.1 rows=1) (actual time=18.4..18.4 rows=1 loops=1)
```

##### 결과 분석

| 항목              | Before                             | After                                                                                                   |
|-------------------|------------------------------------|---------------------------------------------------------------------------------------------------------------|
| **인덱스 사용**   | 없음 (`FULL` 스캔)                     | `idx_user_created_at` 인덱스를 사용하여 `user_id`로 인덱스 룩업 수행                                             |
| **필터링 단계**   | 테이블 스캔 후 `user_id`에 대한 필터링을 추가로 수행 | 인덱스 룩업 단계에서 `user_id` 조건을 만족하는 레코드를 효율적으로 검색                                             |
| **비용**          | 248638                             | 1.1                                                                                                             |
| **예상 행 수**    | 2378402                            | 1                                                                                                               |
| **실제 실행 시간**| 16.4..2647 ms                      | 18.4..18.4 ms                                                                                                    |
| **실제 처리된 행 수**| 2.65e+6                            | 1                                                                                                               |

인덱스 룩업을 사용함으로써 쿼리의 효율성이 크게 향상되었다. 
전체 테이블을 스캔하지 않고 인덱스를 사용하여 필요한 레코드를 빠르게 찾을 수 있어 실행 시간이 대폭 단축되었고, 쿼리 비용도 크게 감소하였다.


### 잔액 충전/사용 내역 조회 API




**`BalanceTransaction` 테이블에서 `userId`로 특정 사용자의 잔액 거래 내역을 조회하는 쿼리**
```sql
Explain 
select
        bt1_0.transaction_id,
        bt1_0.amount,
        bt1_0.created_at,
        bt1_0.transaction_reason,
        bt1_0.transaction_type,
        bt1_0.user_id,
        bt1_0.version 
    from
        balance_transaction bt1_0 
    where
        bt1_0.user_id=123
```


**before**

```sql
1,SIMPLE,bt1_0,,ALL,,,,,1991966,10,Using where
```

```sql
-> Filter: (bt1_0.user_id = 123)  (cost=207496 rows=199197) (actual time=8.98..2675 rows=2 loops=1)
    -> Table scan on bt1_0  (cost=207496 rows=1.99e+6) (actual time=8.37..2492 rows=2e+6 loops=1)
```

**after**

```sql
1,SIMPLE,bt1_0,,ref,"idx_user_created_at,idx_user_transaction_type",idx_user_created_at,9,const,2,100,
```

```sql
-> Index lookup on bt1_0 using idx_user_created_at (user_id=123)  (cost=2.2 rows=2) (actual time=27..27.1 rows=2 loops=1)
```

##### 결과 분석

| 항목              | Before                                                                                                  | After                                                                                                   |
|-------------------|---------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| **인덱스 사용**   | 없음 (`FULL` 스캔)                                                                                                       | `idx_user_created_at`, `idx_user_transaction_type` 인덱스를 사용하여 `user_id`로 인덱스 룩업 수행           |
| **필터링 단계**   | 테이블 스캔 후 `user_id`에 대한 필터링을 수행                                                                          | 인덱스 룩업 단계에서 `user_id` 조건을 만족하는 레코드를 효율적으로 검색                                    |
| **비용**          | 207496                                                                                                  | 2.2                                                                                                     |
| **예상 행 수**    | 199197                                                                                                  | 2                                                                                                       |
| **실제 실행 시간**| 8.98..2675 ms                                                                                           | 27..27.1 ms                                                                                             |
| **실제 처리된 행 수**| 2                                                                                                       | 2                                                                                                       |

인덱스를 사용하여 `user_id`로 인덱스 룩업을 수행함으로써, 쿼리의 실행 시간이 크게 단축되었다. 
테이블 스캔 대신 인덱스 스캔을 사용하여 효율적으로 데이터를 검색할 수 있었다. 
비용과 예상 행 수가 크게 줄어들어 쿼리의 성능이 향상되었다.



### 결제 내역 조회 API 


**`payment_transaction` 테이블에서 `user_id`로 특정 사용자의 결제 트랜잭션을 조회하는 쿼리**
```sql
Explain select
        pt1_0.transaction_id,
        pt1_0.amount,
        pt1_0.created_at,
        pt1_0.payment_method,
        pt1_0.payment_status,
        pt1_0.target_id,
        pt1_0.user_id 
    from
        payment_transaction pt1_0 
    where
        pt1_0.user_id=123
```




**before**

```sql
1,SIMPLE,pt1_0,,ALL,,,,,915320,10,Using where
```

```sql
-> Filter: (pt1_0.user_id = 123)  (cost=95987 rows=91532) (actual time=23.2..797 rows=1 loops=1)
    -> Table scan on pt1_0  (cost=95987 rows=915320) (actual time=21.9..750 rows=1e+6 loops=1)
```

**after**

```sql
1,SIMPLE,pt1_0,,ref,idx_user_target,idx_user_target,9,const,1,100,
```

```sql
-> Index lookup on pt1_0 using idx_user_target (user_id=123)  (cost=0.817 rows=1) (actual time=8.63..8.64 rows=1 loops=1)
```



##### 결과 분석

| 항목              | Before                                                                                           | After                                                                                          |
|-------------------|--------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| **인덱스 사용**   | 없음 (`FULL` 스캔)                                                       | `idx_user_target` 인덱스를 사용하여 `user_id`로 인덱스 룩업 수행                               |
| **필터링 단계**   | 전체 테이블 스캔 후 `user_id`에 대한 필터링을 수행                                                | 인덱스 룩업 단계에서 `user_id` 조건을 만족하는 레코드를 효율적으로 검색                        |
| **비용**          | 95987                                                                                             | 0.817                                                                                          |
| **예상 행 수**    | 91532                                                                                            | 1                                                                                              |
| **실제 실행 시간**| 23.2..797 ms                                                                                      | 8.63..8.64 ms                                                                                   |
| **실제 처리된 행 수**| 1                                                                                               | 1                                                                                              |

전체 테이블 스캔에서 인덱스 룩업으로 변경됨에 따라 비용이 크게 감소하였고, 실행 시간 또한 상당히 단축되었다. 






</details>
