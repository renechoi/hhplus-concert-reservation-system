##### 항해 플러스 서버 구축 콘서트 예약 시스템

<details>
<summary><b>요구사항</b></summary>

<details>
<summary><b>Description</b></summary>

> 💡 아래 명세를 잘 읽어보고, 서버를 구현합니다.

- `콘서트 예약 서비스`를 구현해 봅니다.
- 대기열 시스템을 구축하고, 예약 서비스는 작업가능한 유저만 수행할 수 있도록 해야합니다.
- 사용자는 좌석예약 시에 미리 충전한 잔액을 이용합니다.
- 좌석 예약 요청시에, 결제가 이루어지지 않더라도 일정 시간동안 다른 유저가 해당 좌석에 접근할 수 없도록 합니다.

</details>

<details>
<summary><b>Requirements</b></summary>

- 아래 5가지 API 를 구현합니다.
    - 유저 토큰 발급 API
    - 예약 가능 날짜 / 좌석 API
    - 좌석 예약 요청 API
    - 잔액 충전 / 조회 API
    - 결제 API
- 각 기능 및 제약사항에 대해 단위 테스트를 반드시 하나 이상 작성하도록 합니다.
- 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성하도록 합니다.
- 동시성 이슈를 고려하여 구현합니다.
- 대기열 개념을 고려해 구현합니다.
</details>

<details>
<summary><b>API Specs</b></summary>



1️⃣ **`주요` 유저 대기열 토큰 기능**

- 서비스를 이용할 토큰을 발급받는 API를 작성합니다.
- 토큰은 유저의 UUID 와 해당 유저의 대기열을 관리할 수 있는 정보 ( 대기 순서 or 잔여 시간 등 ) 를 포함합니다.
- 이후 모든 API 는 위 토큰을 이용해 대기열 검증을 통과해야 이용 가능합니다.

> 기본적으로 폴링으로 본인의 대기열을 확인한다고 가정하며, 다른 방안 또한 고려해보고 구현해 볼 수 있습니다.
>

**2️⃣ `기본` 예약 가능 날짜 / 좌석 API**

- 예약가능한 날짜와 해당 날짜의 좌석을 조회하는 API 를 각각 작성합니다.
- 예약 가능한 날짜 목록을 조회할 수 있습니다.
- 날짜 정보를 입력받아 예약가능한 좌석정보를 조회할 수 있습니다.

> 좌석 정보는 1 ~ 50 까지의 좌석번호로 관리됩니다.
>

3️⃣ **`주요` 좌석 예약 요청 API**

- 날짜와 좌석 정보를 입력받아 좌석을 예약 처리하는 API 를 작성합니다.
- 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 5분간 임시 배정됩니다. ( 시간은 정책에 따라 자율적으로 정의합니다. )
- 만약 배정 시간 내에 결제가 완료되지 않는다면 좌석에 대한 임시 배정은 해제되어야 하며 다른 사용자는 예약할 수 없어야 한다.

4️⃣ **`기본`**  **잔액 충전 / 조회 API**

- 결제에 사용될 금액을 API 를 통해 충전하는 API 를 작성합니다.
- 사용자 식별자 및 충전할 금액을 받아 잔액을 충전합니다.
- 사용자 식별자를 통해 해당 사용자의 잔액을 조회합니다.

5️⃣ **`주요` 결제 API**

- 결제 처리하고 결제 내역을 생성하는 API 를 작성합니다.
- 결제가 완료되면 해당 좌석의 소유권을 유저에게 배정하고 대기열 토큰을 만료시킵니다.


</details>

<details>
<summary><b>💡KEY POINT</b></summary>

- 유저간 대기열을 요청 순서대로 정확하게 제공할 방법을 고민해 봅니다.
- 동시에 여러 사용자가 예약 요청을 했을 때, 좌석이 중복으로 배정 가능하지 않도록 합니다.

</details>

</details>





<details>
<summary><b>유저 스토리</b></summary>

### 통합 시나리오

1. 클라이언트가 예약 가능한 날짜와 좌석 정보를 서버에 요청합니다.
2. 서버는 예약 가능한 날짜와 좌석 정보를 반환합니다.
3. 클라이언트가 사용자의 토큰을 요청하여 대기열에 진입합니다.
4. 서버는 대기열에서 사용자의 순서를 확인하고, 유효한 토큰을 발급합니다.
5. 클라이언트가 유효한 토큰을 이용해 좌석 예약을 서버에 요청합니다.
6. 서버는 좌석을 임시 예약하고, 예약 성공 여부를 반환합니다.
7. 예약 성공 시, 클라이언트가 결제를 서버에 요청합니다.
8. 서버는 결제 처리를 완료하고, 좌석 예약을 확정합니다.

### 세부 시나리오

#### 1. 예약 가능한 날짜 및 좌석 조회


1. 클라이언트가 서버에 `/available-dates` API를 호출하여 예약 가능한 날짜 목록을 요청합니다.
2. 서버는 예약 가능한 날짜 목록을 조회하여 클라이언트에 반환합니다.
3. 클라이언트가 특정 날짜를 선택하고, 서버에 `/available-seats` API를 호출하여 해당 날짜의 예약 가능한 좌석 정보를 요청합니다.
4. 서버는 해당 날짜의 예약 가능한 좌석 목록을 조회하여 클라이언트에 반환합니다.


#### 2. 유저 대기열 토큰 발급

1. 클라이언트가 서버에 `/token-request` API를 호출하여 대기열 토큰을 요청합니다.
2. 서버는 사용자의 UUID와 대기열 정보를 기반으로 토큰을 생성합니다.
3. 서버는 대기열에 사용자를 등록하고, 대기열 순서를 관리합니다.
4. 서버는 유효한 토큰을 클라이언트에 전달해야 합니다. 
5. 클라이언트는 대기열 상태를 확인하기 위해 서버에 주기적으로 대기열 상태를 요청(polling)하거나, 서버가 대기열 상태를 클라이언트에 전달(SSE)을 사용하거나 웹소켓을 이용해 통신할 수 있습니다. 
  - **Polling**:
    1. 클라이언트는 일정 주기로 서버에 `/queue-status` API를 호출하여 대기열 상태를 요청합니다.
    2. 서버는 현재 대기열 상태(현재 순서, 잔여 시간 등)를 조회하여 클라이언트에 반환합니다.
  - **SSE**:
    1. 클라이언트는 서버에 `/queue-status-sse` API를 호출하여 SSE 연결을 시작합니다.
    2. 서버는 대기열 상태가 변경될 때마다 클라이언트에 실시간으로 상태 정보를 전송합니다.
  - **WebSocket**:
    1. 클라이언트는 서버와 WebSocket 연결을 설정합니다.
    2. 서버는 대기열 상태가 변경될 때마다 클라이언트에 실시간으로 상태 정보를 전송합니다.


#### 3. 좌석 예약 요청


1. 클라이언트가 서버에 `/reserve-seat` API를 호출하여 좌석 예약 요청을 보냅니다.
2. 서버는 사용자의 토큰을 검증하여 유효성을 확인합니다.
3. 토큰이 유효한 경우, 서버는 해당 좌석을 사용자를 위해 임시 예약합니다.
4. 서버는 임시 예약 시간을 설정하고, 다른 사용자가 해당 좌석에 접근하지 못하도록 합니다.
5. 서버는 예약 성공 여부를 클라이언트에 반환합니다.

#### 4. 결제 처리


1. 클라이언트가 서버에 `/process-paymentEntity` API를 호출하여 결제 요청을 보냅니다.
2. 서버는 사용자의 토큰을 검증하여 유효성을 확인합니다.
3. 토큰이 유효한 경우, 서버는 좌석 예약 정보를 확인합니다.
4. 서버는 결제 처리를 진행합니다.
5. 결제가 성공하면 서버는 좌석 예약을 확정하고, 대기열 토큰을 만료시킵니다.
6. 서버는 결제 성공 메시지를 클라이언트에 반환합니다.
7. 결제가 실패할 경우, 서버는 임시 예약을 취소하고, 클라이언트에 실패 메시지를 반환합니다.

</details>






<details>
<summary><b>시퀀스 다이어그램</b></summary>


## part1: 대기열 생성 및 관리

![sequence-part-1.png](documents%2Fdiagram%2Fsequence-part-1.png)


## part2: 예약/결제

![sequence-part-2.png](documents%2Fdiagram%2Fsequence-part-2.png)

</details>



<details>
<summary><b>아키텍처 구상도</b></summary>

### 아키텍처 

![waiting-queue-archi.png](documents%2Farchitecture%2Fwaiting-queue-archi.png)


</details>



<details>
<summary><b>Milestone</b></summary>

### 마일스톤

#### 1주차 (6월 30일 - 7월 5일)
- **프로젝트 시작 및 초기 설정**
  - 프로젝트 저장소와 초기 폴더 구조를 설정합니다.
  - 프로젝트 요구사항을 분석하고 시나리오 및 시퀀스 다이어그램을 작성합니다.
  - 데이터베이스 스키마와 ERD를 계획하고 설계합니다.
  - Mock API를 작성합니다.

#### 2주차 (7월 6일 - 7월 12일)
- **대기열 시스템 구축**
  - 유저 토큰 발급 API 구현
  - 대기열 관리 시스템 구현
  - WebSocket을 통한 대기열 상태 확인 기능 개발

#### 3주차 (7월 13일 - 7월 19일)
- **예약 및 결제 기능 구현**
  - 예약 가능한 날짜/좌석 조회 API 구현
  - 좌석 예약 요청 API 구현
  - 잔액 충전/조회 API 구현
  - 결제 API 구현

#### 4주차 (7월 20일 - 7월 26일)
- **기능 고도화 및 리팩토링**
  - 코드 리팩토링 및 최적화
  - 유닛 테스트 작성 및 통합 테스트 수행
  - 에러 핸들링 및 예외 처리 강화

#### 5주차 (7월 27일 - 8월 2일)
- **대용량 트래픽 대비 개선**
  - 동시성 이슈 해결 및 성능 최적화
  - 로드 테스트 및 성능 모니터링 도구 설정
  - 다중 인스턴스 환경에서의 동작 확인 및 개선

#### 6주차 (8월 3일 - 8월 9일)
- **트러블 슈팅 및 모니터링**
  - 실시간 모니터링 시스템 구축
  - 로그 관리 및 분석 시스템 설정
  - 장애 대응 시나리오 테스트 및 문서화

#### 7주차 (8월 10일 - 8월 16일)
- **보안 강화 및 검토**
  - API 보안 검토 및 취약점 점검
  - 데이터 보호 및 개인정보 처리 방침 검토
  - 보안 관련 유닛 테스트 및 통합 테스트 수행

#### 8주차 (8월 17일 - 8월 23일)
- **최종 검토 및 배포 준비**
  - 전체 시스템 검토 및 최종 리팩토링
  - 배포 스크립트 작성 및 배포 환경 설정
  - 최종 문서화 및 사용자 가이드 작성

### 요약

- **1주차:** 프로젝트 설정 및 초기 설계.
- **2~3주차:** 기능 구현 (대기열 시스템, 예약 및 결제 기능).
- **4~8주차:** 고도화 및 리팩토링, 대용량 트래픽 트러블 슈팅 대비 개선.



```mermaid
gantt
  title 항해 플러스 서버 구축 콘서트 예약 시스템 마일스톤
  dateFormat  YYYY-MM-DD
  section 초기 설정 및 설계
    프로젝트 설정 및 설계         :done, des1, 2024-06-30, 2024-07-05

  section 기능 구현
    대기열 시스템 구축             :active, dev1, 2024-07-06, 2024-07-11
    예약 및 결제 기능 구현        :active, dev2, 2024-07-14, 2024-07-19

  section 기능 고도화 및 리팩토링
    코드 리팩토링 및 테스트        :active, enh1, 2024-07-20, 2024-07-25

  section 버퍼 기간
    버퍼 기간                     :active, buf1, 2024-07-26, 2024-07-28

  section 대용량 트래픽 대비 개선
    성능 최적화 및 트러블 슈팅     :active, opt1, 2024-07-29, 2024-08-02

  section 버퍼 기간
    버퍼 기간                     :active, buf2, 2024-08-03, 2024-08-04

  section 트러블 슈팅 및 모니터링
    실시간 모니터링 및 로그 관리   :active, mon1, 2024-08-05, 2024-08-09

  section 보안 강화 및 검토
    보안 검토 및 강화             :active, sec1, 2024-08-10, 2024-08-14

  section 버퍼 기간
    버퍼 기간                     :active, buf3, 2024-08-15, 2024-08-16

  section 최종 검토 및 배포 준비
    시스템 검토 및 배포 준비       :active, rel1, 2024-08-17, 2024-08-23

```



</details>






<details>
<summary><b>ERD</b></summary>

### erd

![erd.png](documents%2Ferd%2Ferd.png)


</details>





<details>
<summary><b>API Spec & Mock API</b></summary>


## Swagger 

### queue-management

> http://localhost:24031/queue-management/swagger-ui/index.html#/

![queue-management.png](documents%2Fswagger-screenshot%2Fqueue-management.png)



### api-orchestration

> http://localhost:24051/api-orchestration/swagger-ui/index.html#/

![api-orchestration.png](documents%2Fswagger-screenshot%2Fapi-orchestration.png)


### client-channel-service

> http://localhost:24101/client-channel/swagger-ui/index.html#/

![client-channel.png](documents%2Fswagger-screenshot%2Fclient-channel.png)



### payment-service 

> http://localhost:24081/payment/swagger-ui/index.html#/

![payment.png](documents%2Fswagger-screenshot%2Fpayment.png)



### reservation-service

> http://localhost:24071/reservation/swagger-ui/index.html#/

![reservation-service.png](documents%2Fswagger-screenshot%2Freservation-service.png)








## 유저 토큰 발급 API

### Endpoint
```http request
POST http://localhost:24000/queue-management/api/token
Content-Type: application/json
```

### Request Body
```json
{
  "userId": "spring123",
  "requestedTime": "2024-07-03T10:00:00",
  "priority": 2
}
```

### Response
- **Status Code: 201 Created**
- **Headers:**
  - `Content-Type: application/json`
  - `Date: Thu, 04 Jul 2024 14:52:58 GMT`
- **Body:**
  ```json
  {
    "userId": "spring123",
    "tokenValue": "ce586fe5-78fb-4124-8e83-7cb164fbf58b",
    "remainingTime": "2024-07-05T00:22:58.008573",
    "position": 1,
    "validUntil": "2024-07-05T00:52:58.008641",
    "status": "WAITING"
  }
  ```
![token-issue.png](documents%2Fmock-api%2Ftoken-issue.png)

---

## 예약 가능한 날짜 목록 조회 API

### Endpoint
```http request
GET http://localhost:24000/api-orchestration/api/reservations/available-dates/1
```

### Response
- **Status Code: 200 OK**
- **Headers:**
  - `Content-Type: application/json`
  - `Date: Thu, 04 Jul 2024 14:55:57 GMT`
- **Body:**
  ```json
  {
    "concertId": 1,
    "availableDates": [
      "2024-07-10",
      "2024-07-11",
      "2024-07-12"
    ]
  }
  ```
![available-dates.png](documents%2Fmock-api%2Favailable-dates.png)

---

## 특정 날짜의 예약 가능한 좌석 목록 조회 API

### Endpoint
```http request
GET http://localhost:24000/api-orchestration/api/reservations/available-seats?concertId=1&date=2024-07-10
```

### Response
- **Status Code: 200 OK**
- **Headers:**
  - `Content-Type: application/json`
  - `Date: Thu, 04 Jul 2024 14:56:00 GMT`
- **Body:**
  ```json
  {
    "concertId": 1,
    "date": "2024-07-10",
    "availableSeats": [
      {"seatId": 1, "seatNumber": "A1"},
      {"seatId": 2, "seatNumber": "A2"},
      ...
    ]
  }
  ```
![retrieve-seats-by-date-params.png](documents%2Fmock-api%2Fretrieve-seats-by-date-params.png)

---

## 좌석 예약 요청 API

### Endpoint
```http request
POST http://localhost:24000/api-orchestration/api/reservations/reserve-seat
Content-Type: application/json
```

### Request Body
```json
{
  "userId": 1,
  "concertOptionId": 1,
  "seatId": 1,
  "date": "2024-07-10"
}
```

### Response
- **Status Code: 200 OK**
- **Headers:**
  - `Content-Type: application/json`
  - `Date: Thu, 04 Jul 2024 14:45:42 GMT`
- **Body:**
  ```json
  {
    "reservationId": 1,
    "userId": 1,
    "concertOptionId": 1,
    "seatId": 1,
    "status": "TEMPORARY_RESERVED",
    "message": "2024-07-04T23:50:42.733509분간 예약되었습니다."
  }
  ```
![reserve-seat.png](documents%2Fmock-api%2Freserve-seat.png)

---

## 잔액 충전 API

### Endpoint
```http request
POST http://localhost:24000/api-orchestration/api/balance/charge
Content-Type: application/json
```

### Request Body
```json
{
  "userId": 1,
  "amount": 100.00
}
```

### Response
- **Status Code: 200 OK**
- **Headers:**
  - `Content-Type: application/json`
  - `Date: Thu, 04 Jul 2024 14:45:45 GMT`
- **Body:**
  ```json
  {
    "userId": 1,
    "amount": 100.00
  }
  ```
![charge-balance.png](documents%2Fmock-api%2Fcharge-balance.png)

---

## 잔액 조회 API

### Endpoint
```http request
POST http://localhost:24000/api-orchestration/api/balance/payment
Content-Type: application/json
```

### Request Body
```json
{
  "userId": 1,
  "reservationId": 1,
  "amount": 100.00
}
```

### Response
- **Status Code: 200 OK**
- **Headers:**
  - `Content-Type: application/json`
  - `Date: Thu, 04 Jul 2024 14:45:46 GMT`
- **Body:**
  ```json
  {
    "paymentId": 1,
    "userId": 1,
    "reservationId": 1,
    "amount": 100.00,
    "status": "CONFIRMED",
    "message": "결제가 정상 처리되었고, 좌석도 예약 완료"
  }
  ```
![retrieve-balance.png](documents%2Fmock-api%2Fretrieve-balance.png)

</details>







<details>
<summary><b>Tests</b></summary>

## 서비스별 테스트 코드 현황

- 총계: 91 건

### api-orchestration-service

- 14 건

![api-orchestration-service.png](documents%2Ftest-captures%2Fapi-orchestration-service.png)



### queue-management-service

- 38 건 

![queue-management-service.png](documents%2Ftest-captures%2Fqueue-management-service.png)



### reservation-service


- 14 건

![reservation-service.png](documents%2Ftest-captures%2Freservation-service.png)


### payment-service

- 22 건

![payment-service.png](documents%2Ftest-captures%2Fpayment-service.png)


### client-channel-service 

- 3 건

![client-channel-service.png](documents%2Ftest-captures%2Fclient-channel-service.png)



</details>




<details>
<summary><b>구상 단계에서의 기술적 고민들</b></summary>

# 구상 단계에서의 기술적 고민들

## 1. 대기열 상태 인지 방법

대기열 상태를 유저에게 전달하는 방법에 대해 고민했다. 

### Polling

- **방법**: Polling 방식은 클라이언트가 일정한 주기로 서버에 대기열 상태를 요청하고, 서버가 해당 정보를 응답하는 방식이다.
- **장점**: Polling은 구현이 간단하다. 
- **해결하는 문제**: 대기열 상태를 주기적으로 업데이트하여 클라이언트가 최신 정보를 얻을 수 있도록 한다. 주기성을 두어 부하를 조절할 수 있다.
- **예상되는 어려움**:
  - 동기적으로 작동하여 서버와 클라이언트 모두에서 성능 최적화가 어렵다. 
  - 실시간성이 떨어지며, 대기열 상태가 자주 변경될 경우 업데이트가 지연될 수 있다.

### Server-Sent Events (SSE)

- **방법**: SSE 방식은 서버가 클라이언트에 실시간으로 대기열 상태를 전송하는 단방향 통신 방식이다. 클라이언트는 초기 연결을 설정한 후 서버에서 보내는 이벤트를 수신한다.

- **장점**: SSE는 서버에서 클라이언트로 실시간 이벤트를 전송할 수 있습니다. 실시간성이 반영된다.

- **해결하는 문제**:
  - 서버에서 클라이언트로의 실시간 데이터 전송을 통해 대기열 상태 변경을 즉시 반영할 수 있다.
  - 클라이언트의 반복적인 요청을 줄여 서버 부하를 감소시킨다.

- **예상되는 어려움**:
  - 많은 클라이언트가 연결될 경우 서버 부하가 증가할 수 있다. 
  - 모든 이벤트에 대해 처리하다 보면, 대규모 트래픽 환경에서 서버 부하가 심해질 수 있다. 특히 이벤트 방식의 비동기 처리를 고려한다면 서버에서 동시에 처리해야 하는 이벤트의 양이 증가하면 큰 부하가 예상된다.

### WebSocket

- **방법**: WebSocket 방식은 서버와 클라이언트 간의 양방향 실시간 통신을 가능하게 한다. 초기 연결 이후 지속적인 연결을 유지하며 데이터 전송 시 오버헤드가 적다.

- **장점**: WebSocket은 양방향 통신을 지원하여 대기열 상태 변경을 실시간으로 주고받을 수 있다.

- **해결하는 문제**:
  - 실시간 데이터 전송을 통해 대기열 상태를 즉시 업데이트하고, 양방향 통신으로 클라이언트와 서버 간 원활한 통신이 가능한다.
  - 효율적인 데이터 전송으로 대규모 트래픽을 처리할 수 있다.

- **예상되는 어려움**:
  - 구현이 복잡하고 방화벽이나 프록시 설정 문제를 겪을 수 있다.
  - 많은 클라이언트가 연결될 경우 서버 자원 소모에 대해 고민해야 한다.
  - 모든 이벤트에 대해 처리하다 보면, 대규모 트래픽 환경에서 서버 부하가 심해질 수 있다. 특히 이벤트 방식의 비동기 처리를 고려한다면 서버에서 동시에 처리해야 하는 이벤트의 양이 증가하면 큰 부하가 예상된다.



### 결론

대규모 서비스에서 웹소켓과 폴링을 선택할 때는, 실시간성과 연결 수용성 및 확장성을 중점으로 생각해보자.  

1. **실시간성이 중요한 경우**:
  - 웹소켓이 더 적합하다. 웹소켓은 실시간 양방향 통신을 제공하여 대기열 상태와 같은 실시간 업데이트를 효과적으로 처리할 수 있다.

2. **대규모 연결 및 확장성**:
  - 폴링이 더 적합할 수 있다. 폴링은 구현이 간단하고, 부하 조절이 용이하여 대규모 트래픽을 관리하기 쉽다. 폴링 주기를 최적화하고 상태 변경 감지를 통해 효율성을 개선할 수 있다.



## 2. 대기열 상태 업데이트 최적화

대기열 상태의 모든 변화를 실시간으로 전부 트래킹하는 것은 서버에 큰 부하를 줄 수 있다고 판단했다. 이에 대한 해결책으로 다음과 같은 방안을 고려했다.

### polling 최적화 

  1. **폴링 간격 최적화**: 클라이언트가 대기열 상태를 확인하는 폴링 주기를 최적화한다. 예를 들어, 초기 대기열에 진입한 사용자는 5초마다 폴링을 하고, 대기열 상위권에 근접한 사용자는 1초마다 폴링을 하도록 조정합니다. 이렇게 하면 서버의 부하를 최소화하면서도 사용자에게 중요한 시점에 실시간성을 제공할 수 있다.

  2. **상태 변경 감지**: 폴링 방식에서는 대기열 상태가 변경될 때만 클라이언트에게 응답하도록 서버를 설정한다. 상태가 변경되지 않았을 경우에는 응답을 보내지 않거나 최소한의 데이터만 전송하여 트래픽과 서버 부하를 줄일 수 있다.

### 이벤트 처리 최적화 

polling 대비 sse나 websocket을 사용하는 방식은 비동기적 프로세스를 이용할 수 있다는 장점이 있다. 그러나 모든 변화를 전부 감지하여 처리하면 서버 내부적으로 과도한 처리량이 발생할 수 있습니다. 이를 해결하기 위해 다음과 같은 방안을 고려했다.

1. **변경 구간 전송**: 대기열 상태의 모든 변화를 실시간으로 전송하는 대신, 대기열의 주요 변화만 전송한다. 예를 들어, 대기열 순서가 10명씩 변할 때마다 클라이언트에게 업데이트를 전송한다. 

2. **우선순위 기반 업데이트**: 대기열 상위에 있는 사용자가 더 자주 업데이트를 받도록 우선순위를 설정한다. 예를 들어, 대기열 상위 10명에게는 상태 변화를 실시간으로 전송하고, 나머지 사용자에게는 일정 간격(e.g. 1분)으로 요약된 상태를 전송한다. 

3. **이벤트 배치 처리**: 상태 변화를 개별적으로 처리하지 않고, 일정 시간 간격으로 배치 처리한다. 예를 들어, 1초 동안 발생한 모든 상태 변화를 모아서 한 번에 전송한다. 




## 3. 대기열 정보를 전달할 때 얼마나 정확해야 할까? 

대기 순서를 대기자들에게 알려주어야 한다는 요구 사항에 대해, 이 순서가 반드시 절대적으로 정확해야 할지에 대해서 고민할 필요가 있다. 대기 순서를 대략적으로 알려주어도 사용자에게 충분한 정보를 제공할 수 있다. 

### 대기열 데이터의 성격 및 특징

대기열 데이터의 성격과 특징을 생각해보자. 

- **변동성이 큰 데이터**: 대기열은 사용자 요청이 계속 들어오고 처리되는 과정에서 실시간으로 변동한다.
- **상대적인 정보**: 대기열은 특성상 상대적인 정보가 중요하다. 즉, 내 앞에 몇 명이 있는지가 절대적인 수치보다 중요하다.
- **예상 대기 시간**: 대기열은 그 자체의 정보보다 그로부터 얼마나 기다리는 시간이 중요하다. 


### 사용자 입장에서의 실질적인 니즈

대기열의 위와 같은 특성을 고려할 때, 실질적인 사용자의 니즈를 생각해보자. 

- **투명성**: 사용자는 자신의 요청이 처리되고 있다는 확신을 원한다. 따라서 본인의 위치 정보에 대한 정보는 어떤식으로든 제공되어야 한다. 
- **예측 가능성**: 사용자들은 자신이 얼마나 기다려야 하는지 알고 싶어 한다. 사용자 입장에서 대기열의 "몇 번째"라는 절대적인 사실 자체보다는 대기 시간이 어느 정도일지에 대한 정보가 더 중요할 수 있다. 
- **실제 알고 싶은 것**: 사용자 입장에서는 자신의 대기 순서가 정확히 몇 번째인지 알기보다는, 대략적인 위치나 예상 대기 시간을 알고 싶어 할 수 있다. 



### 대기 순서를 대략적으로 알려주는 방법

위와 같은 대기열의 특성과 사용자 니즈를 바탕으로, 대기 순서를 대략적으로 알려주는 몇 가지 방법을 고려해보자. 

1. **대기열 그룹화**:
  - 대기열을 여러 개의 그룹으로 나누어 사용자에게 그룹 내에서의 순서를 알려준다. 예를 들어, "당신은 현재 50-100번 그룹에 있습니다"와 같은 방식으로 정보를 제공한다. 이는 대기열의 변동성을 줄이면서도 사용자가 자신의 위치를 대략적으로 알 수 있도록 한다.

2. **ETA(Estimated Time of Arrival) 제공**:
  - 대기열 순서 대신 예상 대기 시간을 알려준다. 예를 들어, "현재 대기 시간은 약 5분입니다"와 같은 방식으로 정보를 제공하여 사용자가 얼마나 기다려야 하는지 알 수 있게 한다. 

3. **확률적 대기열 정보 제공**:
  - 대기열의 상태를 확률적으로 제공하여 사용자가 어느 정도 대기해야 할지를 대략적으로 예측할 수 있게 한다. 예를 들어, "당신은 현재 상위 20%의 대기열에 있습니다"와 같은 정보를 제공한다. 




  
### 결론

대기열은 매우 동적이다. 사용자 요청의 속도와 처리 시간에 따라 실시간으로 변동된다. 유저 수가 많을수록 대기열의 순서가 빠르게 변화할 수 있다. 이런 환경에서 절대적으로 정확한 순서보다 대략적인 순서나 예상 대기 시간을 제공하는 것이 실효성 차원에서 나은 선택일 수 있다.






## 4. 콘서트 옵션 - 좌석 생성 방식에 대한 쟁점 


Concert Option와 Seat의 연관관계 기반의 프로세스에서 좌석을 언제 생성할 것인가 문제가 있다. 
즉, 콘서트 옵션 생성 시점에서 할 것인지, 실제 예약 요청 시점에서 할 것인지에 대한 문제이다. 
각각의 접근 방식에는 장단점이 있어서 다음과 같이 고민해보았다. 


### 엔티티 구조와 관계

먼저 엔티티 간의 관계를 살펴보자.

1. **Concert**: 콘서트 자체를 의미한다. 
2. **Concert Option**: 콘서트의 특정 옵션 또는 인스턴스를 나타내며, 날짜, 시간, 티켓 가격 등을 설정한다.
3. **Seat**: 특정 콘서트 옵션 내에서 예약 가능한 개별 좌석을 나타낸다.

Concert는 여러 Concert Option을 가지고, 각 Concert Option은 여러 Seat를 가진다. 


<details>
<summary><b>Concert</b></summary>


```java
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners({AuditingEntityListener.class})
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertId;
    private String title;
    @CreatedDate
    @Column(updatable = false)
    @Setter
    private LocalDateTime createdAt;
    @Setter
    private LocalDateTime requestAt;
}
```
</details>



<details>
<summary><b>ConcertOption</b></summary>

```
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners({AuditingEntityListener.class})
public class ConcertOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertOptionId;
    @ManyToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;
    private LocalDateTime concertDate;
    private Duration concertDuration;
    private String title;
    private String description;
    private BigDecimal price;
    @CreatedDate
    @Column(updatable = false)
    @Setter
    private LocalDateTime createdAt;
    @Setter
    private LocalDateTime requestAt;
}
```
</details>




<details>
<summary><b>Seat</b></summary>
```
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners({AuditingEntityListener.class})
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    @ManyToOne
    @JoinColumn(name = "concert_option_id")
    private ConcertOption concertOption;
    private Long seatNumber;
    private Boolean occupied;
    @CreatedDate
    @Column(updatable = false)
    @Setter
    private LocalDateTime createdAt;
}
```

</details>


### 1. 콘서트 옵션 생성 시 좌석 생성 전략

#### 장점:
- **즉시 사용 가능**: 콘서트 옵션이 생성되자마자 좌석이 준비해 놓는 방식이다. 조회시 이미 생성된 좌석을 불러오기만 하면 된다.

#### 단점:
- **잠재적 오버헤드**: 콘서트 옵션 생성 로직과 결합도가 크다. 대규모 이벤트의 경우, 수천, 수만 개의 좌석을 미리 생성한다면, 옵션 생성 자체에서 시간 소요가 클 수 있다.
- **미사용 좌석**: 콘서트 옵션이 매진되지 않으면 미리 생성된 좌석이 가비지가 될 수 있다. 

### 2. 예약 요청 시 좌석 생성 전략

#### 장점:
- **자원 효율성**: 수요가 있을 때만 좌석을 생성하여 불필요한 자원을 절약할 수 있다.

#### 단점:
- **증가된 예약 지연**: 실시간으로 좌석을 생성하므로 예약 과정에서 지연을 초래할 수 있다.
- **복잡한 예약 로직**: 좌석 생성과 동시에 좌석 이용 가능성을 처리하는 부분에서 복잡도가 올라간다.


### 기술적 고려 사항

### 성능
- **사전 생성**: 예측 가능한 높은 수요와 많은 좌석이 있는 이벤트에 적합하다. 
- **요청 시 생성**: 수요가 불확실한 동적 환경에 이상적이다. 초기 오버헤드를 줄일 수 있지만, 동시성 처리가 필요하다.

### 데이터 일관성
- **사전 생성**: 좌석이 미리 정의되어 있고 상태만 변경되므로 데이터 일관성이 보장된다.
- **요청 시 생성**: 충돌을 피하고 좌석 가용성을 보장하기 위해 신중한 관리가 필요하다.

### 동시성
- **사전 생성**: 좌석 엔티티가 미리 존재하므로 동시성 관리가 간단하다.
- **요청 시 생성**: 동시에 들어오는 요청을 처리하고 중복 좌석 생성을 방지하기 위해 강력한 동시성 제어가 필요하다.

### 멘토링 피드백

허재 코치님의 공개 Q&A 세션에서 다음과 같은 피드백을 받았다.

1. **부하 관리**: 예약 요청 시 좌석을 생성하는 것은 피크 타임에 상당한 부하를 줄 수 있다. 
2. **동시성 문제**: 요청 시 좌석 생성을 처리하기 위한 동시성 처리가 필요하다. 
3. **가비지 데이터?**: 미리 생성해 놓는 것을 꼭 가비지로 볼 것인가? 통계 자료 활용 등에서도 보다 유연하고 쉽게 대응할 수 있지 않나?

### 결론

초기 생각했던 것은, 예약 API가 호출되는 시점은 이미 유량 제어가 된 시점이므로, 예약과 좌석을 결합하는 방식이 나쁘지 않을 것이라고 생각했다. 

하지만 그렇더라도 동시성 이슈나 복잡도 측면에서 난이도가 더 어려운 접근이라고 판단했다. 

미리 생성해두는 방식을 선택하기에 가장 마음에 걸렸던 부분은 `미리 생성해둔 좌석이 예마가 안되면?` `예를 들어 10만 건 예매 했는데 3만 건만 예약되면 7만 건은 가비지 아닌가?` 였다. 

그런데 그 자원이 서버에 그렇게 부담이 되는가를 고려했을 때, 그렇게 큰 부담은 아닐 수 있고, 또 가비지 데이터에 대해서는 사후 처리로 관리하기도 용이할 것이라 판단했다.

따라서 후자의 방식, 즉 예약 요청 시 좌석을 생성하는 방식을 선택하기로 결정했다.




## 5. 임시 예약과 예약에 대한 테이블 분리에 대한 쟁점 

임시 예약(temporary reservation)과 본 예약(confirmed reservation)을 어떻게 관리할 것인가에 대한 고민이 있다.

### 배경

사용자가 콘서트 예약을 요청하면, 서버는 먼저 임시 예약을 생성한다.
결제가 일정 시간 내에 완료되지 않으면, 이 임시 예약은 취소된다. 결제가 완료되면 임시 예약이 본 예약으로 확정된다. 
이때 임시 예약 관리에 대해, 두 가지 방식을 고려할 수 있다.
1. 예약 상태 필드를 통해 하나의 테이블에서 관리
2. 임시 예약과 본 예약을 별도의 테이블로 분리하여 관리

### 두 방식 비교

##### 상태 필드를 통한 관리
상태 필드를 통해 하나의 테이블에서 임시 예약과 본 예약을 관리하는 방법을 살펴보자.

장점은 구현이 간단하다는 것이다. 데이터베이스 스키마가 단순해지고, 단일 테이블에서 모든 예약 데이터를 조회할 수 있기 때문에 데이터 접근도 용이하다.

하지만 다음과 같은 단점들이 있다.

1. **모수 증가의 문제**: 테이블에 모든 예약 데이터를 저장할 경우, 데이터 모수 증가의 문제가 있다. 
2. **상태 관리 복잡성**: 상태 전이(transition)에 대한 일관성을 유지하도록 구현해야 한다. 

##### 테이블 분리를 통한 관리

별도 임시 예약 테이블로 분리하여 관리하는 방법은 관리 포인트를 명확히 분리하는 것이다. 

1. **데이터 관리 용이성**: 적은 모수의 장점이 있다. 관리와 성능에서 용이할 수 있다.

하지만, 이 방법 역시 다음과 같은 단점이 존재한다.

1. **구현의 복잡성**: 두 개의 테이블을 관리하기 위한 추가적인 로직이 필요하다. 
2. **동기화의 어려움**: 분리된 테이블 간의 데이터 일관성을 유지하기 위한 동기화 작업이 중요하다.

### 최종 결정: 테이블 분리
고민 끝에 임시 예약과 본 예약을 별도의 테이블로 분리하여 관리하는 방향으로 결정했다. 
주요 이유는 관리 포인트를 나누는 것이 좀 더 편하다고 생각했고, 또 확장성을 고려할 때 적은 모수라는 장점이 크다고 생각했다. 





## 6. MSA 아키텍처에서 '잔액'을 별도의 도메인 서비스로 분리하는 것에 대한 문제 



잔액 충전/조회 API를 개발하는 데 있어, 별도의 도메인 서비스로 'balance service'를 분리하는 것에 대해 검토해보았다.

### 도메인 모델링 관점에서의 적절성 검토

**도메인 모델링 견해 요약:**
- **Active Domain**: 사용자, 주문, 결제처럼 시스템에서 중요한 비즈니스 로직을 처리하며 독립적으로 행동하고 자신의 상태를 변경하거나 다른 객체와 상호작용하는 도메인
- **Passive Domain**: 콘서트 좌석이나 포인트처럼 주체적 도메인에 의해 상태가 변경되거나 영향을 받는 객체로, 독립적으로 비즈니스 기능을 제공하지 않는다.

**잔액(포인트) 도메인 분석:**
- 잔액(포인트)는 사용자의 행위(충전, 사용)에 의해 변경.
- 독립적으로 비즈니스 로직을 제공하기보다는 다른 도메인(예: 결제, 예약 등)의 행위에 의해 상태가 변경.

### 적절성 검토

**1. 독립적인 비즈니스 로직 제공 여부**
   - 잔액(포인트)는 자체적으로 독립적인 비즈니스 로직을 제공하지 않는다.
   - 충전 및 사용과 같은 행위는 사용자의 요청에 의해 발생하며, 자체적인 상태 변경은 없다.

**2. 시스템 내에서의 상호작용**
   - 잔액은 주로 결제 시스템과 상호작용하며, 결제 프로세스의 일부로 동작한다.
   - 잔액을 독립적인 서비스로 분리하면, 결제와의 상호작용에서 복잡성이 증가할 수 있다. 특히 트랜잭션 관리나 데이터 일관성 유지를 위한 추가적인 노력이 필요하다.

**3. 관리 및 유지보수**
   - 별도의 'balance service'로 분리하면, 단일 책임 원칙(Single Responsibility Principle)과 마이크로서비스 아키텍처의 장점을 살릴 수 있다.
   - 그러나, 시스템 복잡성이 증가하고, 여러 서비스 간의 통신 비용 및 오버헤드가 발생할 수 있다.

### 결론

잔액(포인트) 관리 기능이 시스템 내에서 중요한 비즈니스 로직을 독립적으로 제공하지 않기 때문에, 엄밀히 말하면 Passive Domain으로 볼 수 있다. 
따라서 별도의 도메인 서비스로 'balance service'를 분리하는 것은 필요에 따라 결정할 수 있다.

- **단순한 시스템**: 시스템 복잡성을 낮추고, 성능 이슈를 줄이기 위해 잔액 관리를 결제 서비스 내에 포함시키는 것이 더 효율적일 수 있다.
- **복잡한 시스템**: 시스템의 규모가 커지고, 잔액 관련 비즈니스 로직이 복잡해진다면 별도의 서비스로 분리하여 관리하는 것이 바람직할 수 있다.

현재 요구 사항과 시스템의 복잡성을 고려하여, 잔액 관리를 결제 서비스와 통합하여 구현하는 것이 적절하다고 판단. 
이후 시스템이 확장되고, 잔액 관련 로직이 복잡해진다면 그때 별도의 서비스로 분리하는 것을 고려하자.







</details>
















<details>



<summary><b>설계 및 구상</b></summary>


<details>
<summary><b>개략적 규모 측정</b></summary>


## 시스템 규모 가정

- **총 유저 수**: 1,000,000명
- **수용 가능한 관객 수**: 100,000명
- **대기열 진입 유저 수**: 110,000명 (수용 가능한 관객 수의 110%)

## 유량 제어 메커니즘

- **처리 방식**: 은행 창구식 처리 방식을 채택하여 항상 활성 상태 인원수를 유지한다. (c.f. vs. 놀이공원 방식)
- **처리열 동시 접속 최대 인원수**: 6,000명
- **인원별 활성 상태 최대 시간**: 5분
- **인원별 활성 상태 시간 연장 가능 여부**: 향후 검토

## 트래픽 분석

1. **대기열 생성 및 관리**
  - 예상 트래픽: 전체 유저가 티켓팅에 동시에 접속한다고 가정.
  - 접속 희망 최대 유저 수: 1,000,000명
  - 대기열 진입 최대 유저 수: 110,000명
2. **대기열 인입 및 처리**
  - **인입율**: 초기 접속 유저의 인입 처리율은 분당 10,000명
  - **처리율**: 대기열에서 처리열로 분당 최대 6,000명의 유저 이동 가능.
  - **처리 과정**: 대기열에서 한 명이 처리열로 이동하면, 바로 다음 유저가 대기열에 추가된다.
3. **유량 제어**
  - **대기열 유량 제어**: 대기열에서는 최대 수용 인원 수를 100,000명으로 제한함으로써 대기열 서버의 부하를 관리한다.
  - **처리열 유량 제어**: 처리열에서는 분당 최대 6,000명의 요청을 처리함으로써 서버의 부하를 관리한다.
  - **구체적인 처리 메커니즘**: 유저의 상태를 추적하고 관리하는 방식은 다음과 같다.
    1. **상태 코드 부여**: 각 유저에게 고유의 토큰을 부여하여 대기열과 처리열에서 상태를 추적한다. 다음과 같은 상태 코드를 사용할 수 있다.
      - `대기`: 대기열에서 기다리고 있는 상태
      - `처리 중`: 처리열로 이동하여 활성 상태인 유저
      - `완료`: 예약을 성공적으로 마친 상태
      - `재진입`: 5분의 시간이 만료되어 대기열로 다시 돌아간 상태
    2. **상태 전이 관리**: 시스템은 일정 시간 간격으로 유저의 상태를 점검한다. 이때 상태 코드 혹은 TTL를 이용하여 전이를 관리한다. (시간 기반 트리거링 + 이벤트 기반 트리거링)
  - **시간 제약**: 처리열로 이동한 유저는 최대 시간(예: 5분) 동안 예약을 시도할 수 있다.
  - **처리 후 대기열 재진입**: 예약이 완료되지 않은 유저는 5분 후에 처리열에서 제거되며, 대기열로 재진입하여 다시 대기열 처리 과정을 반복한다.

## TPS 및 QPS 산정

1. **대기열 부분**
- **대기열 진입 TPS**: 전체 유저가 1분 동안 접속을 시도할 경우, 분당 10,000명씩 대기열에 진입한다고 가정.
- 대기열 진입 최대 TPS: 10,000명 ÷ 60초 = 약 167 TPS
- **대기열 상태 조회 TPS**: 최대 110,000명의 유저가 대기열 상태를 확인하기 위해 주기적으로 요청을 보낸다고 가정.
- 대기열 상태 조회 주기: 10초 간격
- 대기열 상태 조회 API Call MAX: 110,000명 ÷ 10초 = 11,000 API Call/초
- 대기열 상태 조회 QPS: 11,000 QPS
1. **처리열 부분**
  - **처리열 진입 TPS**: 처리열 MAX 수용치는 6,000명임에 따라 진입 요청의 최대 값은 6,000/분.
    - 처리열 진입 TPS: 6,000명 ÷ 60초 = 100 TPS
  - **처리열 상태 조회 TPS**: 처리열에 있는 유저들이 예약 상태를 주기적으로 확인한다고 가정.
    - 처리열 상태 조회 주기: 10초 간격
    - 처리열 상태 조회 API Call MAX: 6,000명 ÷ 10초 = 600 API Call
    - 처리열 상태 조회 QPS: 600 QPS
2. **예약 및 결제 (본 API) 부분**
  - 각 단계별 유저의 분포를 고려하여 계산.
  - **예약 가능 좌석 조회 TPS/QPS**: 처리열 유저들의 50%가 조회 요청한다고 가정.
    - 예약 가능 좌석 조회 QPS: 6,000명 × 50% ÷ 60초 = 50 QPS
  - **예약 상태 조회 TPS/QPS**: 처리열 유저들의 30%가 예약 상태 조회 요청한다고 가정.
    - 예약 상태 조회 QPS: 6,000명 × 30% ÷ 60초 = 30 QPS
  - **결제 처리 TPS/QPS**: 처리열 유저들의 20%가 결제 요청한다고 가정.
    - 결제 처리 TPS: 6,000명 × 20% ÷ 60초 = 20 TPS

## 이탈율 고려

실제 예약 단계에서 이탈되는 유저가 5%라고 가정하면, 6,000명의 유저가 처리열로 이동할 때 실제로 예약을 완료하는 유저는 95%인 5,700명.

## 100,000 좌석 마감에 걸리는 시간

- 매 분마다 6,000명이 처리열로 이동하고 이 중 5,700명이 실제로 예약을 완료한다고 가정.
- 따라서, 매 분 5,700명이 예약을 완료.
- 100,000 좌석을 예약하려면 100,000 ÷ 5,700 ≈ 17.54분이 필요.

## 물리 스펙 검토

### 어플리케이션 서버 성능 및 스펙 가정

어플리케이션 서버의 경우, 높은 CPU 성능을 가진 저비용 인스턴스를 여러 개 사용하는 것이 효과적일 수 있다. 또한, 고성능 비동기 처리를 위해 웹플럭스(WebFlux)나 Netty와 같은 기술을 고려할 수 있다.

- **어플리케이션 서버 스펙**: AWS t3.medium 인스턴스(vCPU 4개, 메모리 2GB)
- **스프링 부트**: 3.3.1
- **Tomcat**: 9.0
- **TPS**: 인스턴스 당 약 100 TPS (일반적인 웹 애플리케이션 기준)
- **QPS**: 인스턴스 당 약 1,000 QPS (일반적인 웹 애플리케이션 기준)

### DB 서버 성능 및 스펙 가정

- **DB 서버 스펙**: AWS RDS db.m5.large (vCPU 2개, 메모리 8GB)
- **MySQL 버전**: 8.0
- **DB TPS**: 인스턴스 당 약 1,000 TPS (읽기/쓰기 혼합 워크로드 기준)
- **DB QPS**: 인스턴스 당 약 10,000 QPS (읽기 기준)

## 물리 스펙 검토

### 어플리케이션 서버 TPS 및 QPS 감당 가능성 검토

1. **대기열 부분**
  - 대기열 진입 TPS: 167 TPS
    -> 2 x t3.medium 인스턴스 (100 TPS/인스턴스)
  - 대기열 상태 조회 QPS: 11,000 QPS
    -> 11 x t3.medium 인스턴스 (1,000 QPS/인스턴스)
2. **처리열 부분**
  - 처리열 진입 TPS: 100 TPS
    -> 1 x t3.medium 인스턴스
  - 처리열 상태 조회 QPS: 600 QPS
    -> 1 x t3.medium 인스턴스
3. **예약 및 결제 처리**
  - 예약 가능 좌석 조회 QPS: 50 QPS
    -> 1 x t3.medium 인스턴스
  - 예약 상태 조회 QPS: 30 QPS
    -> 1 x t3.medium 인스턴스
  - 결제 처리 TPS: 20 TPS
    -> 1 x t3.medium 인스턴스

### DB 서버 TPS 및 QPS 감당 가능성 검토

1. **대기열 상태 조회**
  - 읽기: 11,000 QPS
    -> 2 x db.m5.large 인스턴스 (10,000 QPS/인스턴스)
  - 쓰기: 167 TPS
    -> 1 x db.m5.large 인스턴스
2. **처리열 상태 조회**
  - 읽기: 600 QPS
    -> 1 x db.m5.large 인스턴스
  - 쓰기: 100 TPS
    -> 1 x db.m5.large 인스턴스
3. **예약 및 결제 처리**
  - 읽기: 80 QPS
    -> 1 x db.m5.large 인스턴스
  - 쓰기: 20 TPS
    -> 1 x db.m5.large 인스턴스

### 트래픽 처리를 위한 총 필요 인스턴스

- **어플리케이션 서버**:

  2 (대기열 진입) + 11 (대기열 상태 조회) + 1 (처리열 진입) + 1 (처리열 상태 조회) + 1 (예약 가능 좌석 조회) + 1 (예약 상태 조회) + 1 (결제 처리) = **18 인스턴스**

- **DB 서버**:

  2 (대기열 상태 조회 읽기) + 1 (대기열 상태 조회 쓰기) + 1 (처리열 상태 조회 읽기) + 1 (처리열 상태 조회 쓰기) + 1 (예약 및 결제 처리 읽기) + 1 (예약 및 결제 처리 쓰기) = **7 인스턴스**


## 비용 산정

### 어플리케이션 서버 비용

- **필요한 인스턴스 수**: 18개
- **시간당 비용**: $0.0416 (AWS t3.medium 인스턴스 기준)
- **월간 비용 계산**:
  - 시간당 비용: 18 인스턴스 x $0.0416/인스턴스 = $0.7488
  - 일일 비용: $0.7488 x 24시간 = $17.9712
  - 월간 비용: $17.9712 x 30일 = $539.136

### DB 서버 비용

- **필요한 인스턴스 수**: 7개
- **시간당 비용**: $0.096 (AWS RDS db.m5.large 인스턴스 기준)
- **월간 비용 계산**:
  - 시간당 비용: 7 인스턴스 x $0.096/인스턴스 = $0.672
  - 일일 비용: $0.672 x 24시간 = $16.128
  - 월간 비용: $16.128 x 30일 = $483.84

### 총 비용

- **어플리케이션 서버 비용**: $539.136/월
- **DB 서버 비용**: $483.84/월
- **총 월간 비용**: $539.136 + $483.84 = $1,022.976/월

## 유의미한 도출

병목 예상 구간 -> 대기열 관리, 특히 대기열 조회 부분!

</details>

<details>
<summary><b>어플리케이션 로깅 시스템</b></summary>



# 어플리케이션 로깅 시스템

이전에 김영한님의 강의를 듣다가 AOP를 이용한 어플리케이션 로깅 코드를 배운 적이 있다.
현업에서 계속 일을 하면서 해당 코드를 고도화시켜 최적화하고 맞춤형으로 사용하고 있다.
본 프로젝트에서도 AOP 기반의 로깅 시스템을 구현하여 사용했다.

## 로깅의 니즈 

다음과 같은 로깅의 니즈를 만족시켜준다.

1. 로깅 자체가 어플리케이션의 성능을 떨어뜨려서는 안 된다.

로깅이 어플리케이션의 성능에 영향을 주지 않도록 Logback의 비동기 로깅 설정을 사용할 수 있다. 
비동기 로깅을 통해 로깅 작업이 별도의 스레드에서 처리되어 애플리케이션의 주요 작업 흐름에 영향을 주지 않게 된다.

   ```xml
   <appender name="ASYNC-STDOUT" class="ch.qos.logback.classic.AsyncAppender">
       <param name="BufferSize" value="8196"/>
       <appender-ref ref="STDOUT"/>
   </appender>

   <root level="INFO">
       <appender-ref ref="ASYNC-STDOUT"/>
   </root>
   ```

2. 필요한 로깅을 해야 한다. 
   - 비즈니스 로직 수행에서 각 메서드의 시작 시점과 반환 시점 (+파라미터, 시간 측정)
   - 요청(request) 및 응답(response)시 파라미터 로깅 (컨트롤러)
   - 예외 발생 시
   - 특정 구간에서 추가 필요시



## 구현 코드 

### LogTrace 애노테이션
`LogTrace` 애노테이션은 메서드 수준에서 로깅을 활성화하기 위해 사용된다. 기본적으로 Aspect로 동작하지만 특정 구간에서 추가 필요시 사용될 수 있다. 

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogTrace {
}
```

### LogTracer 인터페이스
`LogTracer` 인터페이스는 로깅의 기본 구조를 정의한다. `begin`, `end`, `exception` 메서드를 통해 로깅을 시작하고 끝내며 예외 상황을 처리한다.

```java
public interface LogTracer {
    TraceStatus begin(String message);
    void end(Object result, TraceStatus status);
    void exception(Object result, TraceStatus status, Exception e);
}
```

### ThreadLocalLogTracer 클래스
`ThreadLocalLogTracer` 클래스는 `LogTracer` 인터페이스를 구현하며, 로깅의 세부 사항을 정의한다.
각 스레드에 독립적인 `TraceId`를 관리하며, 시작, 종료, 예외 상황에 따른 로깅을 처리한다.

```java
@Slf4j
@Component
public class ThreadLocalLogTracer implements LogTracer {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EXCEPTION_PREFIX = "<X-";

    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();
    private ThreadLocal<Boolean> isExceptionLogged = ThreadLocal.withInitial(() -> false);

    @Override
    public TraceStatus begin(String message) {
        isExceptionLogged.set(false);
        syncTraceId();
        TraceId traceId = getCurrentTraceId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

        return new TraceStatus(traceId, startTimeMs, message);
    }

    @Override
    public void end(Object result, TraceStatus status) {
        complete(result, status, null);
    }

    @Override
    public void exception(Object result, TraceStatus status, Exception exception) {
        if (!isExceptionLogged.get()) {
            log.info("[{}] {}{} time={}ms ex={}", status.getTraceId().getId(), addSpace(EXCEPTION_PREFIX, status.getTraceId().getLevel()),
                limitMessage(status.getMessage()), System.currentTimeMillis() - status.getStartTimeMs(), exception.toString());
            isExceptionLogged.set(true);
        }
        releaseTraceId();
    }

    private void complete(Object result, TraceStatus status, Exception exception) {
        long resultTimeMs = System.currentTimeMillis() - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (exception == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()),
                limitMessage(status.getMessage()), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EXCEPTION_PREFIX, traceId.getLevel()),
                limitMessage(status.getMessage()), resultTimeMs, exception.toString());
        }
        releaseTraceId();
    }
	
    // 기타 메서드 생략
}
```

### GlobalTraceHandler 클래스
`GlobalTraceHandler` 클래스는 AOP를 활용하여 전역적인 로깅을 처리한다. 
각 메서드 호출 전후에 로깅을 수행하며, 예외 발생 시에도 로깅을 처리한다.

```java
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GlobalTraceHandler {

    private final LogTracer logTracer;

    @Around("all()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        Object result = null;

        try {
            status = logTracer.begin(joinPoint.getSignature().toShortString());

            if (isAnnotationPresent(joinPoint, RestController.class)) {
                result = logWithParameters(joinPoint, status);
            } else {
                result = joinPoint.proceed();
                logTracer.end(result, status);
            }
            return result;
        } catch (Exception exception) {
            logTracer.exception(null, status, exception);
            throw exception;
        }
    }

    private Object logWithParameters(ProceedingJoinPoint joinPoint, TraceStatus status) throws Throwable {
        log.info("Incoming Request Body: {}", Arrays.toString(joinPoint.getArgs())); // 들어오는 DTO 로깅
        Object result = joinPoint.proceed();
        log.info("Outgoing Response Body: {}", result); // 나가는 DTO 로깅
        logTracer.end(result, status);
        return result;
    }

    // 기타 메서드 생략
}
```

## 실제 로깅 내용 예시

아래는 실제 애플리케이션 실행 중 로깅된 내용의 예시이다. 각 요청의 시작과 종료 시점, 그리고 요청 및 응답 파라미터가 로깅된 모습을 확인할 수 있다.

```plaintext
00:16:21.949 [INFO ] [http-nio-auto-1-exec-1] [i.a.c.logtrace.ThreadLocalLogTracer] - [a09595d2] |-->TokenInterceptor.preHandle(..)
00:16:21.951 [INFO ] [http-nio-auto-1-exec-1] [i.a.c.logtrace.ThreadLocalLogTracer] - [a09595d2] |<--TokenInterceptor.preHandle(..) time=3ms
00:16:22.043 [INFO ] [http-nio-auto-1-exec-1] [i.a.c.logtrace.ThreadLocalLogTracer] - [411b92ba] |-->BalanceAndPaymentOrchestrationController.chargeBalance(..)
00:16:22.046 [INFO ] [http-nio-auto-1-exec-1] [i.a.c.l.impl.GlobalTraceHandler] - Incoming Request Body: [UserBalanceChargeRequest(userId=1, amount=500)]
00:16:22.047 [INFO ] [http-nio-auto-1-exec-1] [i.a.c.logtrace.ThreadLocalLogTracer] - [411b92ba] |   |-->BalanceChargeFacade.charge(..)
00:16:22.065 [INFO ] [http-nio-auto-1-exec-1] [i.a.c.logtrace.ThreadLocalLogTracer] - [411b92ba] |   |   |-->SimpleBalanceService.charge(..)
...
00:16:22.317 [INFO ] [http-nio-auto-1-exec-1] [i.a.c.logtrace.ThreadLocalLogTracer] - [411b92ba] |<--BalanceAndPaymentOrchestrationController.chargeBalance(..) time=275ms
```

## 한계점 및 해결 방안

개인적으로는 개별 log 파일에서 리눅스 커맨트를 활용해서 찾는 것을 선호하는 편이다.
위의 로깅 시스템으로 실제 서비스를 운영하면서 불편하거나 놓치는 케이스는 없었던 것 같다.

하지만 다음과 같은 이유로 어플리케이션 수준의 로깅을 넘어서, 중앙화된 로깅 시스템이 필요하다. 

- **로그의 양과 성능 이슈**: SLF4J, Log4j, Logback, Winston 만으로는 한계가 있다. 로그가 많이 쌓이면 시스템 부하가 증가할 수 있다.
- **동시성 및 서버 부하**: 서버가 늘어나면 중앙화된 방식의 성능 좋은 로깅 시스템이 필요하다. 이를 위해 ELK Stack (Elasticsearch, Logstash, Kibana) 또는 EFK (Elasticsearch, Fluentd, Kibana)를 이용한 중앙화가 필요하다.

현재 다니는 회사에서는 Greylog를 사용하여 중앙화된 로깅 시스템을 운영하고 있다.
어쩌면 중앙화된 로그를 잘 활용하지 못하는 것일 수도 있다.

본 프로젝트의 고도화 시점에 ELK? EFK 스택을 붙여서 로깅 중앙화를 시도해보고자 한다!

</details>




<details>
<summary><b>예외 처리 및 알림 프로세스</b></summary>

비동기 예외와 전체 애플리케이션 예외를 포착하고, 로깅 및 알림을 별도로 관리하는 방식이 필요하여 구현한 예외 처리 및 알림 프로세스이다.

## 예외 처리 시스템

### 1. Global Exception Handler

`@RestControllerAdvice`와 `ResponseEntityExceptionHandler`를 활용한 전역 예외 처리를 구현 예시는 다음과 같다. 

모든 예외를 포착하고 적절한 로깅 및 응답을 제공한다.



<details>
<summary><b>구현 예시</b></summary>

```java
@RestControllerAdvice
@Slf4j
class ApiControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Object handleException(Exception ex) {
        log.error("processUnDefinedErrors: {}", ex.getMessage());
        return new CommonApiResponse<>(UNKNOWN_ERROR);
    }

    @ExceptionHandler(ServerException.class)
    public Object processServerException(ServerException serverException) {
        log.error("ServerException: {}", serverException.getMessage());
        return new CommonApiResponse<>(serverException.getCode());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public Object processNotFoundException(ServerException serverException) {
        log.error("ServerException: {}", serverException.getMessage());
        return new CommonApiResponse<>(NO_CONTENT);
    }
}
```
</details>

### 2. Async Exception Handler

비동기 메서드에서 발생하는 예외를 처리하기 위한 `AsyncUncaughtExceptionHandler`의 구현 예시는 다음과 같다.

비동기 메서드에서 발생하는 예외는 일반적인 예외 처리 흐름에 포함되지 않으므로, 이를 별도로 처리해야 한다.




<details>
<summary><b>구현 예시</b></summary>

```java
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

    private final GlobalExceptionAlertInternalPublisher globalExceptionAlertInternalPublisher;

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        log.error("Thread {} threw exception: {}", Thread.currentThread().getName(), throwable.getMessage());
        globalExceptionAlertInternalPublisher.publish(new GlobalExceptionAlertEvent("uncaughtException", throwable));
    }
}
```
</details>


### 3. AOP를 통한 예외 포착

애플리케이션 전역의 비동기 및 동기 메서드에서 발생하는 예외를 포착하기 위해 AOP를 활용했다.

<details>
<summary><b>구현 예시</b></summary>

```java
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GlobalExceptionAlertAspect {

    private final GlobalExceptionAlertInternalPublisher globalExceptionAlertInternalPublisher;
    private final Set<Throwable> markedExceptions = Collections.newSetFromMap(new WeakHashMap<>());

    @Pointcut("execution(public * io.reservationservice.api..*(..))")
    void apiMethods() {}

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Async)")
    void async() {}

    @Pointcut("!async()")
    void notAsync() {}

    @AfterThrowing(pointcut = "apiMethods() && notAsync()", throwing = "throwable")
    public void alertApplicationWideMethods(JoinPoint jp, Throwable throwable) {
        if (!isMarkedException(throwable)) {
            log.debug("(alertApplicationWideMethods) Class = {}, Method = {}, Cause = {}", jp.getSignature().getDeclaringTypeName(), jp.getSignature().getName(), throwable.getLocalizedMessage());
            globalExceptionAlertInternalPublisher.publish(new GlobalExceptionAlertEvent(jp.getSignature().getName(), throwable));
            markException(throwable);
        }
    }

    private boolean isMarkedException(Throwable throwable) {
        return markedExceptions.contains(throwable);
    }

    private void markException(Throwable throwable) {
        markedExceptions.add(throwable);
    }
}
```
</details>

이 클래스는 특정 포인트컷을 정의하고, 예외가 발생했을 때 이를 포착하여 알림을 발행한다. 
여기서 정의된 포인트컷은 `apiMethods`와 `async`이며, 비동기 메서드를 제외한 모든 메서드에서 발생한 예외를 포착한다. 
포착된 예외는 `GlobalExceptionAlertInternalPublisher`를 통해 알림으로 발행된다.

## 알림 시스템


`message-service`는 알림을 관리하는 별도의 마이크로서비스이다.
현재는 슬랙을 통해 알림을 지원하며, 추후 다른 알림 채널을 추가할 수 있다.

슬랙 메시지 예약 기능을 구현하여, 메시지를 예약하고 별도의 스레드에서 이를 폴링하여 발송한다.

### 설계 철학
알림 시스템은 메시지 서비스에서 독립적으로 동작하며, 사용자 요청에 대한 빠른 응답을 보장한다. 
알림을 즉시 발송하는 대신, 먼저 저장해 두고 내부에서 폴링(polling)을 통해 순차적으로 발송하는 방식을 채택했다. 
이러한 설계로 성능을 최적화하며, 메시지 발송 실패에 따른 재시도 등의 확장 로직 가능성을 열어둔다. 

### 1. 슬랙 메시지 서비스

`message service`의 슬랙 메시지 예약 및 발송을 처리하는 서비스이다.

<details>
<summary><b>구현 예시</b></summary>

```java
@Service
@RequiredArgsConstructor
public class SlackMessageService {

    private final SlackChannelRegistrar slackChannelRegistrar;
    private final SlackChannelRetriever slackChannelRetriever;
    private final SlackMessageSender slackMessageSender;
    private final SlackMessageReservationManager slackMessageReservationManager;

    public SlackChannelRegistrationResponse register(SlackChannelRegistrationRequest request) {
        return SlackChannelRegistrationResponse.from(slackChannelRegistrar.save(request.toCommand()));
    }

    public SlackMessageReserveResponse reserve(SlackMessageReserveRequest slackMessageReserveRequest) {
        SlackChannelInfo slackChannelInfo = slackChannelRetriever.retrieveSlackChannelByName(slackMessageReserveRequest.getChannelName());
        return SlackMessageReserveResponse.from(slackMessageReservationManager.reserve(slackMessageReserveRequest.toCommand().withChannelInfo(slackChannelInfo)));
    }

    public void sendReservedMessages() {
        SlackMessageReserveInfo slackMessageReserveInfo = slackMessageReservationManager.popNextReservedMessage();
        if (slackMessageReserveInfo.isEmpty()) {
            return;
        }
        slackMessageSender.sendAsync(slackMessageReserveInfo.toCommand());
        slackMessageReservationManager.markAsSentAsync(slackMessageReserveInfo.id());
    }
}
```

</details>


### 2. 슬랙 메시지 발송기

슬랙 메시지를 비동기적으로 발송하는 컴포넌트이다.

<details>
<summary><b>구현 예시</b></summary>

```java
@Component
@RequiredArgsConstructor
public class SlackMessageSender {

    private final Slack slack = Slack.getInstance();

    @SneakyThrows
    @Async("slackMessageSenderExecutor")
    public void sendAsync(SlackMessageSendCommand command) {
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
            .token(command.getToken())
            .channel(command.getChannelId())
            .text(command.getMessage())
            .build();

        slack.methods().chatPostMessage(request);
    }
}
```

</details>

### 3. 폴링 프로세서

예약된 메시지를 주기적으로 폴링하여 발송하는 프로세서이다.

<details>
<summary><b>구현 예시</b></summary>

```java
@Component
@RequiredArgsConstructor
public class SlackMessagePollingProcessor {

    private final SlackMessageService slackMessageService;

    @Value("${slack.message.polling.interval:100}")  // 폴링 간격 (기본값: 100ms)
    private long pollingInterval;

    public void startPolling() {
        while (true) {
            try {
                slackMessageService.sendReservedMessages();
                Thread.sleep(pollingInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
```
</details>


<details>
<summary><b>메시지 예시</b></summary>


![slack-alert.png](documents%2Fslack-message-captures%2Fslack-alert.png)

</details>


</details>


</details>


<details>
<summary><b>회고</b></summary>

## 1~5 주차 회고

### 1주차: TDD
첫 주차에는 TDD(Test Driven Development)에 대해 집중적으로 학습하고 실습했다. TDD는 코드를 작성하기 전에 테스트를 먼저 작성하는 방법론으로, 이를 통해 코드의 안정성과 유지보수성을 높일 수 있음을 알게 되었다.

**TDD 실습 내용**:
1. **포인트 충전 API 개발**: 특정 사용자에게 포인트를 충전하는 API를 구현했다. 이 과정에서 단위 테스트와 통합 테스트의 중요성을 체감할 수 있었다.
2. **동시성 문제 해결**: 동시에 여러 사용자가 포인트를 충전하거나 사용할 때 발생하는 문제를 해결하기 위해 다양한 방법을 탐구했다.

**주요 포인트**:
- 테스트 코드를 작성함으로써 코드의 신뢰성을 확보
- 동시성 이슈를 해결하는 다양한 방법(락 기반 동기화, 락 프리 접근 방식 등)을 학습
- 실제로 겪었던 동시성 문제와 이를 해결하기 위한 과정을 통해 실무에서의 적용 방안을 모색

**회고**:
- TDD를 통해 코드를 작성하는 습관을 들이면서 코드의 품질이 높아짐을 느꼈다.
- 동시성 문제를 해결하는 다양한 방법을 학습하면서, 실제 문제를 해결하는 데 필요한 사고방식과 접근 방식을 키울 수 있었다.
- 앞으로의 프로젝트에서도 TDD를 적극 활용할 계획이다.

### 2주차: 아키텍처 고민
두 번째 주차에서는 특강 신청 서비스를 구현하면서 아키텍처 설계와 관련된 고민을 많이 했다. 레이어드 아키텍처와 클린 아키텍처의 혼합을 추구하며 실용적이면서도 유연하고 안정적인 아키텍처 지향.

**특강 신청 서비스**:
- **특강 신청 API**: 선착순으로 특정 사용자에게 특강을 신청하는 API를 구현. 
- **특강 신청 여부 조회 API**: 특정 사용자가 특강 신청을 완료했는지 여부를 조회하는 API를 구현.

**주요 포인트**:
- 대체키 사용: 보안성과 유지보수성을 높이기 위해 대체키를 도입해 보았다.
- 파사드 레이어의 필요성: 서비스 간의 순환 참조 문제를 해결하기 위해 파사드 패턴을 도입했다. 
- 레이어드 아키텍처의 단점과 클린 아키텍처의 필요성: 전통적인 레이어드 아키텍처의 단점을 극복하기 위해 클린 아키텍처를 도입했다. 이를 통해 도메인 중심의 설계를 구현하고, 코드의 모듈성과 유지보수성을 높일 수 있다.

**회고**:
- 아키텍처 설계에 대한 깊이 있는 고민을 통해 코드의 구조를 더 체계적으로 구성.
- 파사드 패턴을 도입하면서 서비스 간의 의존성을 줄이고, 코드의 유연성 증대.
- 클린 아키텍처를 도입하면서 도메인 중심의 설계를 구현.

**기술적 고민들**:
- 대체키 도입으로 보안성과 유지보수성 향상
- 파사드 패턴을 통해 서비스 간 의존성 해결
- 클린 아키텍처로 도메인 중심 설계 구현



### 3주차: 시스템 설계 및 구상
세 번째 주차에는 콘서트 예약 시스템을 본격적으로 설계하고 구상하는 작업을 진행했다. 다이어그램 작성, 마일스톤 설정, ERD 설계 등 다양한 설계 작업을 수행했다.

**시스템 설계 및 구상**:
- **다이어그램 작성**: 시스템의 전반적인 구조를 시각적으로 표현하기 위해 다양한 다이어그램을 작성. 
- **마일스톤 설정**: 프로젝트의 주요 마일스톤을 설정하여 각 단계별 목표를 명확히 함. 
- **ERD 설계**: 데이터베이스의 구조를 설계하기 위해 ERD를 작성. 

**주요 포인트**:
- 시스템의 전반적인 흐름을 시각적으로 이해할 수 있었다.
- 프로젝트 진행 상황을 체계적으로 관리할 수 있었다.
- 효율적인 데이터베이스 구조를 설계할 수 있었다.

**회고**:
- 다이어그램 작성을 통한 전체 플로우 구상
- 마일스톤 설정을 통해 프로젝트 진행 상황에 대한 스케줄링 관리 
- ERD 설계를 통해 효율적인 데이터베이스 구조 설계

### 4주차: 본격적인 서버 개발 (1)
네 번째 주차에는 본격적인 서버 개발을 시작했다. 주요 기능들을 구현하고, 단위 테스트를 작성하며, 코드의 안정성을 확보하는 작업을 진행했다.

**서버 개발 내용**:
- **유저 토큰 발급 API 구현**: 유저가 대기열에 진입하기 위한 토큰을 발급받는 API를 구현. 
- **예약 가능 날짜/좌석 조회 API 구현**: 사용자가 예약 가능한 날짜와 좌석을 조회할 수 있는 API를 구현. 
- **좌석 예약 요청 API 구현**: 사용자가 원하는 날짜와 좌석을 선택하여 예약 요청을 보낼 수 있는 API를 구현. 
- **잔액 충전/조회 API 구현**: 사용자가 예약에 필요한 잔액을 충전하고 조회할 수 있는 API를 구현. 
- **결제 API 구현**: 사용자가 예약한 좌석을 결제할 수 있는 API를 구현. 

**회고**:
- 유저 토큰 발급 기반의 대기열 시스템 이해 
- 유량 제어에 대한 기술적 고민들 

### 5주차: 본격적인 서버 개발 (2)
다섯 번째 주차에는 서버 개발을 계속 진행하며, 기능 고도화 및 리팩토링 작업을 수행했다. 코드의 품질을 높이고, 성능을 최적화하는 작업을 진행했다.

**서버 개발 내용**

- **코드 리팩토링 및 최적화**: 기존에 구현된 코드를 리팩토링하여 미흡한 기능을 보완하고 코드의 품질을 향상시킬 수 있었다.
- **에러 핸들링 및 예외 처리 강화**: 에러 핸들링과 예외 처리 로직을 강화하여 서버 운영시 필요한 안정성을 확보할 수 있었다.



</details>







<details>
<summary><b>WIL(What I Learend)</b></summary>




<details>
<summary><b>본 프로젝트에서 jwt토큰을 사용하지 않는 이유(feat. Opaque 토큰)</b></summary>

# 본 프로젝트에서 jwt토큰을 사용하지 않는 이유(feat. Opaque 토큰)

JWT (JSON Web Token)은 사용자 인증 및 정보 교환에 자주 사용되는 방식입니다. 현재 시나리오에서 JWT를 사용하는 것이 적합하지 않은 몇 가지 이유가 있습니다.

### 1. 동적 정보 업데이트의 어려움
JWT는 토큰 생성 시점에 정보를 인코딩하여 발행됩니다. 토큰이 발행된 후에는 토큰에 포함된 정보를 변경할 수 없습니다. 현재 시나리오에서는 사용자의 대기열 순서, 잔여 시간 등과 같은 동적 정보가 자주 변경됩니다. 이러한 정보를 실시간으로 업데이트하고 관리하는데 JWT를 사용하면 너무 많은 오버헤드가 예상됩니다.

### 2. 보안 및 유효성 검사
JWT는 클라이언트가 서버로부터 발급받아 이후 요청 시 포함하여 사용하는 방식입니다. 만약 대기열 토큰이 JWT로 구현된다면, 클라이언트가 토큰을 발급받은 이후에도 토큰의 유효성을 지속적으로 검증하고 대기열 상태를 업데이트하는 것이 어렵습니다. 대기열 시스템에서는 각 요청마다 토큰의 유효성을 확인하고, 대기열 상태를 갱신하며, 동시성 이슈를 관리해야 합니다. JWT는 이러한 요구사항을 충족하기 어렵습니다.

### 3. 토큰 만료 및 갱신
JWT는 만료 시간을 설정할 수 있지만, 만료된 토큰을 갱신하는 과정이 복잡할 수 있습니다. 대기열 시스템에서는 토큰의 유효성을 지속적으로 관리해야 하며, 토큰 만료 시 새로운 토큰을 발급받아야 합니다. 이러한 과정에서 발생할 수 있는 복잡성과 보안 문제를 고려할 때, JWT보다는 상태 저장 방식이 더 적합합니다.


## JWT에 대비되는 Opaque 토큰

Opaque 토큰은 JWT와 대비되는 다음과 같은 특징으로 인해 현재 시나리오에서 더 적합합니다.

1. **불투명성**:
- Opaque 토큰은 클라이언트가 그 내용을 해석할 수 없는 무작위 문자열로 구성됩니다. 토큰 자체에 정보를 포함하지 않으므로, 클라이언트가 토큰의 내용을 알 수 없습니다.

2. **상태 저장 방식 및 실시간성**:
- Opaque 토큰은 서버 측에서 상태 정보를 저장하고 관리합니다. 서버는 토큰과 연관된 모든 정보를 데이터베이스나 메모리에 저장하며, 클라이언트의 요청 시 이 정보를 조회하여 유효성을 검사합니다. 이는 동적 정보의 실시간 업데이트를 가능하게 합니다.
- Opaque 토큰은 서버 측에서 중앙 집중적으로 관리되므로, 토큰의 유효 기간과 상태를 동적으로 조정할 수 있습니다. 이는 대기열 시스템에서 각 사용자의 상태를 실시간으로 관리하고, 필요한 경우 토큰을 갱신하거나 무효화하는 데 적합합니다.

3. **유효성 검사**:
- 각 요청마다 서버에서 토큰의 유효성을 검사합니다. 서버는 내부 상태를 기반으로 토큰의 유효성을 확인하고, 필요할 경우 토큰을 무효화하거나 갱신할 수 있습니다. 이는 대기열 상태와 같은 동적 정보를 효과적으로 관리하는 데 유리합니다.

Opaque 토큰은 클라이언트가 토큰의 내용을 해석할 수 없게 하여 보안을 강화하고, 서버 측에서 상태 정보를 중앙에서 관리할 수 있도록 하는 방식입니다.
이러한 특성으로 사용자 대기열 관리와 같은 시나리오에서 특히 유용하며, JWT보다 적합한 선택이 될 수 있습니다.


</details>



<details>
<summary><b>동시성 문제와 극복</b></summary>


# 동시성 문제란? 

## CS에서 동기화의 개념 


동기화(synchronization)는 다중 스레드 또는 프로세스 환경에서 중요한 개념이다. 
여러 스레드가 동일한 자원에 접근할 때, 데이터의 일관성과 무결성을 보장하기 위해 필요한 절차를 말한다. 

프로세스 동기화란 무엇일까? 위키백과에 따르면 '동기화(synchronization)는 시스템을 동시에 작동시키기 위해 여러 사건들을 조화시키는 것' 의미한다.

무슨 말일까? 크게 두 가지로 볼 수 있다. 

- 순서 제어: 여러 프로세스 혹은 스레드가 올바른 순서대로 실행되어 의도한 대로 작업이 수행되도록 한다. 
- 상호 배제: 임계 영역에 대한 동시 접근을 막아, 한 번에 하나의 스레드만 자원을 사용할 수 있도록 한다.

전형적인 레이스 컨디션 상황으로 다음과 같은 예를 들 수 있다. 

두 개의 스레드가 상한 귤의 개수를 카운트하는 프로그램을 생각해 보자. 두 스레드가 동시에 badCounter 변수를 증가시키려고 할 때, 동기화가 없다면 다음과 같은 문제가 발생할 수 있다.

- 스레드 T1이 badCounter를 읽어 0이라는 값을 얻는다.
- 스레드 T2도 같은 시점에 badCounter를 읽어 0이라는 값을 얻는다.
- T1이 badCounter에 1을 더해 1로 업데이트한다.
- T2도 badCounter에 1을 더해 1로 업데이트한다.

이때, 두 스레드가 각각 1씩 증가시켰지만 실제로 badCounter는 1로 남아 있게 된다.

```mermaid
sequenceDiagram
    participant T1 as 스레드 1
    participant T2 as 스레드 2
    participant BC as badCounter (초기값: 0)

    T1->>BC: badCounter 읽기 (0)
    T2->>BC: badCounter 읽기 (0)
    T1->>T1: 로컬 값 증가 (0 + 1)
    T2->>T2: 로컬 값 증가 (0 + 1)
    T1->>BC: 로컬 값 기록 (1)
    T2->>BC: 로컬 값 기록 (1)
    Note right of BC: 최종 badCounter 값은 1, 기대값은 2
```


또 다른 문제를 살펴 보자.

고전적인 문제로 생산자 소비자 문제가 있다.

## 생산자 소비자 문제

생산자 소비자 문제는 여러 스레드가 데이터를 생산하고 소비할 때 발생하는 동기화 문제를 다룬다. 
예를 들어, 생산자는 데이터를 생성해 버퍼에 넣고, 소비자는 버퍼에서 데이터를 꺼내 사용한다.
이때, 버퍼는 생산자와 소비자가 동시에 접근하는 공유 자원이다.

문제 상황을 예시로 들어 보자.

1. 버퍼의 초기 상태는 비어 있다.
2. 생산자 스레드 P1이 데이터를 생성하여 버퍼에 넣으려고 한다.
3. 동시에 소비자 스레드 C1이 버퍼에서 데이터를 꺼내려고 한다.
4. 또 다른 생산자 스레드 P2도 데이터를 버퍼에 넣으려고 한다.
5. 소비자 스레드 C2도 버퍼에서 데이터를 꺼내려고 한다.

```
생산자 {
  while (true) {
    데이터 생성
    버퍼에 데이터 추가
  }
}

소비자 {
  while (true) {
    버퍼에서 데이터 제거
    데이터 소비
  }
}
```

이러한 상황에서 동기화가 제대로 이루어지지 않으면 어떻게 될까? 

구체적인 예시로, 버퍼 크기가 1인 경우를 생각해 보자:

1. 초기 상태: 버퍼 [빈 상태]
2. P1이 1을 추가: 버퍼 [1]
3. C1이 데이터를 꺼냄: 버퍼 [빈 상태]
4. P2가 2를 추가: 버퍼 [2]
5. C2가 데이터를 꺼냄: 버퍼 [빈 상태]

동기화가 없으면 다음과 같은 문제가 발생할 수 있다.

- P1이 1을 추가하기 전에 C1이 버퍼에서 데이터를 꺼내려 하면, C1은 빈 버퍼에서 데이터를 꺼내려 해 오류가 발생한다.
- P1이 1을 추가한 후, C1이 그 데이터를 꺼내기도 전에 P2가 데이터를 추가하려 하면, 버퍼가 가득 차서 P2는 데이터를 추가하지 못하는 상황이 생긴다.


![producer-consumer.png](documents%2Fconcurrency-problem%2Fproducer-consumer.png)
(https://www.webdevway.com/2022/11/producer-consumer-semaphore.html)

만약 버퍼 크기가 무한이고, 초기에 10개의 데이터가 들어있다고 해보자. 이때, 생산자와 소비자가 각각 100,000개의 데이터에 대해 작업을 수행한다면, 작업 후 기대 값은 10일 것이다.

그러나 동기화가 제대로 이루어지지 않으면, 실제로 작업 후 버퍼에 남아있는 데이터 수는 예측할 수 없게 된다. 예를 들어,

- 초기 상태: 버퍼 [10]
- 생산자 P1과 P2가 100,000개의 데이터를 추가
- 소비자 C1과 C2가 100,000개의 데이터를 꺼냄

이때, 발생가능한 문제들은,

- 소비자 스레드 C1과 C2가 동시에 같은 데이터를 꺼내서 실제로는 1개만 꺼냈지만 두 번 꺼낸 것으로 처리됨
- 생산자 스레드 P1과 P2가 동시에 데이터를 추가하려고 하면, 실제로는 1개만 추가되었지만 두 번 추가된 것으로 처리됨

이로 인해, 최종적으로 버퍼에 남아 있는 데이터 수는 10이 아닐 수 있다. 
동기화가 제대로 이루어지지 않아 데이터의 일관성이 깨졌기 때문이다.


## 공유 자원, 레이스 컨디션, 임계 영역

이와 같이 발생한 문제에서 짚고 갈 주요 개념들이 있다.   

**공유 자원**이란, 여러 스레드가 동시에 접근할 수 있는 자원을 말한다.

위의 귤 박스에서 상한 귤의 개수를 세는 예시에서 `badCounter` 변수가 공유 자원이다. 두 스레드가 동시에 접근하여 값을 증가시키려고 할 때 동기화가 없으면 문제가 발생했던 자원이다. 

동시에 `badCounter`를 읽고 값을 증가시킨 후 저장하려 할 때, 두 스레드가 같은 값을 읽고 1씩 더한 후 저장하게 되므로 최종 값은 한 번만 증가했다.
이러한 문제를 **레이스 컨디션(race condition)**이라고 한다. 즉, 여러 스레드가 동시에 공유 자원에 접근할 때 발생하는 예측 불가능한 문제이다.

**임계 영역(critical section)**은 공유 자원에 접근하는 코드 블록을 말한다.
임계 영역 내에서는 한 번에 하나의 스레드만 접근할 수 있어야 한다. 
임계 영역을 설정하지 않으면 레이스 컨디션 문제가 발생한다.


![critical-section.png](documents%2Fconcurrency-problem%2Fcritical-section.png)
(https://nailyourinterview.org/interview-resources/operating-systems/critical-section-problem)

**상호 배제(mutual exclusion)** 란 임계 영역에 대한 동시 접근을 막는 것을 말한다. 상호 배제를 위한 동기화에서는 다음과 같은 세 가지 원칙이 지켜져야 한다. 

- 상호 배제: 한 스레드가 임계 영역에 진입했다면 다른 스레드는 진입할 수 없다.
- 진행 보장(progress): 임계 영역에 들어가고자 하는 스레드가 있으면 반드시 하나의 스레드가 임계 영역에 들어가도록 보장해야 한다. 
- 한정 대기(bounded waiting): 스레드가 임계 영역에 진입하기 위해 무한정 기다리지 않도록 보장해야 한다.

** 위의 문장에서 '스레드'는 프로세스로 표현할 수도 있다. 


## 상호 배제를 위한 기법들

대표적인 상호 배제 기법인 뮤텍스, 세마포어, 모니터를 살펴 보자.

### 뮤텍스(Mutex)

뮤텍스는 상호 배제를 보장하는 가장 간단한 방법 중 하나이다. 
뮤텍스는 오직 한 스레드만 접근할 수 있는 잠금을 제공하며, 다른 스레드는 잠금이 해제될 때까지 대기하는 개념이자 메커니즘이다.

### 세마포어(Semaphore)

세마포어는 뮤텍스와 유사하지만, 정해진 수의 스레드가 동시에 접근할 수 있도록 한다. 두 종류가 있다.
- **이진 세마포어**: 뮤텍스와 유사하게 동작하며, 하나의 스레드만 접근할 수 있다.
- **계수 세마포어**: N개의 스레드가 동시에 접근할 수 있으며, N이 1인 경우는 이진 세마포어와 같다.

### 모니터(Monitor)

모니터는 뮤텍스와 조건 변수(condition variable)를 결합한 기법으로 실제 프로그래밍 언어 수준에서 사용되는 경우가 많다.
조건 변수는 모니터의 구성 요소이다. 스레드가 특정 조건이 만족될 때까지 기다리거나, 조건이 만족되었을 때 대기 중인 스레드를 깨우는 데 사용된다.
`wait()`, 'signal()`, `broadcaset()`과 같은 연산으로 표현된다. 

모니터는 한 번에 하나의 스레드만 접근할 수 있는 임계 영역을 제공하며, 스레드 간 협력이 필요할 때 조건 변수를 통해 대기와 신호를 관리할 수 있다.



### c.f. 자바에서의 모니터 예시

자바에서 모니터는 어떻게 구현될까? 
동시성 제어의 가장 기본으로 사용되는 `synchronized` 키워드를 사용하여 모니터를 구현한다. 이 키워드는 JVM 수준에서 한 번에 하나의 스레드만 지정된 블록에 접근할 수 있도록 한다.

자바의 모든 객체는 하나의 모니터를 가지고 있다. 이를 통해 객체 수준에서 상호 배제를 관리할 수 있다. 

모니터의 중요한 구성 요소는 뮤텍스와 조건 변수이다. 뮤텍스는 상호 배제를 보장하며, 조건 변수는 스레드 간의 협업을 가능하게 한다. 

자바에서는 `wait()`, `notify()`, `notifyAll()` 메서드를 통해 조건 변수를 사용할 수 있다.

위의 생상자와 소비자 문제에 대해 자바의 모니터를 이용해서 동기화하는 예시를 살펴보자. 

```java
public class Buffer {
    private int data;
    private boolean isEmpty = true;

    public synchronized void produce(int value) throws InterruptedException {
        while (!isEmpty) {
            wait();
        }
        data = value;
        isEmpty = false;
        notify();
    }

    public synchronized int consume() throws InterruptedException {
        while (isEmpty) {
            wait();
        }
        isEmpty = true;
        notify();
        return data;
    }
}
```

이 코드에서 `produce` 메서드는 버퍼가 비어있지 않으면 `wait`을 호출하여 대기 상태로 전환된다. `consume` 메서드는 버퍼가 비어 있으면 `wait`을 호출하여 대기 상태로 전환된다. `notify` 메서드는 대기 중인 스레드를 깨워서 작업을 재개하도록 한다.

여기서 임계영역은 어떻게 보장되는 걸까?  

모니터는 `synchronized` 키워드로 상호 배제를 보장하여 하나의 스레드만 임계 영역에 진입할 수 있게 한다.
이후 `wait()`, `notify()`, `notifyAll()` 메서드를 통해 스레드 간 협력을 관리하여 조건이 만족되면 대기 중인 스레드를 깨운다. 
이러한 방식으로 상호배제를 구현하고 동기화를 달성한다.


# 웹 서비스 개발에서의 동시성 이슈 

웹 서비스 개발에서의 동시성 이슈는 다중 사용자와 다중 요청을 처리하는 과정에서 발생하는 문제를 말한다.
특성상 운영체제나 어플리케이션 수준의 동시성 문제보다는 주로 데이터베이스 트랜잭션 처리, 세션 처리 등에서 발생한다.

## 1. 데이터베이스 접근 동시성 문제 

웹 애플리케이션은 다수의 사용자가 동시에 데이터베이스에 접근하여 데이터를 조회하거나 수정하는 상황이 빈번하다. 이때, 여러 사용자가 동시에 동일한 데이터를 수정하려고 하면 데이터 일관성과 무결성이 깨질 수 있다. 운영체제 수준의 동기화 문제는 주로 메모리나 CPU 자원에 관한 것이지만, 웹 서비스에서는 데이터베이스의 트랜잭션 처리와 관련된 문제가 주를 이룬다.

### 1) Lost Update 문제

Lost Update 문제는 두 개 이상의 트랜잭션이 동시에 같은 데이터를 갱신하려고 할 때 발생한다.
한 트랜잭션의 업데이트가 다른 트랜잭션에 의해 덮어써져 최종적으로 하나의 업데이트만 반영되는 상황이다.
앞서 살펴본 `badCounter` 문제를 DB 자원으로 변경한 것과 결국 동일한 문제이다. 

예를 들어, 다음과 같이 은행 계좌 잔액을 갱신하는 상황을 생각해보자.

- 트랜잭션 T1이 계좌 잔액을 읽어 1000원이라는 값을 얻는다.
- 동시에 트랜잭션 T2도 같은 계좌 잔액을 읽어 1000원이라는 값을 얻는다.
- T1이 계좌에 200원을 더해 1200원으로 업데이트한다.
- T2가 계좌에 300원을 더해 1300원으로 업데이트한다.

이 경우, T1의 업데이트가 T2의 업데이트에 의해 덮어써져 최종 잔액이 1300원이 되며, T1의 업데이트는 사라지게 된다. 이로 인해 데이터의 일관성이 깨지게 된다.

### 2) Uncommitted Dependency 문제

Uncommitted Dependency 문제는 다른 말고 또는 Dirty Read 문제라고도 불린다.
하나의 트랜잭션이 커밋되지 않은 데이터를 다른 트랜잭션이 읽을 때 발생한다. 이때, 잘못된 데이터를 기반으로 작업을 수행하게 되어 데이터의 무결성이 깨질 수 있다. 

예를 들어, 트랜잭션 T1이 데이터를 수정하고 아직 커밋하지 않은 상태에서 트랜잭션 T2가 그 데이터를 읽는 상황을 생각해보자.

- 트랜잭션 T1이 데이터 값을 0에서 1로 수정한다.
- 트랜잭션 T2가 데이터를 읽어 값 1을 얻는다.
- 트랜잭션 T1이 롤백하여 데이터 값을 다시 0으로 되돌린다.
- 트랜잭션 T2는 값 1을 계속 사용한다.

이 경우, 트랜잭션 T2는 실제로 존재하지 않는 값을 사용하게 되어 데이터 무결성이 깨지게 된다.

![uncomitted-dependency-problem.png](documents%2Fconcurrency-problem%2Funcomitted-dependency-problem.png)
(데이터 중심 애플리케이션 설계, 229p)

### 3) Inconsistent Analysis 문제

Inconsistent Analysis 문제는 또는 Non-repeatable Read 문제라고도 한다.
한 트랜잭션이 같은 데이터를 여러 번 읽을 때, 다른 트랜잭션이 그 데이터를 수정하여 매번 다른 값을 읽게 되는 상황이다. 
이는 데이터의 일관성을 보장할 수 없게 만든다. 

예를 들어, 트랜잭션 T1이 데이터를 여러 번 읽는 동안 트랜잭션 T2가 그 데이터를 수정하는 상황을 생각해보자.

- 트랜잭션 T1이 데이터 값을 읽어 100을 얻는다.
- 트랜잭션 T2가 데이터를 200으로 수정하고 커밋한다.
- 트랜잭션 T1이 다시 데이터를 읽어 200을 얻는다.

![inconsistent-analysis-problem.png](documents%2Fconcurrency-problem%2Finconsistent-analysis-problem.png)
(데이터 중심 애플리케이션 설계, 237p)

이 경우, 트랜잭션 T1은 동일한 데이터에 대해 일관되지 않은 값을 얻게 되며, 데이터의 일관성이 깨지게 된다.



## 2. 세션 관리 동시성 문제

또 한가지 이슈가 발생할 수 있는 영역은 세션 관리 부분이다.

웹 애플리케이션은 사용자의 상태 정보를 세션에 저장하여 사용자별로 개별적인 상태를 관리한다. 다중 사용자가 동시에 세션 정보를 갱신하거나 접근할 때, 동시성 문제가 발생할 수 있다.

다만, 최근의 웹 통신 방식에서는 세션 기반 인증보다는 토큰 기반 인증을 많이 사용하는 추세이다. 
토큰 기반 인증은 세션 관리의 동시성 문제를 해결하는 데 유리하며, 특히 분산 시스템 환경에서 더 효율적일 수 있다. 


### 세션 공유 문제

다중 서버 환경에서 사용자의 세션 정보가 서버 간에 일관성 있게 공유되지 않아 한 서버에서 변경된 세션 정보가 다른 서버에서 반영되지 않을 수 있는 문제이다.

예를 들어, 상태 경쟁으로 레이스 컨디션이 발생하는 경우를 생각해보자.

- 사용자가 서버 A에 로그인하여 세션이 생성된다.
- 같은 사용자가 다른 요청을 통해 서버 B에 연결되어 세션 정보를 갱신한다.
- 서버 B에서 갱신된 세션 정보가 서버 A에 반영되지 않아, 서버 A에서는 여전히 이전 세션 정보를 사용하게 된다.

이로 인해 사용자는 일관성 없는 상태를 경험하게 된다. 
예를 들어, 장바구니에 상품을 추가하는 작업이 여러 서버에 분산되어 처리될 때, 각 서버가 서로 다른 세션 정보를 가지고 있어 최종적으로 장바구니 상태가 불일치하게 될 수 있다.


### 토큰 기반 인증의 장점

최근의 웹 통신 방식에서 많이 사용되는 토큰 기반 인증은 이러한 세션 관리의 동시성 문제를 해결하는 데 효과적이다. 
특히, JWT(Json Web Token)를 사용한 토큰 기반 인증은 다음과 같은 장점을 제공한다.

1. **서버 확장성**: 토큰은 클라이언트가 매 요청마다 서버에 제공하므로, 서버 간 세션 정보를 공유할 필요가 없다. 
2. **무상태(stateless) 인증**: 토큰 기반 인증은 서버가 상태 정보를 유지하지 않으므로, 서버는 클라이언트의 토큰을 검증하기만 하면 된다. 
3. **보안 강화**: 토큰에는 서명된 정보가 포함되어 있어 변조를 방지할 수 있으며, 토큰의 만료 시간을 설정하여 보안성을 강화할 수 있다.

**예시**

1. 사용자가 로그인하여 JWT를 발급받는다.
2. 클라이언트는 JWT를 저장하고, 이후 모든 요청에 이 토큰을 포함시킨다.
3. 서버는 토큰을 검증하여 사용자를 인증하고 요청을 처리한다.

토큰 기반 인증은 세션 관리의 복잡성을 줄이고, 동시성 문제를 효과적으로 해결하는 대안이 될 수 있다.


# 왜 DB의 트랜잭션 격리 레벨로 동시성이 해결되지 않는가?   

## 트랜잭션 격리 레벨의 역할과 한계

데이터베이스 차원의 일관성을 보장하기 위해서는 데이터베이스 자체에서 제공되는 기능으로 다양한 격리 수준이 존재한다.

1. **Read Uncommitted**: 커밋되지 않은 데이터를 읽을 수 있어, Dirty Read 문제가 발생할 수 있다.
2. **Read Committed**: 커밋된 데이터만 읽을 수 있어, Dirty Read 문제를 방지한다.
3. **Repeatable Read**: 트랜잭션 동안 동일한 데이터를 여러 번 읽어도 같은 값을 보장하며, Non-repeatable Read 문제를 방지한다.
4. **Serializable**: 가장 높은 격리 수준으로, 트랜잭션을 직렬화하여 실행된 것처럼 보장하여 모든 동시성 문제를 방지한다.

일반적으로 최고 단계인 `Serializable`을 사용하면 완전한 스케줄 처리를 보장하여, 모든 동시성 문제를 방지할 수 있다. 
그러나 성능 저하가 발생할 수 있어 실제로는 `Read Committed`나 `Repeatable Read` 수준을 사용하는 경우가 많다.

`Mysql`의 `InnoDB` 엔진은 특히 Mvcc(Multi-Version Concurrency Control)를 통해 격리 수준을 보장하는데,
`Repeatable Read` 수준에서 MVCC를 사용하여 각 트랜잭션은 자신만의 스냅숏을 참조하게 된다. 
각 트랜잭션은 시작 시점에 데이터 상태를 캡처하고, 데이터가 변경될 때 이전 상태를 undo 영역에 저장하고, 이를 활용해 일관성을 보장한다.
다만, MySQL에서는 Serializable 격리 수준을 사용하지 않는 한, Phantom Read 문제를 완전히 방지하지는 못한다.



## 동시성 이슈가 완벽히 해결되지 않는 이유


### 1. Write Skew (쓰기 왜곡)
Repeatable Read 격리 수준은 읽기 작업에서 일관성을 보장하지만, 쓰기 작업에서는 문제가 발생할 수 있다. 
Write Skew는 두 트랜잭션이 서로 간섭하지 않는 방식으로 데이터를 읽고 수정하지만, 그 결과 데이터 일관성이 깨지는 상황이다.

```
앨리스와 밥이 어느날 함께 호출 대기를 하고 있다고 상상해보자. 둘 다 몸이 안 좋아서 호출 대기를 그만두기로 결심했다. 
불행하게도 그들은 거의 동시에 호출 대기 상태를 끄는 버튼을 클릭했다. 다음에 어떤 일이 생길지 그림 7-8에 설명돼 있다. 

각 트랜잭션에서 애플리케이션은 먼저 현재 두명 이상의 의사가 대기 중인지 확인한다. 
만약 그렇다면 의사 한 명이 호출 대기에서 빠져도 안전하다고 가정한다. 
데이터베이스에서 스냅숏 격리를 사용하므로 둘 다 2를 반환해서 두 트랜잭션 모두 다음 단계로 진행한다. 
앨리스는 대기 상태를 끄도록 자신의 레코드를 갱신하고 밥도 같은 식으로 자신의 레코드를 갱신한다. 
두 트랜잭션 모두 커밋되고 호출 대기하는 의사가 한 명도 없게 된다. 
최소 한 명의 의사가 호출 대기해야 한다는 요구 사항을 위반했다. 
```

![write-skew.png](documents%2Fconcurrency-problem%2Fwrite-skew.png)
(데이터 중심 애플리케이션 설계 247p) 

위의 예시에서 앨리스와 밥은 동시에 호출 대기를 취소하려고 시도한다.이때 충돌이 발생했다는 것이 경쟁 조건이다. 두 트랜잭션이 한 번에 하나씩 실행됐다면 이상 현상이 나타나지 않았을 것이다.


### 2. Phantom Read

예를 들어, 한 트랜잭션이 고객 테이블에서 특정 조건을 만족하는 고객 수를 조회하고, 다른 트랜잭션이 동시에 새로운 고객을 삽입하는 상황을 고려해 보자.

- 트랜잭션 T1이 고객 테이블에서 나이 30 이상의 고객 수를 조회한다.
- 트랜잭션 T2가 새로운 고객(나이 35)을 삽입하고 커밋한다.
- T1이 다시 고객 테이블에서 나이 30 이상의 고객 수를 조회한다.

이 경우, T1은 두 번째 조회 시점에서 이전과 다른 결과를 얻게 된다. 
이는 팬텀 읽기 문제의 예로, Repeatable Read 격리 수준에서 발생하는 이상 현상이다.


### 3. 공유 자원 접근의 본질적인 문제
데이터베이스 격리 수준만으로는 어플리케이션 레벨의 모든 동시성 문제를 해결할 수 없는 본질적인 이유는 결국 데이터베이스는 여러 트랜잭션이 동시에 접근하는 공유 자원이라는 점이다.

예를 들어, 두 명의 사용자가 동시에 같은 상품을 장바구니에 담는 상황을 고려해 봅시다. 각 사용자는 장바구니에 담긴 상품 수를 증가시키려고 합니다.

- 사용자 A가 장바구니에 상품을 추가하려고 합니다.
- 사용자 B도 동시에 장바구니에 상품을 추가하려고 합니다.
- 두 사용자가 각각의 트랜잭션에서 장바구니의 상품 수를 증가시키려고 하지만, 이 과정에서 동시성 문제가 발생할 수 있습니다.

Repeatable Read 격리 수준은 트랜잭션이 시작된 시점의 스냅샷을 사용하여 읽기 작업에서 일관성을 보장한다.
그러나 여러 트랜잭션이 동일한 데이터를 동시에 쓰거나 갱신하려고 할 때, 위에서와 같은 동시성 문제가 여전히 발생할 수 있다.




# 다양한 해결 방안 


## 1. Database Lock


### 기본 개념


1. S-Lock
- a.k.a. "읽기 전용"
- `select .. from .. where .. for share`
- 동작 방식: 여러 트랜잭션이 S Lock 획득 → 다수의 트랜잭션이 동시에 읽되 수정 불가


2. X-Lock
- a.k.a "쓰기 잠금"
- `select .. from .. where .. for update`
- 한 번에 하나의 트랜잭션만 X Lock 획득 → 데이터를 읽거나 수정하는 동안 다른 트랜잭션은 접근 불가


### 실전 활용


1. Pessimistic Lock(비관적 락)
- 데이터 충돌이 많이 일어날 것으로 상황을 "비관할 때" 사용된다고 이해하면 쉽다
- 데이터베이스의 동시성 문제를 해결하기 위해 S Lock과 X Lock을 사용하는 전략
- 데이터 충돌을 미리 방지하는 방식
- 트랜잭션이 데이터에 접근할 때 즉시 락을 획득, 다른 트랜잭션이 해당 데이터에 접근하지 못하도록 함
- 예시: `select for update`를 이용하여 해당 행에 X Lock을 걸어 다른 트랜잭션이 접근하지 못하게 한다 (Mysql, PostgreSQL 기준)
- 🚨 주의 사항: 불필요한 Lock에 따른 성능 저하 주의, 여러 테이블에 걸친 Lock 작업은 데드락 유발 가능성 내포



2. Optimistic Lock(낙관적 락)
- 데이터 충돌이 드믈 것으로 "낙관할 때" 사용된다고 이해하면 쉽다
- 데이터 충돌을 사후에 감지하여 처리하는 방식
- 트랜잭션이 데이터에 접근할 때 락을 사용하지 않고, 커밋 시점에 충돌 여부를 확인한다
- 예시: 레코드에 버전 번호를 두고 업데이트 시 현재 버전과 비교하여 충돌을 감지하고 처리한다 (CAS 메커니즘)
- 🚨 주의 사항: 잦은 경합으로 retry가 많아지면 DB Connection, 스레드 점유 등 잠재적 문제 요소 내포

## 2. Distributed Lock

> 분산 환경에서의 동기화 문제를 해결하기 위한 Lock 제어 기법으로 Redis와 같은 공용 저장소를 이용한 동시 제어 접근 메커니즘


### Redis를 이용한 분산락

- Redis의 싱글 스레드, key-value 저장소의 특성을 활용하여 락 구현
- Redis의 `SETNX`(Set if Not eXists) 명령어를 사용하여 락을 획득하고, `EXPIRE` 명령어를 사용하여 락의 유효 시간을 설정



1. **Simple Lock**
- **개념**: 기본적인 락 메커니즘으로, Redis의 `SETNX` 명령어를 사용하여 락을 획득하고, `EXPIRE` 명령어를 통해 락의 유효 시간을 설정한다.
- **원리**: 클라이언트가 `SETNX` 명령어를 사용하여 특정 키에 값을 설정하면, 해당 키가 이미 존재하지 않는 경우에만 설정이 성공 → 실패시 별도 처리 없음.
2. **Spin Lock**
- **개념**: Spin Lock은 락을 획득할 때까지 반복적으로 시도하는 방식으로, 클라이언트는 일정 시간 동안 `Simple Lock`을 빈복 시도한다.
- **원리**: 클라이언트가 `SETNX` 명령어를 사용하여 락을 획득하지 못하면, 다시 시도한다. 락을 획득할 때까지 반복된다. Spin Lock은 비교적 간단한 구현이지만, 락을 획득하기 전까지 CPU 자원을 소비할 수 있다는 단점이 있다. 이를 보완하기 위해 지수적 백오프(exponential backoff)와 같은 기법을 사용할 수 있다.

3. **Pub/Sub**
- **개념**: Pub/Sub는 Redis의 메시징 기능을 이용하여 분산 락을 구현하는 방법이다. 클라이언트는 락을 획득하려고 시도한 후, 락이 이미 획득된 경우에는 특정 채널을 구독하여 락이 해제될 때까지 기다린다.
- **원리**: 클라이언트가 `SETNX` 명령어를 사용하여 락을 획득하지 못하면, 특정 채널을 구독한다. 락을 해제할 때는 해당 채널에 메시지를 발행하여 구독 중인 클라이언트에게 락이 해제되었음을 알린다. 이를 통해 클라이언트는 락이 해제된 시점을 인지하고 다시 락을 획득하려고 시도할 수 있다.



### Lettuce Vs. Redisson

Lettuce와 Redisson은 모두 Redis 클라이언트를 위한 Java 라이브러리이다. 두 라이브러리는 제공하는 기능과 구현 방식에 있어서 차이가 있다.

**Lettuce**
- **특징**: Lettuce는 비동기적, 동기적, 리액티브 프로그래밍을 지원하는 Redis 클라이언트. 다양한 Redis 명령어를 직접적으로 사용할 수 있으며, 낮은 수준의 추상화를 제공.
- **스핀락 구현**: Lettuce는 직접적인 분산 락 기능을 제공하지 않으며, 기본적으로 `SETNX`와 `EXPIRE` 명령어를 사용하여 애플리케이션 수준에서 Spin Lock을 구현해야 한다.
- **장점**: 높은 성능, 비동기적 지원, 리액티브 프로그래밍 지원.
- **단점**: 분산 락 기능을 직접 구현해야 하는 부담.

```java
while (!redisClient.setnx("lockKey", "lockValue")) {
    // 일정 시간 대기 후 재시도
    Thread.sleep(100);
}
// 락을 획득한 후 작업 수행
try {
    // 임계 영역
} finally {
    redisClient.del("lockKey");
}
```

**Redisson**
- **특징**: Redisson은 Java 개발자를 위한 고수준의 추상화를 제공하며, 다양한 분산 객체, 컬렉션, 락, 동기화 기법을 지원한다.
- **분산 락 구현**: Redisson은 `RLock` 클래스를 통해 분산 락을 쉽게 구현할 수 있으며, Pub/Sub 구조를 사용하여 락 획득 대기를 효율적으로 처리한다.
- **장점**: 고수준의 추상화, 다양한 분산 객체 및 컬렉션 지원, 쉬운 분산 락 구현.
- **단점**: 비교적 높은 메모리 사용량.


```java
RLock lock = redissonClient.getLock("lockKey");
lock.lock();
try {
    // 임계 영역
} finally {
    lock.unlock();
}
```


### Redis 분산락에서 중요한 순서

락과 트랜잭션은 데이터의 무결성을 보장하기 위해 아래 순서에 맞게 수행됨을 보장해야 한다.

> 락 획득 → 트랜잭션 시작 → 비즈니스 로직 수행 → 트랜잭션 종료 → 락 해제

이와 같은 순서가 지켜지지 않으면 어떻게 될까?

예를 들어, 트랜잭션 A가 좌석 예약 시스템에서 좌석을 조회한 후 락을 획득하기 전에 트랜잭션 B가 동일한 좌석을 예약하는 상황을 가정해 보자. 이 경우 트랜잭션 A는 이미 예약된 좌석을 다시 예약하는 일관성 문제가 발생할 수 있다.

### 예시
- **트랜잭션 A**: 좌석 조회 → 락 획득 → 예약
- **트랜잭션 B**: 락 획득 → 좌석 조회 → 예약

### Mermaid 다이어그램

```mermaid
sequenceDiagram
    participant A as 트랜잭션 A
    participant B as 트랜잭션 B
    participant DB as 데이터베이스

    A->>DB: 좌석 조회
    B->>DB: 락 획득
    B->>DB: 좌석 조회
    B->>DB: 좌석 예약
    A->>DB: 락 획득
    A->>DB: 좌석 예약 시도
    DB->>A: 예약 실패 (이미 예약됨)
```

위의 예시와 같이, 트랜잭션 시작 후 락을 획득하면 다른 트랜잭션이 동일한 데이터를 변경할 수 있어 사실상 락의 기능을 보장받지 못한다.


## 3. Scheduling With Message Queue

메시지 큐를 사용하여 동시성을 제어할 수도 있다. 특히 분산 시스템에서 유용할 수 있다.


**Kafka Messaging**

Kafka는 높은 처리량과 낮은 지연 시간으로 대규모 데이터 스트림을 처리하는 메시징 시스템이다. Kafka의 주요 기능 중 하나는 동일한 키로 메시지를 발행하면 항상 동일한 파티션에 메시지가 저장되어 순서가 보장된다는 점이다. 이를 통해 특정 데이터에 대한 동시성 문제를 해결할 수 있다.

순서를 보장받을 수 있다는 점이 카프카를 사용하면서 얻을 수 있는 정말 큰 장점이다.

메시지 큐를 이용하여 좌석 예약 시스템의 동시성 문제를 해결하는 예시를 생각해 보자. 좌석 예약 요청을 Kafka 메시지 큐에 발행하고, 각 요청은 순차적으로 처리된다.


```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant APIServer as API 서버
    participant Kafka as Kafka 메시지 큐
    participant OtherAPIServer as 타 API 서버
    participant DB as 데이터베이스

    Client->>APIServer: 좌석 예약 요청
    APIServer-->>Kafka: 예약 요청 메시지 발행
    Kafka-->>OtherAPIServer: 메시지 전달 (순서 보장)
    OtherAPIServer->>DB: 좌석 예약 처리
    DB->>OtherAPIServer: 예약 성공/실패 응답
    OtherAPIServer->>Client: 처리 결과 응답
```

1. **클라이언트**가 좌석 예약 요청을 보낸다.
2. **API 서버**는 요청을 받아 Kafka 메시지 큐에 예약 요청 메시지를 발행한다.
3. Kafka는 동일한 키로 메시지를 발행하면 항상 동일한 파티션에 메시지를 저장하여 메시지의 순서를 보장한다.
4. **타 API 서버**는 Kafka로부터 순차적으로 메시지를 받아 좌석 예약을 처리한다.
5. **타 API 서버**는 **데이터베이스**에 좌석 예약을 요청하고, 예약 성공 또는 실패 응답을 받는다.
6. **타 API 서버**는 예약 처리 결과를 클라이언트에게 응답한다.

이와 같은 방식으로 Kafka를 사용하여 메시지의 순서를 보장하고, 동시성 문제를 해결할 수 있다. 이를 통해 좌석 예약과 같은 중요한 비즈니스 로직을 안정적으로 처리할 수 있다.


### 질문 1. 카프카에서 파티션 저장하는 메커니즘으로 순서를 보장한다고 하더라도, 발행과 소비에서 비동기 구간에서, 순서가 보장되는 게 맞는가?

Kafka에서 동일한 키를 가진 메시지는 항상 동일한 파티션에 저장되며, 이로 인해 메시지의 순서는 파티션 내에서 보장된다. 발행 시, 메시지는 순서대로 파티션에 기록되며, 소비자는 해당 파티션의 메시지를 순차적으로 읽어 처리한다.

비동기 구간에서 발행과 소비가 이루어지더라도 Kafka의 구조상 순서가 보장된다. 발행자는 메시지를 파티션에 순서대로 기록하고, 소비자는 파티션의 오프셋을 순차적으로 증가시키며 메시지를 소비하기 때문이다. 따라서 비동기 환경에서도 Kafka의 파티션 메커니즘을 통해 메시지의 순서는 보장된다.

소비시에 전송 자체가 비동기로 진행된다고 하더라도, 오프셋으로 순차적으로 읽을 수 있는 것이 핵심이다.

##### 예시
- **발행 시**: API 서버는 메시지를 발행할 때 순서대로 Kafka에 기록한다.
- **소비 시**: 타 API 서버는 파티션의 메시지를 순서대로 읽어 처리한다.

```mermaid
sequenceDiagram
    participant Producer as API 서버
    participant Kafka as Kafka 메시지 큐
    participant Consumer as 타 API 서버

    Producer->>Kafka: 메시지 1 발행 (키 A)
    Producer->>Kafka: 메시지 2 발행 (키 A)
    Producer->>Kafka: 메시지 3 발행 (키 A)
    Kafka-->>Consumer: 메시지 1 전달 (키 A)
    Kafka-->>Consumer: 메시지 2 전달 (키 A)
    Kafka-->>Consumer: 메시지 3 전달 (키 A)
```

### 질문 2. 메시지 유실 문제에 대한 대책은 무엇인가?

한 가지 해결 방식은 Outbox 패턴을 이용해서 메시지를 저장하고 Retry 하는 메커니즘이다.

이벤트가 Kafka로 전송되지 않으면 Outbox 패턴을 통해 이벤트를 데이터베이스에 저장하고, 이후 Kafka 소스 커넥터를 사용하여 다시 전송한다. 이 방법은 네트워크 문제로 인한 이벤트 유실을 방지하지만, 중복 이벤트 문제가 발생할 수 있다. 이를 해결하기 위해 이벤트 처리 로직을 수정하여 첫 번째 이벤트만 처리하고 나머지 이벤트는 무시하는 방식으로 개선할 수 있다.


```mermaid
sequenceDiagram
    participant Producer as 프로듀서
    participant Kafka as Kafka
    participant Couchbase as Couchbase
    participant Connector as Kafka 커넥터
    participant Consumer as 컨슈머

    Producer->>Kafka: 이벤트 전송
    alt 타임아웃 발생
        Producer->>Couchbase: 이벤트 저장 (Outbox 패턴)
        Couchbase->>Connector: 이벤트 전송
        Connector->>Kafka: 이벤트 재전송
    end
    Kafka->>Consumer: 이벤트 전달
```


(https://miro.medium.com/v2/resize:fit:1100/format:webp/1*tU8_v_V4ID9ptis5mg9Gfw.png)






# 본 프로젝트의 잠재적 이슈 및 해결 방안


## 1. 중복 클릭 문제 (Duplicate Submission)
- **대상 API**: 모든 사용자 인터렉션이 발생하는 API
- **예상 이슈**: 사용자가 동일한 요청을 여러 번 보내 중복 처리가 발생하는 문제
- **대상 공유 자원**: 데이터베이스의 특정 자원 상태 값 (예: 재고, 예약 상태)
- **원인 및 설명**:
중복 클릭 문제는 사용자가 버튼을 빠르게 여러 번 클릭할 때 발생할 수 있다. 
  예를 들어, 재고 업데이트 API에서 중복 요청이 발생하면 사용자가 동일한 재고를 여러 번 갱신하여 중복 처리가 발생할 수 있다. 
  이를 방지하기 위해 클라이언트 측에서 버튼 클릭 후 비활성화 처리, 서버 측에서는 중복 요청을 감지하고 처리하는 로직을 구현할 수 있다.


## 2. 대기열 진입 시나리오
- **대상 API**: 토큰 생성
- **예상 이슈**: 여러 사용자가 동시에 대기열에 진입할 때 순서가 보장되지 않거나 동일한 순서 번호를 부여받는 문제
- **대상 공유 자원**: 데이터베이스의 대기열 순서 정보
- **원인 및 설명**:
여러 사용자가 동시에 토큰을 요청할 때 동일한 토큰 정보가 덮어쓰일 수 있다. 
예를 들어, 두 사용자가 동시에 대기열에 진입하려 할 때 동일한 순서 번호를 부여받으면 대기열의 순서가 일관적이지 않을 수 있다.

  
### 취약 코드 분석

1. **토큰 생성 및 대기열 추가 로직**

```java
@Override
@Transactional
public WaitingQueueTokenGenerateInfo generateAndEnqueue(WaitingQueueTokenGenerateCommand command) {
    WaitingQueueTokenCounter counter = counterManager.getIncreaseCounter(getMaxWaitingTokens());
    WaitingQueueToken newToken = WaitingQueueToken.createToken(command).init(counter);
    return WaitingQueueTokenGenerateInfo.from(enqueueRepository.enqueue(newToken));
}
```

`generateAndEnqueue` 메서드는 사용자의 대기열 진입 요청을 받아 토큰을 생성하고 대기열에 추가하는 로직이다.

1. **카운터 증가 및 토큰 초기화 로직**
`getWithLockAndIncreaseCounter` 메서드를 호출하여 대기열의 순서 번호를 증가시키고, 새로운 토큰을 초기화한다. 
여러 스레드가 동시에 이 메서드를 호출하는 경우, 비관적 락을 사용하지 않으면 동일한 순서 번호가 부여될 수 되어 동시성 이슈 발생이 가능하다. 

2. **토큰 생성 및 대기열 추가 로직**
대기열에 추가하는 과정에서도 동시성 문제가 발생할 수 있다.
`WaitingQueueToken.createToken` 메서드를 호출하여 새로운 토큰을 생성한 후, `enqueueRepository.enqueue` 메서드를 호출하여 대기열에 추가한다. 

   

### 해결 방안 탐구

1) 락을 사용하지 않고 해결할 수 있는가? (유니크 인덱스, 멱등성) [✔]
- **판단**: 대기열 생성시 가능하다.
- **근거**: 토큰 생성 시 `userId`와 `status`에 대한 유니크 제약 조건을 설정하여 중복된 토큰 생성을 방지할 수 있다. 

2) 락 프리(lock-free, 낙관적 락)로 해결 가능한가? [✔]
- **판단**: 가능하다.
- **근거**: `WaitingQueueTokenCounter`에 낙관적 락을 적용하여 충돌 발생 시 재시도할 수 있다.

3) 비관적 락으로 해결 가능한가? [✔]
- **판단**: 가능하다.
- **근거**: 비관적 락을 사용하여 `WaitingQueueTokenCounter`에 대한 동시성 제어가 가능하다. 

4) 분산 락이나 메시지 큐로 동시성 제어가 가능하며 더 이점이 있나? [✔]
- **판단**: 가능하다. 
- **근거**: 분산 락으로 다수 인스턴스에서의 자원 접근을 제어하고, 메시지 큐는 요청을 순차적으로 처리할 수 있다. 


### 선택

1. **WaitingQueueTokenCounter**
카운터 값을 증가시키는 `getWithLockAndIncreaseCounter`에 비관적 락을 적용한다.

2. **WaitingQueueToken**
토큰 생성 시 `userId`와 `status`에 유니크 제약 조건을 설정한다.
   



## 3. 예약 시나리오
- **대상 API**: 임시 예약 생성
- **예상 이슈**: 여러 사용자가 동시에 좌석을 예약하려는 경우, 일관성이 깨질 수 있는 문제
- **대상 공유 자원**: 특정 좌석 및 예약 정보
- **원인 및 설명**: 여러 사용자가 동시에 동일한 좌석을 예약하려고 할 때, 예를 들어, 사용자 A와 B가 동시에 좌석 10번을 예약하려고 시도하면, 한 사용자가 예약을 완료하기 전에 다른 사용자가 동일한 좌석을 예약할 수 있다.


### 취약 코드 분석


```java
@Transactional
public TemporalReservationCreateInfo createTemporalReservation(ReservationCreateCommand command) {
  ConcertOption concertOption = concertOptionRepository.findById(command.getConcertOptionId()); 
  Seat seat = seatRepository.findSingleByCondition(onConcertOptionSeat(concertOption, command.getSeatNumber()));
  seatRepository.save(seat.doReserve());
  return TemporalReservationCreateInfo.from(temporalReservationRepository.save(create(command, concertOption, seat)));
}
```

1. **ConcertOption 조회 로직**

`findById` 메서드를 호출하여 `concertOptionId`에 해당하는 `ConcertOption` 엔티티를 조회한다. 
이 과정에서 다른 사용자가 동일한 `concertOption`에 접근해도 조회할 때는 크게 문제가 되지 않는다.
그러나 수정을 하는 경우에는 문제가 될 수 있다. 

2. **좌석 예약 로직**

`findSingleByCondition` 메서드를 호출하여 특정 `ConcertOption`과 좌석 번호에 해당하는 `Seat` 엔티티를 조회한 후, `doReserve` 메서드를 호출하여 좌석을 예약한다. 
이 과정에서 다른 사용자가 동시에 같은 좌석을 예약하려고 시도할 경우, `Seat` 엔티티의 상태가 불일치할 수 있다.


3. **임시 예약 생성 및 저장 로직**

`TemporalReservation` 객체를 생성한 후 저장한다.
`Seat` 엔티티 처리에서 동시성 처리가 이루어지지 않으면, 여러 사용자가 동시에 동일한 좌석을 예약할 수 있지만, 제어가 될 것으로 가정한다면 괜찮을 것으로 예상된다.  



### 해결 방안 탐구


1) **락을 사용하지 않고 해결할 수 있는가?** (유니크 인덱스, 멱등성) [❌]
- **판단**: 딱히 가능한 부분이 없어 보인다.
- **근거**: 다만 좀 더 안정적으로 하려면 `temporalReservation` 에 걸 수 있다.


2) **락 프리(lock-free, 낙관적 락)로 해결 가능한가?** [✔]
- **판단**: 가능하다.
- **근거**: 예약의 특성상, 하나만 성공하고 나머지는 실패해도 괜찮다. 따라서 retry 없이 `Seat`에 대해 낙관적 락을 적용할 수 있다. 


3) **비관적 락으로 해결 가능한가?** [✔]
- **판단**: 가능하다.
- **근거**: `concertOption`에 대한 비관적 락을 적용하여, 사용자가 예약을 시도하는 시점에 `concertOption`이 변경되지 않도록 제어할 수 있다. `concertOption`은 조회한 값에 대해 다른 쪽에서 변경이 발생하지 않도록 하는 것이 목적이므로 비관적 락이 적합하다.


4) **분산 락이나 메시지 큐로 동시성 제어가 가능하며 더 이점이 있나?** [✔]
- **판단**: 가능하다.
- **근거**: 현재 케이스에서 여러 락과 엔티티가 겹쳐져 있는 상황이라면, 오케스트레이션에서 분산 락으로 한 번에 제어해주는 것도 좋을 것 같다. 


### 선택

1. **Seat**: 
~~좌석 예약 시 `Seat` 엔티티에 낙관적 락을 적용한다. 좌석 예약 시도 시 충돌이 발생하면 재시도 없이 실패 처리된다.~~

2. **ConcertOption**
~~`concertOption` 엔티티에 비관적 락을 적용한다. 다른 사용자가 동일한 `concertOption`을 동시에 수정하지 못하도록 한다.~~


`Seat`에 낙관적 락 적용시, 하나의 예약만 성공하고 나머지는 실패시킨다는 테스트는 통과한다. 즉 결과적으로 원하는 결과를 얻을 수는 있다.  
그러나, 로그를 분석한 결과 데드락이 발생하는 상황이 관찰됐다. 그 이유는 두 요청이 동시에 동일한 `Seat`를 조회하고 업데이트하려 할 때 S-Lock을 X-Lock으로 전환하려 하면서 서로의 락을 기다리며 데드락이 발생하기 때문이다.

자세한 내용은 [낙관적 락에서 발생한 데드락 트러블 슈팅](documents/trouble-shooting/why-deadlock-found-with-optimistic-locking.md)에서 볼 수 있다. 

데드락 상황을 방치할 수는 없다. 해결 방법으로는 낙관적 락을 사용하지 않고 비관적 락을 사용하는 것이다. 
`Seat`에 비관적 락을 걸어 해결 할 수 있다. 
단, 이때 `concertOption`도 잠그고 있다면, 해당 엔티티에는 X락을 적용하지 말고 S락으로 조회해야 한다. `ConcertOption`에도 X-Lock을 걸면 같은 이유로 다시 데드락 문제가 발생한다.

결론적으로 다음과 같이 된다. 

```java
public TemporalReservationCreateInfo createTemporalReservation(ReservationCreateCommand command) {
    ConcertOption concertOption = concertOptionRepository.findByIdWithSLock(command.getConcertOptionId()); // read / write 분리를 고려한 S lock
    Seat seat = seatRepository.findSingleByConditionWithLock(onConcertOptionSeat(concertOption, command.getSeatNumber()));
    seatRepository.save(seat.doReserve());
    return TemporalReservationCreateInfo.from(temporalReservationRepository.save(create(command, concertOption, seat)));
}
```


## 4. 잔액 충전/사용 및 결제 시나리오
- **대상 API**: 잔액 충전/사용, 결제
- **예상 이슈**: 동일 사용자가 여러 번 잔액 충전/사용을 시도할 때 잔액 정보가 올바르게 업데이트되지 않는 문제, 동일 사용자가 동시에 여러 결제 요청을 시도할 때 결제 상태가 일관되지 않게 되는 문제
- **대상 공유 자원**: 사용자의 잔액
- **원인 및 설명**:
동일 사용자가 동시에 여러 번 잔액을 충전하거나 사용할 경우, 혹은 결제 요청의 경우, 트랜잭션 간의 동기화가 제대로 이루어지지 않으면 한 트랜잭션의 업데이트가 다른 트랜잭션에 의해 덮어써질 수 있다. 
예를 들어, 사용자가 잔액을 충전하려는 두 개의 요청을 거의 동시에 보낼 때, 두 요청이 충돌하여 하나의 충전만 반영되거나 잘못된 잔액이 기록될 수 있다.
예를 들어, 사용자가 동일한 서비스에서 발생한 요청에 대해 동시에 결제 요청을 보내면, 두 요청이 충돌하여 중복 결제가 발생할 수 있다.


### 취약 코드 분석

```java
// 충전
public class BalanceCharger {

    private final BalanceRepository balanceRepository;

    @Transactional
    public BalanceChargeInfo charge(BalanceChargeCommand command) {
        return balanceRepository.findByUserIdOptional(command.getUserId())
            .or(() -> Optional.of(createDefaultNewBalance(command.getUserId())))
            .map(balance -> balance.charge(command.getAmount(), command.getTransactionReason()))
            .map(balanceRepository::save)
            .map(BalanceChargeInfo::from)
            .orElseThrow(BalanceChargeUnAvailableException::new);
    }
}

// 사용
public BalanceUseInfo use(BalanceUseCommand command) {
  Balance balance = balanceRepository.findByUserId(command.getUserId());
  balance.use(command.getAmount(), command.getTransactionReason());
  return BalanceUseInfo.from(balanceRepository.save(balance));
}

// 결제
public class SimplePaymentService implements PaymentService {

  private final PaymentProcessor paymentProcessor;
  private final PaymentHistoryReader paymentHistoryReader;
  private final PaymentCanceller paymentCanceller;

  /**
   * @see {@link PaymentTransactionAspect}
   */
  public PaymentInfo processPayment(PaymentCommand paymentCommand) {
    return paymentProcessor.process(paymentCommand);
  }
}


public class PaymentProcessor {

  private final PaymentTransactionRepository paymentTransactionRepository;

  @Transactional
  public PaymentInfo process(PaymentCommand paymentCommand) {
    return	PaymentInfo.from(paymentTransactionRepository.save(paymentCommand.toEntity().withCompleted()));
  }
}

public class PaymentTransactionAspect {

  private final BalanceUseManager balanceUseManager;
  private final BalanceCharger balanceCharger;

  @Around("execution(* io.paymentservice.api.payment.business.service.PaymentService.processPayment(..)) && args(paymentCommand)")
  public PaymentInfo manageTransaction(ProceedingJoinPoint joinPoint, PaymentCommand paymentCommand) throws Throwable {
    try {
      balanceUseManager.use(paymentCommand(paymentCommand.getUserId(), paymentCommand.getAmount()));
    } catch (Exception e) {
      throw new PaymentProcessUnAvailableException(USER_BALANCE_USE_UNAVAILABLE, createFailed(paymentCommand), e);
    }

    try {
      return (PaymentInfo) joinPoint.proceed();
    } catch (Exception e) {
      balanceCharger.charge(rollbackCommand(paymentCommand.getUserId(), paymentCommand.getAmount()));
      throw new PaymentProcessUnAvailableException(PAYMENT_PROCESSING_FAILED, createFailed(paymentCommand), e);
    }
  }
}
```

1. **잔액 검색 및 업데이트**: 
`findByUserIdOptional` 메서드가 여러 스레드에서 동시에 호출될 때, 동일한 사용자의 잔액을 검색하고 업데이트하려는 시도가 중첩될 수 있다. 
각 트랜잭션이 서로 다른 잔액 값을 읽고 업데이트하여 최종적으로 저장되는 잔액 값이 일관되지 않게 된다.

2. **잔액 저장**: 
`save` 저장 전 다른 스레드에서 잔액이 이미 업데이트되어 저장될 경우, 최종 저장된 잔액 값이 예상과 다를 수 있다. 

3. **잔액 사용**:
`balance` 조회 후 사용, 저장 로직에서 `lost update` 문제가 발생할 수 있다. 

3. **결제**:
`balance` 차감 로직, `paymentTransaction` 생성 로직에서 `lost update` 및 `wrtie skew` 문제가 발생할 수 있다.



### 해결 방안 탐구

1) 락을 사용하지 않고 해결할 수 있는가? (유니크 인덱스, 멱등성) [✔]
- **판단**: 잔액 충전 및 사용은 불가, 결제는 가능  
- **근거**: 잔액 충전이나 사용은 멱등하지 않다. 결제의 경우 트랜잭션 히스토리를 쌓는 점에서 유니크 인덱스를 사용해 해결할 수 있다. 단 이때 중복 요청을 어떻게 정의할 것이냐에 대한 기준이 필요하다. 

2) 락 프리(lock-free, 낙관적 락)로 해결 가능한가? [✔]
- **판단**: 경우에 따라 가능하다.  
- **근거**: 충전 또는 사용 요청을 처리할 때, 먼저 잔액을 읽고 수정한 후, 저장 시점에 다른 트랜잭션이 잔액을 변경했는지 확인할 수 있다.

3) 비관적 락으로 해결 가능한가? [✔]
- **판단**: 경우에 따라 가능하다. 
- **근거**: `Balance` 엔티티를 검색할 때, 데이터베이스 수준에서 락을 걸 수 있다. 

4) 분산 락이나 메시지 큐로 동시성 제어가 가능하며 더 이점이 있나? [✔]
- **판단**: 가능하다.
- **근거**: 분산 락을 사용하면, 특정 사용자의 잔액에 대한 접근을 제어할 수 있다. 메시지 큐를 사용하면, 충전 및 사용 요청을 큐에 넣고 순차적으로 처리할 수 있다. 


### 선택

정책에 따라 다른 방식을 선택한다. 

1. 동시 요청에 대해, 단 1회만 성공시키고 나머지는 실패시키는 정책 

- 순서 상관 없이 반드시 1회만 성공시키고 나머지는 실패한다. 
- 가용 스레드 개수 보다 요청이 적은 경우 낙관적 락을 사용할 수 있다. 
- 그러나 요청이 많아지면 (e.g. 30개 ↑) 낙관적 락을 사용시 이상 현상이 발생한다. 이는 트랜잭션 경합과 롤백 재시도로 인해 예측 불가능한 잔액 변화가 발생하기 때문이다. 자세한 내용은 [정확히 1회 반영 정책에서의 낙관 락 한계](documents/trouble-shooting/limitations-on-optimistic-locking-for-exatly-once-processing.md) 글에서 살펴볼 수 있다. 
- 따라서 동시 요청을 명확히 판별하고 중복 요청을 필터링하는 비즈니스 로직이 필요하다.
- 그렇다면 여기서의 쟁점은 어떤 요청이 중복 요청이냐이다. 이에 대한 정책적 합의가 필요하다.  
- 예를 들어, 중복 결제에 대한 정의가 필요하다. 100ms 미만, 특정 필드가 모두 동일하면 중복 결제인가?
- 이 경우, `payment` 처리에서 결제 요청의 특성을 반영하여, DB의 유티크 제약조건을 사용하여 중복 요청을 방지하는 구현도 가능하다(거래 발생의 원천, 유저, 금액이 동일한 경우).

2. 동시 요청에 대해, 전부 수용하는 정책 

- 시간이 걸리더라도 모든 요청을 결국에는 수용하여 반영한다. 
- 예를 들어서 잔액 충전 요청이 10건 동시에 발생한다면, 모든 요청을 반영한다. (1000 충전 10 건 -> 10000 반영)
- 적은 요청에 대해서는 낙관적 락 + 적당한 retry를 사용할 수 있다. 
- 요청이 많은 경우, retry 횟수가 정해진 횟수를 초과할 것이므로 사용 불가하다.
- 비관적 락에 긴 타임아웃을 적용한다.


3. 오케스트레이션에서 분산 락을 구현하여 향후 정책에 따른 validation 필요시 사용한다.


##### 오케스트레이션 분산락 구현

```java
public class PaymentFacade {

	/**
	 * 사용자 예약에 대한 결제 처리
	 * 실패시 cancel 요청으로 payment 및 balance에 대한 롤백
	 *
	 * 결제에 대한 중복 요청 validation은 payment service에서 책임
	 */
	@DistributedLock(prefix = "api-orchestration-payment", keys={"#request.userId", "#request.targetId", "#request.amount", "#request.paymentTarget"} , timeUnit = MILLISECONDS, waitTime = 0, leaseTime = 500)
	public PaymentResponse processPayment(PaymentProcessRequest request) {

		PaymentResponse paymentResponse = PaymentResponse.from(paymentService.processPayment(request.toCommand()));
		try{
			reservationService.confirmReservation(createConfirmCommand(request));
		} catch (Exception e){
			paymentService.cancelPayment(paymentResponse.transactionId());
			return createRolledBackResponse(paymentResponse);
		}

		applicationEventPublisher.publishEvent(paymentResponse.toPaymentInternalEventAsComplete());
		return paymentResponse;
	}
}
```







# Before And After 

## 대기열 진입 시나리오 

### 통과해야 하는 테스트 


```
Feature: 대기열 토큰 생성 - 동시성 시나리오

  Scenario: 여러 사용자가 동시에 대기열에 진입하는 경우
    Given 10명의 random 유저 정보가 주어지고 대기열 토큰 생성을 동시에 요청하면 성공 응답을 받는다
    And 생성된 각 대기열 토큰의 성공 응답을 조회하면 각기 다른 포지션 정보가 확인되어야 한다 - 순서 정합성 보장

  Scenario: 동일한 사용자가 여러 번 대기열 진입을 요청하는 경우 중복 처리를 방지
    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 10번 동시에 요청하면 성공 응답을 받는다
      | userId | priority | requestAt |
      | user11 | 1        | now       |
    And 다음과 같은 유저 아이디로 대기열 토큰 전체 조회를 요청하면 성공 응답을 받고 그 개수는 1개여야 한다
      | userId |
      | user11 |
```



### Before

```java
public class SimpleWaitingQueueService{
    @Transactional
	public WaitingQueueTokenGenerateInfo generateAndEnqueue(WaitingQueueTokenGenerateCommand command) {
		WaitingQueueTokenCounter counter = counterManager.getIncreaseCounter(getMaxWaitingTokens());
		WaitingQueueToken newToken = createToken(command).init(counter);
		return WaitingQueueTokenGenerateInfo.from(enqueueRepository.enqueue(newToken));
	}
}
```


```java
@Entity
public class WaitingQueueTokenEntity implements EntityRecordable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long waitingQueueTokenId;
  
	// ...
}
```

![waiting-queue-concurrency-fail.png](documents%2Fconcurrency-problem%2Ftest-results%2Fwaiting-queue-concurrency-fail.png)



### After


```java
public class SimpleWaitingQueueService{
    @Transactional
	public WaitingQueueTokenGenerateInfo generateAndEnqueue(WaitingQueueTokenGenerateCommand command) {
		WaitingQueueTokenCounter counter = counterManager.getWithLockAndIncreaseCounter(getMaxWaitingTokens());
		WaitingQueueToken newToken = createToken(command).init(counter);
		return WaitingQueueTokenGenerateInfo.from(enqueueRepository.enqueue(newToken));
	}
}
```


```java
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uc_userid_status", columnNames = {"userId", "status"})
})
public class WaitingQueueTokenEntity implements EntityRecordable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long waitingQueueTokenId;
  
	// ...
}
```

![waiting-queue-concurrency-success.png](documents%2Fconcurrency-problem%2Ftest-results%2Fwaiting-queue-concurrency-success.png)




## 예약 시나리오

### 통과해야 하는 테스트

```
Feature: 예약 생성 동시성 테스트

  Background:
    Given 다음과 같은 콘서트 생성 요청을 보내고 성공 응답을 받는다
      | title  | requestAt |
      | 콘서트 제목 | now       |
    And 다음과 같은 콘서트 옵션 생성 요청을 보내고 성공 응답을 받는다
      | concertDate      | concertDuration | title | description | price | requestAt | maxSeats |
      | 2024-07-10T20:00 | PT2H            | 옵션 제목 | 옵션 설명       | 10000 | now       | 100      |

  Scenario: 여러 사용자가 동시에 동일한 좌석을 예약 시도
    When 각기 다른 random 유저가 동일한 좌석에 대해 10개의 예약 생성 요청을 동시에 보낸다
    Then 전체 예약을 repository에서 조회하여 다음과 같은 1개의 예약이 생성되었음을 확인한다
      | temporalReservationId |
      | 1                     |

```


### Before



```java
	@Transactional
	public TemporalReservationCreateInfo createTemporalReservation(ReservationCreateCommand command) {
		ConcertOption concertOption = concertOptionRepository.findById(command.getConcertOptionId()); 
		Seat seat = seatRepository.findSingleByCondition(onConcertOptionSeat(concertOption, command.getSeatNumber()));
		seatRepository.save(seat.doReserve());
		return TemporalReservationCreateInfo.from(temporalReservationRepository.save(create(command, concertOption, seat)));
	}
```

![reservation-concurrency-fail.png](documents%2Fconcurrency-problem%2Ftest-results%2Freservation-concurrency-fail.png)


### After 



```java
public class SimpleReservationCrudService {
	@Transactional
	public TemporalReservationCreateInfo createTemporalReservation(ReservationCreateCommand command) {
		ConcertOption concertOption = concertOptionRepository.findByIdWithSLock(command.getConcertOptionId()); // read / write 분리를 고려한 s lock
		Seat seat = seatRepository.findSingleByConditionWithLock(onConcertOptionSeat(concertOption, command.getSeatNumber()));
		seatRepository.save(seat.doReserve());
		return TemporalReservationCreateInfo.from(temporalReservationRepository.save(create(command, concertOption, seat)));
	}
}
```

![reservation-concurrency-success.png](documents%2Fconcurrency-problem%2Ftest-results%2Freservation-concurrency-success.png)




### 잔액 충전/사용/결제 시나리오 

### 통과해야 하는 테스트


```
Feature: 잔액 충전 기능 - 동시성 시나리오
  
  Scenario: 동시에 여러 번 balance를 충전한다 - 동시성 시나리오 - 모든 요청 수용 정책
    Given 사용자의 id가 1이고 충전 금액이 1000인 경우 잔액을 충전 요청하고 정상 응답을 받는다
    When 사용자의 id가 1이고 충전 금액이 500인 잔액 충전 요청을 동시에 300번 보낸다
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 조회한 사용자의 잔액은 151000이어야 한다
```

```
Feature: 잔액 사용 기능 - 동시성 시나리오
  
  Background:
    Given 사용자의 id가 1이고 충전 금액이 500000인 경우 잔액을 충전 요청하고 정상 응답을 받는다

  Scenario: 동시에 여러 번 balance를 사용한다 - 동시성 시나리오 - 모든 요청 수용 정책
    When 사용자의 id가 1이고 사용 금액이 1000인 잔액 사용 요청을 동시에 300번 보낸다
    And 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 조회한 사용자의 잔액은 200000이어야 한다
```

```
Feature: 결제 동시성 처리 기능

  Background:
    Given 사용자의 id가 1이고 충전 금액이 5000인 경우 잔액을 충전 요청하고 정상 응답을 받는다

  Scenario: 동일 사용자가 동시에 상이한 결제 요청을 시도한다 - 성공 시나리오
    Given 동일한 사용자가 다음과 같은 결제 요청을 동시에 시도한다
      | userId | targetId | amount | paymentMethod |
      | 1      | A2       | 1000   | CREDIT_CARD   |
      | 1      | A3       | 1000   | CREDIT_CARD   |
    When 사용자의 id가 1인 경우 결제 내역을 조회 요청하고 정상 응답을 받는다
    Then 조회한 결제 내역은 다음과 같아야 한다
      | transactionId | userId | amount | paymentStatus |
      | 1             | 1      | 1000   | COMPLETE       |
      | 2             | 1      | 1000    | COMPLETE        |
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 조회한 사용자의 잔액은 3000이어야 한다

  Scenario: 동일 사용자가 동시에 같은 결제 요청을 시도한다 - 성공 시나리오(동일한 결제도 허용하는 정책)
    Given 동일한 사용자가 다음과 같은 결제 요청을 동시에 시도한다
      | userId | targetId | amount | paymentMethod |
      | 1      | A4       | 1000   | CREDIT_CARD   |
      | 1      | A4       | 1000   | CREDIT_CARD   |
    When 사용자의 id가 1인 경우 결제 내역을 조회 요청하고 정상 응답을 받는다
    Then 조회한 결제 내역은 모두 COMPLETE여야 한다
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 조회한 사용자의 잔액은 3000이어야 한다

  Scenario: 동일 사용자가 잔액 부족 시 동시에 여러 결제 요청을 시도한다
    Given 동일한 사용자가 다음과 같은 결제 요청을 동시에 시도한다
      | userId | targetId | amount | paymentMethod |
      | 1      | A7       | 4000   | CREDIT_CARD   |
      | 1      | A8       | 2000   | CREDIT_CARD   |
      | 1      | A9       | 3500   | CREDIT_CARD   |
    When 사용자의 id가 1인 경우 결제 내역을 조회 요청하고 정상 응답을 받는다
    Then 조회한 결제 내역에서 한 건은 COMPLETE이고 나머지는 FAILED여야 한다
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    And 조회한 사용자의 잔액은 3000 이하여야 한다
```



### Before


```java
public class BalanceCharger {

	@Transactional
	public BalanceChargeInfo charge(BalanceChargeCommand command) {
		return balanceRepository.findByUserIdOptional(command.getUserId())
			.or(() -> Optional.of(createDefaultNewBalance(command.getUserId())))
			.map(balance -> balance.charge(command.getAmount(), command.getTransactionReason()))
			.map(balanceRepository::save)
			.map(BalanceChargeInfo::from)
			.orElseThrow(BalanceChargeUnAvailableException::new);
	}
}
```



```java
public class SimpleBalanceUseManager implements BalanceUseManager {

	@Transactional
	public BalanceUseInfo use(BalanceUseCommand command) {
		Balance balance = balanceRepository.findByUserIdOptional(command.getUserId()).orElseThrow(BalanceNotFoundException::new);
		balance.use(command.getAmount(), command.getTransactionReason());
		return BalanceUseInfo.from(balanceRepository.save(balance));
	}
}
```



![balance-charge-fail.png](documents%2Fconcurrency-problem%2Ftest-results%2Fbalance-charge-fail.png)

![balance-use-fail.png](documents%2Fconcurrency-problem%2Ftest-results%2Fbalance-use-fail.png)

![payment-fail.png](documents%2Fconcurrency-problem%2Ftest-results%2Fpayment-fail.png)



### After



```java
public class BalanceCharger {

	@Transactional
	public BalanceChargeInfo charge(BalanceChargeCommand command) {
		return balanceRepository.findSingleByConditionOptionalWithLock(onUser(command))
			.or(() -> Optional.of(createDefaultNewBalance(command.getUserId())))
			.map(balance -> balance.charge(command.getAmount(), command.getTransactionReason()))
			.map(balanceRepository::save)
			.map(BalanceChargeInfo::from)
			.orElseThrow(BalanceChargeUnAvailableException::new);
	}
}
```


```java
public class SimpleBalanceUseManager implements BalanceUseManager {

	@Transactional
	public BalanceUseInfo use(BalanceUseCommand command) {
		Balance balance = balanceRepository.findSingleByConditionWithLock(onUser(command));
		balance.use(command.getAmount(), command.getTransactionReason());
		return BalanceUseInfo.from(balanceRepository.save(balance));
	}
}
```


![balance-charge-success.png](documents%2Fconcurrency-problem%2Ftest-results%2Fbalance-charge-success.png)
 

![balance-use-success.png](documents%2Fconcurrency-problem%2Ftest-results%2Fbalance-use-success.png)


![payment-success.png](documents%2Fconcurrency-problem%2Ftest-results%2Fpayment-success.png)



## 결제 오케스트레이션 시나리오

### 통과해야 하는 테스트


```
  Scenario: 동일 사용자가 동일한 결제 요청을 시도한다 - 동시성 제어 시나리오
    Given 동일한 사용자가 가장 최근의 예약에 대해 다음과 같은 결제 요청을 동시에 시도한다
      | userId | amount | paymentMethod | requestAt | paymentTarget |
      | 1      | 1000   | CREDIT_CARD   | now       | RESERVATION   |
      | 1      | 1000   | CREDIT_CARD   | now       | RESERVATION   |
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 최종 사용자의 잔액은 충전된 잔액에서 사용한 잔액 금액 1000이 차감되어 반영되어 있어야 한다
```



### Before 

![orchestration-payment-fail.png](documents%2Fconcurrency-problem%2Ftest-results%2Forchestration-payment-fail.png)


### After 


![orchestration-payment-success.png](documents%2Fconcurrency-problem%2Ftest-results%2Forchestration-payment-success.png)








## 참고 자료

- https://ko.wikipedia.org/wiki/%EB%8F%99%EA%B8%B0%ED%99%94
- https://malaysia86.blogspot.com/p/when-concurrent-transactions-were-made.html
- https://nailyourinterview.org/interview-resources/operating-systems/critical-section-problem
- https://www.webdevway.com/2022/11/producer-consumer-semaphore.html
- https://medium.com/@bindubc/distributed-system-concurrency-problem-in-relational-database-59866069ca7c
- https://medium.com/trendyol-tech/how-we-solved-kafka-event-loss-problem-by-breaking-it-down-c60bfc1518c1
- https://redisson.org/lettuce-replacement-why-redisson-is-the-best-lettuce-alternative.html
- 유튜브 [쉬운코드](https://www.youtube.com/results?search_query=%EC%89%AC%EC%9A%B4+%EC%BD%94%EB%93%9C+%EB%8F%99%EA%B8%B0%ED%99%94)
- 데이터 중심 애플리케이션 설계(마틴 클레피만 저 | 위키북스)




</details>






</details>




