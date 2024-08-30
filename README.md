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




</details>


<details>



<summary><b>Trouble Shooting</b></summary>




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







<details>
<summary><b>캐싱을 이용한 API 및 쿼리 개선</b></summary>


# 1. 요구사항

**선택한 시나리오에서의 Query 분석 및 캐싱 전략 설계**


> 조회가 오래 걸리는 쿼리에 대한 캐싱, 혹은 Redis 를 이용한 로직 이관을 통해 성능 개선할 수 있는 로직을 분석

**주요 포인트**
- 각 시나리오에서 발생하는 Query 에 대한 충분한 이해하기
- 대량의 트래픽 발생시 지연이 발생할 수 있는 조회쿼리에 대해 분석하고, 이에 대한 결과를 작성하기


# 2. 요구사항 이해하기



<details>
<summary><b>질문과 답변</b></summary>

**질문**

```
이번 주차 Step 13 과제 요구사항에 대한 이해가 필요해서 문의드립니다!

아래의 Step 13 과제 캐시와 DB 조회 부하에 대한 이해 채점 기준에 따르면 쿼리에 대한 이해를 요구하는 것으로 이해가 됩니다.

각 시나리오에서 발생하는 Query 에 대한 충분한 이해가 있는지
대량의 트래픽 발생시 지연이 발생할 수 있는 조회쿼리에 대해 분석하고, 이에 대한 결과를 작성하였는지

제가 처음 생각한 사고의 흐름은 아래와 같았는데요.

대량의 트래픽에서 슬로우 쿼리를 찾아야 하는구나 -> 그러면 대량의 더미 데이터가 필요하겠네  -> 그런 뒤에 성능 테스트를 돌려서 슬로우 쿼리를 찾자

그런데 생각해 보니 대량의 데이터에서 인덱스가 없는 경우 어떤 경우를 조회해도 쿼리 성능은 잘 나오지 않을 것 같습니다. :thinking_face:

현재 스텝에서 캐싱이 주된 주제이고, 조회 쿼리에 대한 캐싱을 통한 개선이 1차 목표인 것으로 이해 했는데요.

그렇다면
인덱스 없이 일단 슬로우 쿼리를 찾기 (대량의 트래픽 발생시 지연이 발생할 수 있는 조회쿼리 에 대한 요구 사항) 
해당 쿼리에 대한 분석하기 (각 시나리오에서 발생하는 Query 에 대한 충분한 이해가 있는지 에 대한 요구사항) 
해당 쿼리에 대한 캐싱을 통해 성능을 개선하기 (분석하고, 이에 대한 결과를 작성  에 대한 요구 사항)

이와 같은 내용으로 접근하는 것이 과제의 요구 사항에 대한 이해가 맞는지 문의드립니다!
```

**답변**

```
네네 추가 인덱스 설정 없이 트래픽 발생 시 지연이 발생할 수 있는 부분을 캐싱 처리를 통해 성능을 개선하는것이 목적 입니다.
(더미 DATA를 넣고 테스트 진행하는 것을 권장 드립니다.)
```


**질문**

```
`쿼리에 대한 이해 및 분석` 을 어떻게 풀어야 할지 잘 감이 안옵니다.
    
인덱스가 없는 경우 대량의 데이터에 대한 조회 성능이 나쁜 점은 어떻게 보면 자명해보여서, 이런 수치로 느리다 빠르다를 판단할 수가 있는 것인지부터 의문이 듭니다.

혹은 조회가 아닌 CUD에 대한 성능 테스트가 필요한 것인지(캐싱과 연결지으려면 조회 중심으로 테스트를 해야 한다고 생각하고 있는데 이 가정이 틀린 것인지), 마찬가지로 그 경우라면 어떤 부분을 중점적으로 보고 ‘쿼리’ 에 대해서 ‘지연 여부’를 어떻게 정의하고, 어떻게 분석해야 하는 것인지, 감이 잘 안와서 ㅠㅠ 조언 부탁드립니다!
```


```
쿼리에 대한 직접적인 성능 판단과 함께 비즈니스적으로 특정 쿼리 패턴이 어떻게 사용해볼지도 생각해보자. 
```



</details>

본 과제의 요구사항은 비즈니스 기반의 쿼리 패턴 사용 사례를 분석하고, 개선 포인트를 위한 캐싱 전략을 설계 및 구현하는 것이다.
이때 전제는 인덱스는 고려하지 않는다.
아래의 스텝을 따라 진행한다.


- 유스케이스별 잠재적 이슈 분석
- 쿼리 성능 확인
- 개선사항 도출





# 3. 분석



## 1. 예약 가능 날짜 조회 API (`GET /api/availability/dates/{concertId}`)
### 설명
- 이 API는 특정 콘서트의 예약 가능한 날짜를 조회한다.
- 사용되는 쿼리는 대개 concertId를 기준으로 해당 콘서트의 가능한 모든 예약 날짜를 반환한다.

### 분석
- **조회 빈도**: 예약 가능한 날짜는 예약 시 자주 조회된다.
- **변경 빈도**: 콘서트 일정이 자주 변경되지 않으므로 데이터 변경 빈도는 낮다.
- **쿼리 성능 추정**: 단일 concertId를 기준으로 하므로 인덱스만 잘 설정되어 있다면 성능이 좋을 것이다.

### 캐싱이 필요한 유스케이스

1. **콘서트 예약 페이지 로드 시**
  - 사용자들이 콘서트 예약 페이지를 방문할 때마다 예약 가능한 날짜를 조회한다.

2. **예약 가능 날짜를 기반으로 한 달력 뷰 생성 시**
  - 여러 사용자가 동시에 예약 가능한 날짜를 달력 형식으로 조회하는 경우, 캐시를 사용하여 동일한 데이터를 반복적으로 조회하지 않도록 최적화할 수 있다.


### 분석 종합

- 분석에 따른 캐싱 적절도: ★★★★★
- 근거: 데이터 변경이 적고 조회 빈도가 높을 것으로 예상된다.


### 선택

1. **캐싱 전략: Look-aside vs Write-back**
  - **Look-aside 캐싱** 선택
  - 예약 가능한 날짜는 읽기 빈도가 높고, 데이터 변경 빈도는 낮다.

2. **캐시 유형 결정: 로컬 캐시 vs 글로벌 캐시**
  - **글로벌 캐시** 선택
  - 예약 가능한 날짜는 서로 다른 여러 유저로부터 동일하게 조회될 가능성이 높다.
  - 글로벌 캐시를 사용하는 경우 공유 자원을 중앙에서 관리함으로써 동기화 과정이 비교적 수월하고, 일관된 데이터를 제공하기 용이하다.


**결론:**
- **캐싱 전략**: Look-aside 캐싱
- **캐시 유형**: 글로벌 캐시 (Redis)




## 2. 예약 가능 좌석 조회 API (`GET /api/availability/seats/{concertOptionId}/{requestAt}`)
### 설명
- 이 API는 특정 콘서트 옵션에 대해 요청 시점(requestAt) 기준으로 예약 가능한 좌석을 조회한다.
- 사용되는 쿼리는 concertOptionId와 requestAt을 기준으로 해당 콘서트 옵션의 가능한 모든 예약 좌석을 반환한다.

### 분석
- **조회 빈도**: 예약 가능 좌석은 예약 과정에서 자주 조회된다.
- **변경 빈도**: 좌석 예약 상황에 따라 변경이 발생할 수 있어 데이터 변경 빈도는 중간 정도이다.
- **쿼리 성능 추정**: 인덱스가 적절히 설정되어 있다면 성능이 좋을 수 있지만, 많은 동시 조회 시 성능 저하 가능성이 있다.

### 캐싱이 필요한 유스케이스

1. **콘서트 좌석 선택 페이지 로드 시**
  - 사용자들이 좌석 선택 페이지를 방문할 때마다 예약 가능한 좌석을 조회한다.

2. **여러 사용자들이 동일한 콘서트 옵션의 좌석을 조회 시**
  - 여러 사용자가 동시에 동일한 콘서트 옵션의 예약 가능한 좌석을 조회하는 경우, 캐시를 사용하여 동일한 데이터를 반복적으로 조회하지 않도록 최적화할 수 있다.

3. **특이 케이스**:
  - 특정 유저들이 의사 결정 과정에서 여러 차례 반복적인 조회하는 경우가 가능하다.

### 분석 종합
- 분석에 따른 캐싱 적절도: ★★★★☆
- 근거: 조회 빈도가 높고 변경 빈도가 중간 정도이기 때문에 캐싱이 유효할 수 있다.

### 선택

1. **캐싱 전략: Look-aside vs Write-back**
  - **Look-aside 캐싱** 선택
  - 예약 가능 좌석은 읽기 빈도가 높고 데이터 변경 빈도가 중간 정도이다.

2. **캐시 유형 결정: 로컬 캐시 vs 글로벌 캐시**
  - **글로벌 캐시** 선택
  - 예약 가능 좌석 데이터는 여러 서버 인스턴스에서 동일하게 조회될 가능성이 높고, 데이터 일관성이 중요하다.

**결론:**
- **캐싱 전략**: Look-aside 캐싱
- **캐시 유형**: 글로벌 캐시 (Redis)



## 3. 예약 상태 조회 API (`GET /api/reservations/status/{userId}/{concertOptionId}`)
### 설명
- 이 API는 특정 사용자(userId)가 특정 콘서트 옵션(concertOptionId)에 대해 예약한 상태를 조회한다.
- 사용되는 쿼리는 userId와 concertOptionId를 기준으로 해당 사용자의 예약 상태를 반환한다.

### 분석
- **조회 빈도**: 예약 상태는 사용자가 예약 상황을 확인할 때 자주 조회된다.
- **변경 빈도**: 예약 상태는 예약 확정, 취소 등으로 인해 변경이 발생할 수 있어 데이터 변경 빈도는 중간 정도이다.
- **쿼리 성능 추정**: 인덱스가 적절히 설정되어 있다면 성능이 좋을 수 있지만, 많은 동시 조회 시 성능 저하 가능성이 있다.

### 캐싱이 필요한 유스케이스

1. **사용자가 자신의 예약 상태를 확인할 때**
  - 사용자들이 예약 후 자신의 예약 상태를 확인하는 경우가 많아 조회 빈도가 높다.
  - 사용자의 행동 패턴을 고려할 때, 초기 조회 수 단기간에 반복적인 조회가 가능하다.

2. **관리자가 다수의 사용자 예약 상태를 조회 시**
  - 관리자가 다수 사용자의 예약 상태를 조회하는 경우, 캐시를 사용하여 동일한 데이터를 반복적으로 조회하지 않도록 최적화할 수 있다.

### 분석 종합
- 분석에 따른 캐싱 적절도: ★★★☆☆
- 근거: 조회 빈도가 높고 변경 빈도가 중간 정도이기 때문에 캐싱이 유효할 수 있다.

### 선택

1. **캐싱 전략: Look-aside vs Write-back**
  - **Look-aside 캐싱** 선택
  - 예약 상태는 읽기 빈도가 높고 데이터 변경 빈도가 중간 정도이다.

2. **캐시 유형 결정: 로컬 캐시 vs 글로벌 캐시**
  - **글로벌 캐시** 선택
  - 이 API는 사용자별로 개인화된 데이터를 조회하므로, 전역 자원에서 데이터를 관리하기보다는 각 인스턴스에서 데이터를 관리하는 로컬 캐시를 사용하는 것이 적합하다.
  - 로컬 캐시는 드라마틱한 성능 개선보다는, 특정 사용자에 대한 집중적인 요청이 발생하는 상황이나 단기간의 부하와 같은 특수 케이스에 대한 방어 목적에 초점을 맞춘다.
  - 따라서, 데이터의 일관성을 유지하면서도 효율적인 캐싱을 위해 짧은 TTL(e.g. 1분)을 설정한다.

**결론:**
- **캐싱 전략**: Look-aside 캐싱
- **캐시 유형**: 로컬 캐시 (짧은 TTL)



## 4. 결제 내역 조회 API (`GET /api/user-balance/payment/history/{userId}`)
### 설명
- 이 API는 특정 사용자(userId)의 결제 내역을 조회한다.
- 사용되는 쿼리는 userId를 기준으로 해당 사용자의 모든 결제 내역을 반환한다.

### 분석
- **조회 빈도**: 결제 내역은 사용자가 자신의 거래 내역을 확인할 때 자주 조회된다.
- **변경 빈도**: 결제나 취소 시 변경이 발생하므로 데이터 변경 빈도는 중간 정도이다.
- **쿼리 성능 추정**: 인덱스가 적절히 설정되어 있다면 성능이 좋을 수 있지만, 많은 동시 조회 시 성능 저하 가능성이 있다.

### 캐싱이 필요한 유스케이스

1. **사용자가 자신의 결제 내역을 확인할 때**
  - 사용자들이 결제 후 자신의 결제 내역을 확인하는 경우가 많아 조회 빈도가 높다.
  - 사용자의 행동 패턴을 고려할 때, 초기 조회 수 단기간에 반복적인 조회가 가능하다.

2. **관리자가 다수의 사용자 결제 내역을 조회 시**
  - 관리자가 다수 사용자의 결제 내역을 조회하는 경우, 캐시를 사용하여 동일한 데이터를 반복적으로 조회하지 않도록 최적화할 수 있다.

### 분석 종합
- 분석에 따른 캐싱 적절도: ★★★☆☆
- 근거: 조회 빈도가 높고 변경 빈도가 중간 정도이기 때문에 캐싱이 유효할 수 있다.

### 선택

1. **캐싱 전략: Look-aside vs Write-back**
  - **Look-aside 캐싱** 선택
  - 결제 내역은 읽기 빈도가 높고 데이터 변경 빈도가 중간 정도이다.

2. **캐시 유형 결정: 로컬 캐시 vs 글로벌 캐시**
  - **글로벌 캐시** 선택
  - 이 API는 사용자별로 개인화된 데이터를 조회하므로, 전역 자원에서 데이터를 관리하기보다는 각 인스턴스에서 데이터를 관리하는 로컬 캐시를 사용하는 것이 적합하다.
  - 로컬 캐시는 드라마틱한 성능 개선보다는, 특정 사용자에 대한 집중적인 요청이 발생하는 상황이나 단기간의 부하와 같은 특수 케이스에 대한 방어 목적에 초점을 맞춘다.
  - 따라서, 데이터의 일관성을 유지하면서도 효율적인 캐싱을 위해 짧은 TTL(e.g. 1분)을 설정한다.



**결론:**
- **캐싱 전략**: Look-aside 캐싱
- **캐시 유형**: 로컬 캐시 (짧은 TTL)



## 5. 대기열 순번 조회 API (`GET /api/waiting-queue-token/{userId}`)
### 설명
- 이 API는 특정 사용자(userId)의 대기열 순번을 조회한다.
- 사용되는 쿼리는 userId를 기준으로 해당 사용자의 대기열 순번을 반환한다.

### 분석
- **조회 빈도**: 대기열 순번은 대기 중인 사용자가 자주 조회한다.
- **변경 빈도**: 대기열 상태는 자주 변경되므로 데이터 변경 빈도는 매우 높다.
- **쿼리 성능 추정**: 인덱스가 적절히 설정되어 있어도 자주 변경되므로 성능 저하 가능성이 크다.

### 캐싱이 필요한 유스케이스

1. **사용자가 자신의 대기열 순번을 확인할 때**
  - 사용자들이 대기 중 자신의 순번을 자주 확인하는 경우가 많아 조회 빈도가 높다.

### 분석 종합
- 분석에 따른 캐싱 적절도: ☆☆☆☆☆
- 근거: 데이터 변경 빈도가 매우 높아 캐싱의 효과가 크지 않다.

### 선택

**캐시 사용하지 않음**
- 대기열 순번 조회 요구는 많지만, 지나치게 개인화된 정보이며 변경이 매우 잦으므로, 캐시를 사용하지 않는 것이 적합하다.
- 실시간 처리가 필요하므로 **Redis**를 사용하여 기존의 RDB를 이용한 구현을 레디스를 이용한 구현으로 리팩토링 하는 것으로 개선한다.

**결론:**
- **캐싱 전략**: 캐시 사용하지 않음
- **대안 전략**: Redis를 사용한 실시간 처리






# 4. 구현




# 3. 분석



## 1. 예약 가능 날짜 조회 API (`GET /api/availability/dates/{concertId}`)
### 설명
- 이 API는 특정 콘서트의 예약 가능한 날짜를 조회한다.
- 사용되는 쿼리는 대개 concertId를 기준으로 해당 콘서트의 가능한 모든 예약 날짜를 반환한다.

### 분석
- **조회 빈도**: 예약 가능한 날짜는 예약 시 자주 조회된다.
- **변경 빈도**: 콘서트 일정이 자주 변경되지 않으므로 데이터 변경 빈도는 낮다.
- **쿼리 성능 추정**: 단일 concertId를 기준으로 하므로 인덱스만 잘 설정되어 있다면 성능이 좋을 것이다.

### 캐싱이 필요한 유스케이스

1. **콘서트 예약 페이지 로드 시**
- 사용자들이 콘서트 예약 페이지를 방문할 때마다 예약 가능한 날짜를 조회한다.

2. **예약 가능 날짜를 기반으로 한 달력 뷰 생성 시**
- 여러 사용자가 동시에 예약 가능한 날짜를 달력 형식으로 조회하는 경우, 캐시를 사용하여 동일한 데이터를 반복적으로 조회하지 않도록 최적화할 수 있다.


### 분석 종합

- 분석에 따른 캐싱 적절도: ★★★★★
- 근거: 데이터 변경이 적고 조회 빈도가 높을 것으로 예상된다.


### 선택

1. **캐싱 전략: Look-aside vs Write-back**
- **Look-aside 캐싱** 선택
- 예약 가능한 날짜는 읽기 빈도가 높고, 데이터 변경 빈도는 낮다.

2. **캐시 유형 결정: 로컬 캐시 vs 글로벌 캐시**
- **글로벌 캐시** 선택
- 예약 가능한 날짜는 서로 다른 여러 유저로부터 동일하게 조회될 가능성이 높다.
- 글로벌 캐시를 사용하는 경우 공유 자원을 중앙에서 관리함으로써 동기화 과정이 비교적 수월하고, 일관된 데이터를 제공하기 용이하다.


**결론:**
- **캐싱 전략**: Look-aside 캐싱
- **캐시 유형**: 글로벌 캐시 (Redis)




## 2. 예약 가능 좌석 조회 API (`GET /api/availability/seats/{concertOptionId}/{requestAt}`)
### 설명
- 이 API는 특정 콘서트 옵션에 대해 요청 시점(requestAt) 기준으로 예약 가능한 좌석을 조회한다.
- 사용되는 쿼리는 concertOptionId와 requestAt을 기준으로 해당 콘서트 옵션의 가능한 모든 예약 좌석을 반환한다.

### 분석
- **조회 빈도**: 예약 가능 좌석은 예약 과정에서 자주 조회된다.
- **변경 빈도**: 좌석 예약 상황에 따라 변경이 발생할 수 있어 데이터 변경 빈도는 중간 정도이다.
- **쿼리 성능 추정**: 인덱스가 적절히 설정되어 있다면 성능이 좋을 수 있지만, 많은 동시 조회 시 성능 저하 가능성이 있다.

### 캐싱이 필요한 유스케이스

1. **콘서트 좌석 선택 페이지 로드 시**
- 사용자들이 좌석 선택 페이지를 방문할 때마다 예약 가능한 좌석을 조회한다.

2. **여러 사용자들이 동일한 콘서트 옵션의 좌석을 조회 시**
- 여러 사용자가 동시에 동일한 콘서트 옵션의 예약 가능한 좌석을 조회하는 경우, 캐시를 사용하여 동일한 데이터를 반복적으로 조회하지 않도록 최적화할 수 있다.

3. **특이 케이스**:
- 특정 유저들이 의사 결정 과정에서 여러 차례 반복적인 조회하는 경우가 가능하다.

### 분석 종합
- 분석에 따른 캐싱 적절도: ★★★★☆
- 근거: 조회 빈도가 높고 변경 빈도가 중간 정도이기 때문에 캐싱이 유효할 수 있다.

### 선택

1. **캐싱 전략: Look-aside vs Write-back**
- **Look-aside 캐싱** 선택
- 예약 가능 좌석은 읽기 빈도가 높고 데이터 변경 빈도가 중간 정도이다.

2. **캐시 유형 결정: 로컬 캐시 vs 글로벌 캐시**
- **글로벌 캐시** 선택
- 예약 가능 좌석 데이터는 여러 서버 인스턴스에서 동일하게 조회될 가능성이 높고, 데이터 일관성이 중요하다.

**결론:**
- **캐싱 전략**: Look-aside 캐싱
- **캐시 유형**: 글로벌 캐시 (Redis)



## 3. 예약 상태 조회 API (`GET /api/reservations/status/{userId}/{concertOptionId}`)
### 설명
- 이 API는 특정 사용자(userId)가 특정 콘서트 옵션(concertOptionId)에 대해 예약한 상태를 조회한다.
- 사용되는 쿼리는 userId와 concertOptionId를 기준으로 해당 사용자의 예약 상태를 반환한다.

### 분석
- **조회 빈도**: 예약 상태는 사용자가 예약 상황을 확인할 때 자주 조회된다.
- **변경 빈도**: 예약 상태는 예약 확정, 취소 등으로 인해 변경이 발생할 수 있어 데이터 변경 빈도는 중간 정도이다.
- **쿼리 성능 추정**: 인덱스가 적절히 설정되어 있다면 성능이 좋을 수 있지만, 많은 동시 조회 시 성능 저하 가능성이 있다.

### 캐싱이 필요한 유스케이스

1. **사용자가 자신의 예약 상태를 확인할 때**
- 사용자들이 예약 후 자신의 예약 상태를 확인하는 경우가 많아 조회 빈도가 높다.
- 사용자의 행동 패턴을 고려할 때, 초기 조회 수 단기간에 반복적인 조회가 가능하다.

2. **관리자가 다수의 사용자 예약 상태를 조회 시**
- 관리자가 다수 사용자의 예약 상태를 조회하는 경우, 캐시를 사용하여 동일한 데이터를 반복적으로 조회하지 않도록 최적화할 수 있다.

### 분석 종합
- 분석에 따른 캐싱 적절도: ★★★☆☆
- 근거: 조회 빈도가 높고 변경 빈도가 중간 정도이기 때문에 캐싱이 유효할 수 있다.

### 선택

1. **캐싱 전략: Look-aside vs Write-back**
- **Look-aside 캐싱** 선택
- 예약 상태는 읽기 빈도가 높고 데이터 변경 빈도가 중간 정도이다.

2. **캐시 유형 결정: 로컬 캐시 vs 글로벌 캐시**
- **글로벌 캐시** 선택
- 이 API는 사용자별로 개인화된 데이터를 조회하므로, 전역 자원에서 데이터를 관리하기보다는 각 인스턴스에서 데이터를 관리하는 로컬 캐시를 사용하는 것이 적합하다.
- 로컬 캐시는 드라마틱한 성능 개선보다는, 특정 사용자에 대한 집중적인 요청이 발생하는 상황이나 단기간의 부하와 같은 특수 케이스에 대한 방어 목적에 초점을 맞춘다.
- 따라서, 데이터의 일관성을 유지하면서도 효율적인 캐싱을 위해 짧은 TTL(e.g. 1분)을 설정한다.

**결론:**
- **캐싱 전략**: Look-aside 캐싱
- **캐시 유형**: 로컬 캐시 (짧은 TTL)



## 4. 결제 내역 조회 API (`GET /api/user-balance/payment/history/{userId}`)
### 설명
- 이 API는 특정 사용자(userId)의 결제 내역을 조회한다.
- 사용되는 쿼리는 userId를 기준으로 해당 사용자의 모든 결제 내역을 반환한다.

### 분석
- **조회 빈도**: 결제 내역은 사용자가 자신의 거래 내역을 확인할 때 자주 조회된다.
- **변경 빈도**: 결제나 취소 시 변경이 발생하므로 데이터 변경 빈도는 중간 정도이다.
- **쿼리 성능 추정**: 인덱스가 적절히 설정되어 있다면 성능이 좋을 수 있지만, 많은 동시 조회 시 성능 저하 가능성이 있다.

### 캐싱이 필요한 유스케이스

1. **사용자가 자신의 결제 내역을 확인할 때**
- 사용자들이 결제 후 자신의 결제 내역을 확인하는 경우가 많아 조회 빈도가 높다.
- 사용자의 행동 패턴을 고려할 때, 초기 조회 수 단기간에 반복적인 조회가 가능하다.

2. **관리자가 다수의 사용자 결제 내역을 조회 시**
- 관리자가 다수 사용자의 결제 내역을 조회하는 경우, 캐시를 사용하여 동일한 데이터를 반복적으로 조회하지 않도록 최적화할 수 있다.

### 분석 종합
- 분석에 따른 캐싱 적절도: ★★★☆☆
- 근거: 조회 빈도가 높고 변경 빈도가 중간 정도이기 때문에 캐싱이 유효할 수 있다.

### 선택

1. **캐싱 전략: Look-aside vs Write-back**
- **Look-aside 캐싱** 선택
- 결제 내역은 읽기 빈도가 높고 데이터 변경 빈도가 중간 정도이다.

2. **캐시 유형 결정: 로컬 캐시 vs 글로벌 캐시**
- **글로벌 캐시** 선택
- 이 API는 사용자별로 개인화된 데이터를 조회하므로, 전역 자원에서 데이터를 관리하기보다는 각 인스턴스에서 데이터를 관리하는 로컬 캐시를 사용하는 것이 적합하다.
- 로컬 캐시는 드라마틱한 성능 개선보다는, 특정 사용자에 대한 집중적인 요청이 발생하는 상황이나 단기간의 부하와 같은 특수 케이스에 대한 방어 목적에 초점을 맞춘다.
- 따라서, 데이터의 일관성을 유지하면서도 효율적인 캐싱을 위해 짧은 TTL(e.g. 1분)을 설정한다.



**결론:**
- **캐싱 전략**: Look-aside 캐싱
- **캐시 유형**: 로컬 캐시 (짧은 TTL)



## 5. 대기열 순번 조회 API (`GET /api/waiting-queue-token/{userId}`)
### 설명
- 이 API는 특정 사용자(userId)의 대기열 순번을 조회한다.
- 사용되는 쿼리는 userId를 기준으로 해당 사용자의 대기열 순번을 반환한다.

### 분석
- **조회 빈도**: 대기열 순번은 대기 중인 사용자가 자주 조회한다.
- **변경 빈도**: 대기열 상태는 자주 변경되므로 데이터 변경 빈도는 매우 높다.
- **쿼리 성능 추정**: 인덱스가 적절히 설정되어 있어도 자주 변경되므로 성능 저하 가능성이 크다.

### 캐싱이 필요한 유스케이스

1. **사용자가 자신의 대기열 순번을 확인할 때**
- 사용자들이 대기 중 자신의 순번을 자주 확인하는 경우가 많아 조회 빈도가 높다.

### 분석 종합
- 분석에 따른 캐싱 적절도: ☆☆☆☆☆
- 근거: 데이터 변경 빈도가 매우 높아 캐싱의 효과가 크지 않다.

### 선택

**캐시 사용하지 않음**
- 대기열 순번 조회 요구는 많지만, 지나치게 개인화된 정보이며 변경이 매우 잦으므로, 캐시를 사용하지 않는 것이 적합하다.
- 실시간 처리가 필요하므로 **Redis**를 사용하여 기존의 RDB를 이용한 구현을 레디스를 이용한 구현으로 리팩토링 하는 것으로 개선한다.

**결론:**
- **캐싱 전략**: 캐시 사용하지 않음
- **대안 전략**: Redis를 사용한 실시간 처리






# 4. 구현


## 패키지 구성 방식

캐시 도입 시 패키지를 구성하는 데 두 가지 방식을 고민했고 각각을 사용해보았다.

#### 1. 기존 패키지에 통합
첫 번째 방식은 기존의 도메인 패키지에 캐시 관련 클래스를 통합하는 것이다. 즉, 각 도메인 api 패키지 하위에 기존에 구성된 계층 구조에, 캐시 관련 컴포넌트들을 추가하는 것이다.

**장점:**
- 패키지를 별도로 추가하지 않아도 되어 코드 량이 늘어나는 부담이 적다.
- 캐시 로직 역시 api 하위 관리 대상이 되므로 실제 구현에서 자연스럽게 구현이 가능하다.

**단점:**
- 캐시 관련 코드가 여러 도메인 패키지에 분산되어 관리가 복잡해질 수 있다.
- 정책의 일관성을 유지하기 어려울 수 있다.

**패키지 구조 예시:**
```
io
└── reservationservice
    └── api
        └── application
            ├── aspect
            │   └── GlobalCacheAspect.java
            ├── eventlistener
            │   └── CacheEvictEventListener.java
            └── service
                ├── CacheService.java
                └── LocalCacheEvictionService.java
```

#### 2. 공통 패키지로 분리
두 번째 방식은 공통 패키지를 생성하고, 그 하위에 캐시 관련 클래스를 모아서 관리하는 것이다. 캐시 관련 기능을 별도의 공통 모듈로 분리하여 재사용성과 관리 용이성을 높인다.

**장점:**
- 캐시 관련 코드가 한 곳에 모여 있어 관리가 용이하다.
- 공통 모듈로 사용되므로 마이크로 서비스 간 일관된 코드를 유지할 수 있고, 바로 복사해서 붙여넣기가 가능하므로 구현 속도가 빨라진다.

**단점:**
- 특정 도메인에 특화된 캐시 전략을 적용할 때 커스터마이징이 필요할 수 있다.

**패키지 구조 예시:**
```
io
└── paymentservice
    ├── balance
    ├── payment
    └── common
        └── cache
            ├── application
            │   ├── aspect
            │   │   └── GlobalCacheAspect.java
            │   ├── eventlistener
            │   │   └── CacheEvictEventListener.java
            │   └── service
            │       ├── CacheService.java
            │       └── LocalCacheEvictionService.java
            ├── business
            │   ├── dto
            │   │   ├── command
            │   │   │   └── CacheCommand.java
            │   │   ├── event
            │   │   │   └── LocalCacheEvictEvent.java
            │   │   └── info
            │   │       └── CacheInfo.java
            │   └── service
            │       ├── CacheEvictionPropagationService.java
            │       └── RedisCacheService.java
            ├── infrastructure
            │   └── clients
            │       └── discovery
            │           └── DiscoveryInstanceClientAdapter.java
            └── interfaces
                └── CacheEviction.java

```
#### 3. 선택

본 프로젝트에서는 `Reservation` 서비스에서는 첫 번째 방식을 적용했고, `Payment` 서비스에서는 두 번째 방식을 적용했다.

향후 추가 구현이 필요할 경우 후자를 선호할 것이다.

## 로컬 캐시 구현

### 요약

- **라이브러리**: Ehcache
- **캐시 생성**: Spring의 `@Cacheable` 어노테이션 사용
- **캐시 동기화 (만료, Eviction)**:
  - 동기화 전략의 첫 번째로 TTL은 스프링의 도움으로 캐시 생성시 설정하는 것으로 가능하다.
  - 수동 Eviction 전략 구현 시엔 어노테이션 기반의 AOP 작동과 도메인 객체의 `AbstractAggregateRoot`를 통한 변경 이벤트 발행을 통해 동기화를 처리한다.
  - 이때, 도메인 엔티티로 JPA 엔티티로 사용하고 있으므로 JPA 엔티티 객체의 변경을 감지한다.
  - 변경 사항은 Listener로 컨슘하여 Eviction을 처리하되, 바로 evict 처리를 하는 것이 아니라 Propagation Service를 통해 서로 다른 인스턴스가 모두 처리할 수 있도록 eviction 요청을 셀프 호출하하여 전체 인스턴스 캐시의 동기화를 달성한다.

### Eviction 전략의 워크플로우

1. **도메인 엔티티 변경 감지**:
  - JPA 엔티티 객체의 변경 사항을 감지하여 도메인 이벤트를 발행.
2. **도메인 이벤트 리스너**:
  - 리스너는 발행된 이벤트를 비동기로 처리하여 캐시 Eviction 명령을 Propagation Service에 전달.
3. **Propagation Service**:
  - 캐시 Eviction 명령을 인스턴스별로 전파.
  - 각 인스턴스는 수신한 명령에 따라 로컬 캐시 Evict.

### 시퀀스 다이어그램

```mermaid
sequenceDiagram
    participant Entity
    participant EventListener
    participant PropagationService
    participant Instances

    Entity-->>+EventListener: 도메인 객체의 변경 이벤트 발행
    EventListener->>+PropagationService: propagateCacheEviction 호출
    PropagationService-->>+Instances: 로컬 캐시 Evict 요청
    Instances->>-Instances: Eviction complete
```


### 주요 코드

**Ehcache 설정** (`ehcache.xml`):

```xml
<config xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
        xmlns='http://www.ehcache.org/v3'
        xsi:schemaLocation="http://www.ehcache.org/v3 http://www.ehcache.org/schema/ehcache-core.xsd'>

    <cache alias="reservationStatus">
        <key-type>java.lang.Long</key-type>
        <value-type>io.reservationservice.api.business.dto.outport.ReservationStatusInfos</value-type>
        <resources>
            <heap unit="entries">1000</heap>
            <offheap unit="MB">10</offheap>
        </resources>
        <expiry>
            <ttl unit="minutes">60</ttl>
        </expiry>
    </cache>
</config>
```
**캐시 적용 서비스** (`ReservationService.java`):

```java
@Service
public class SimpleReservationCrudService implements ReservationCrudService {
	
  @Cacheable(key = "#userId" + "-" + "#concertOptionId", value= "reservationStatus")
  public ReservationStatusInfos getReservationStatus(Long userId, Long concertOptionId) {
    List<Reservation> reservations = reservationRepository.findMultipleByCondition(onMatchingReservation(userId, concertOptionId));

    List<TemporalReservation> temporalReservations = temporalReservationRepository.findMultipleByCondtion(onMatchingTemporalReservaion(userId, concertOptionId));

    return Stream.concat(reservations.stream().map(ReservationStatusInfo::from), temporalReservations.stream().map(ReservationStatusInfo::from))
            .collect(collectingAndThen(toList(), ReservationStatusInfos::new))
            .withValidated();
  }
}
```

**Eviction Aspect** (`LocalCacheEvictionAspect.java`):

```java
@Aspect
public class LocalCacheEvictionAspect {

    @AfterReturning(pointcut = "@annotation(evictLocalCache)", returning = "result")
    public void afterReturning(JoinPoint joinPoint, LocalCacheEvict evictLocalCache, Object result) {
        Arrays.stream(evictLocalCache.keys())
            .forEach(customKey -> propagationService.propagateCacheEviction(
                PropagateCacheEvictionCommand.of(evictLocalCache.cacheName(), parseKey(result, customKey))));
    }
}
```

**Eviction Propagation Service** (`CacheEvictionPropagationService.java`):

```java
@Service
public class CacheEvictionPropagationService {
    public void propagateCacheEviction(PropagateCacheEvictionCommand command) {
        String commandPath = String.format("/api/v1/cache/evict/%s/%s", command.getCacheName(), command.getKey());
        discoveryInstanceClientAdapter.getRequestToAllInstances(commandPath);
    }
}
```

**Eviction Listener** (`ReservationEventListener.java`):

```java
@Component
public class ReservationEventListener {

    @EventListener
    public void handleReservationChangedEvent(ReservationChangedEvent event) {
        evictReservationCache(event.getUserId(), event.getConcertOptionId());
    }

    private void evictReservationCache(Long userId, Long concertOptionId) {
            propagationService.propagateCacheEviction(
                PropagateCacheEvictionCommand.of("reservationStatus", userId + "-" + concertOptionId));
    }
}
```


## 글로벌 캐시 구현

### 요약

- **글로벌 캐시 저장소**: Redis
- **캐시 생성**: `GlobalCacheable` 어노테이션을 이용하여 Aspect를 구현. 복수개의 키와 TTL(Time To Live)을 받아 캐시 생성.
- **캐시 동기화 (만료, Eviction)**:
  - TTL은 `GlobalCacheable` 어노테이션을 통해 캐시 생성 시 설정합니다.
  - Eviction 전략 구현 시, 어노테이션 기반의 AOP 작동과 도메인 객체의 `AbstractAggregateRoot`를 통한 변경 이벤트 발행을 통해 동기화를 처리합니다.
  - 도메인 엔티티의 변경을 감지하여 Listener가 이를 컨슘하고, Redis 서비스를 호출하여 Eviction을 달성합니다.

### Eviction 전략의 워크플로우

1. **도메인 엔티티 변경 감지**:
- JPA 엔티티 객체의 변경 사항을 감지하여 도메인 이벤트를 발행합니다.
2. **도메인 이벤트 리스너**:
- 리스너는 발행된 이벤트를 비동기로 처리하여 캐시 Eviction 명령을 Redis 서비스에 전달합니다.
3. **Redis 서비스**:
- 캐시 Eviction 명령을 처리하여 Redis 캐시를 Evict 합니다.

### 주요 코드


**캐시 적용 서비스**

```
@Service
public class SimpleAvailabilityService implements AvailabilityService {

	@GlobalCacheable(prefix = "availableDates", keys = {"#concertId"}, ttl = 60 * 60 * 24)
	public AvailableDateInfos getAvailableDates(Long concertId) {
             // ...
	}
}
```



**캐시 어노테이션 정의** (`GlobalCacheable.java`):

```java
public @interface GlobalCacheable {
    String prefix() default "";
    String[] keys();
    long ttl() default 60L; 
}

public @interface GlobalCacheEvict {
    String prefix() default "";
    String[] keys();
}
```

**캐시 서비스** (`RedisCacheService.java`):

```java
@Service
public class RedisCacheService {
    public void cache(String cacheKey, Object value, long ttl) {
        redisClientAdapter.cache(cacheKey, value, ttl);
    }

    public Object get(String cacheKey) {
        return redisClientAdapter.getCache(cacheKey).orElse(null);
    }

    public void evict(String cacheKey) {
        redisClientAdapter.evictCache(cacheKey);
    }
}
```

**캐시 Aspect** (`GlobalCacheAspect.java`):

```java
@Aspect
public class GlobalCacheAspect {

    @Around("@annotation(globalCacheable)")
    public Object handleCache(ProceedingJoinPoint joinPoint, GlobalCacheable globalCacheable) throws Throwable {
       String cacheKey = resolveKey(joinPoint, globalCacheable);
		Object cachedValue = getCachedValue(cacheKey, ((MethodSignature) joinPoint.getSignature()).getReturnType());

         // 캐시가 존재한다면 캐시 반환
         // 존재하지 않는다면 메서드 진행, 캐싱 후 반환
    }
}
```

**Eviction Aspect** (`GlobalCacheEvictAspect.java`):

```java
@Aspect
public class GlobalCacheEvictAspect {

    @Before("@annotation(globalCacheEvict)")
    public void handleCacheEvict(JoinPoint joinPoint, GlobalCacheEvict globalCacheEvict) {
        String cacheKey = resolveKey(joinPoint, globalCacheEvict);
        cacheService.evict(cacheKey);
    }
}
```






# 5. Before & After

## 목표 

- 캐시가 사용된 경우와 사용되지 않은 경우 극단적인 비교를 통해 이상적인 캐시 사용시의 개선 확인 

** 캐시는 로컬 캐시로 사용 

## 테스트 환경 및 부하 설정  

- 시스템 : M1 맥북, CPU 8코어, 도커 Mysql
- 성능 테스트 도구: Gatling
- 극단적인 부하 환경 조성: 
  - 1,000건의 랜덤 파라미터 유저 설정 
  - 초당 1명에서 300명으로 1분 동안 증가, 초당 300명을 1분 동안 유지, 초당 300명에서 1명으로 1분 동안 감소
  - 최대 요청 -> 300개 요청/초
  - 총 36060개 요청 (1분) 




## Before: 캐시가 없는 경우 

```
================================================================================
---- Global Information --------------------------------------------------------
> request count                                      36060 (OK=27901  KO=8159  )
> min response time                                      0 (OK=0      KO=9     )
> max response time                                  60272 (OK=59981  KO=60272 )
> mean response time                                 11578 (OK=11493  KO=11870 )
> std deviation                                      14490 (OK=15858  KO=8239  )
> response time 50th percentile                       1780 (OK=25     KO=10727 )
> response time 75th percentile                      20093 (OK=32772  KO=20019 )
> response time 95th percentile                      36642 (OK=36742  KO=20139 )
> response time 99th percentile                      47265 (OK=47937  KO=20629 )
> mean requests/sec                                198.132 (OK=153.302 KO=44.83 )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                         16049 ( 45%)
> 800 ms <= t < 1200 ms                                218 (  1%)
> t >= 1200 ms                                       11634 ( 32%)
> failed                                              8159 ( 23%)
---- Errors --------------------------------------------------------------------
> i.n.c.ConnectTimeoutException: connection timed out after 1000   3438 (42.14%)
0 ms: localhost/[0:0:0:0:0:0:0:1]:24081
> j.n.SocketException: Connection reset by peer                    3386 (41.50%)
> j.i.IOException: Premature close                                 1263 (15.48%)
> Request timeout to localhost/127.0.0.1:24081 after 60000 ms        66 ( 0.81%)
> status.find.is(200), but actually found 500                         6 ( 0.07%)
================================================================================
```



![image](./documents/performance-test/cache/paymenthistory-withoutcache-gatling.png)
![image](./documents/performance-test/cache/paymenthistory-withoutcache-query.png)
![image](./documents/performance-test/cache/paymenthistory-withoutcache-cp.png)
![image](./documents/performance-test/cache/paymenthistory-withoutcache-request-duration.png)
![image](./documents/performance-test/cache/paymenthistory-withoutcache-cpu.png)
![image](./documents/performance-test/cache/paymenthistory-withoutcache-gc.png)



## After: 충분한 예열로 캐시 히트율이 100%인 경우 

```

================================================================================
---- Global Information --------------------------------------------------------
> request count                                       6060 (OK=6060   KO=0     )
> min response time                                      0 (OK=0      KO=-     )
> max response time                                    310 (OK=310    KO=-     )
> mean response time                                     3 (OK=3      KO=-     )
> std deviation                                          5 (OK=5      KO=-     )
> response time 50th percentile                          2 (OK=2      KO=-     )
> response time 75th percentile                          3 (OK=3      KO=-     )
> response time 95th percentile                          8 (OK=8      KO=-     )
> response time 99th percentile                         18 (OK=18     KO=-     )
> mean requests/sec                                 33.297 (OK=33.297 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          6060 (100%)
> 800 ms <= t < 1200 ms                                  0 (  0%)
> t >= 1200 ms                                           0 (  0%)
> failed                                                 0 (  0%)
================================================================================
```




![image](./documents/performance-test/cache/paymenthistory-cache-100-gatling.png)
![image](./documents/performance-test/cache/paymenthistory-cache-100-requestpersecond.png)
![image](./documents/performance-test/cache/paymenthistory-cache-100-cpu.png)
![image](./documents/performance-test/cache/paymenthistory-cache-100-g1-heap.png)



## 결과 

캐시 사용 전후의 성능 차이를 다음과 같이 확인할 수 있었다.

### 캐시 사용 전의 특징
- 실패율이 23%에 달했으며, 성공률은 77%에 그쳤다.
- 초당 300건의 부하를 감당하기 어려웠다.
- DB 커넥션 수는 100개를 풀 가동, 중간 중간 timeout 현상이 발생했다.
- Percona Monitoring에서는 평균 쿼리 시간이 7.73초로, 많은 요청으로 인한 지연이 확인됐다.
- 전반적인 과부하로 인해 `ConnectTimeoutException`, `SocketException`, `IOException` 등의 오류가 발생했다.
- Request Duration에서 밀림 현상이 발생하였다.

### 캐시 사용 후 (After)
- 실패율이 0%로, 모든 부하를 받아냈다.
- 단, 초당 500건의 요청이 발생했을 경우 간헐적으로 에러가 발생하였다.
- 캐시 히트율 100%이므로 당연하겠지만, 응답 시간이 극도로 빠르다. (평균 응답 시간 3ms)
- GC가 활발히 수행되었으며, 메모리 사용량도 많았지만 문제될 수준은 아니었다.





# c.f


## 캐싱 전략 배경 지식



1. Look-aside vs Write-back

  - **Look-aside 캐싱**: 이 전략에서는 애플리케이션이 먼저 캐시를 조회하고, 캐시에 데이터가 없으면 데이터베이스에서 데이터를 조회한 후 캐시에 저장한다. 이 방법은 읽기 성능을 향상시키지만, 데이터 일관성을 유지하는 것이 어렵다.
  - **Write-back 캐싱**: 이 전략에서는 데이터 변경 시 캐시에 먼저 쓰고, 이후 비동기적으로 데이터베이스에 쓰는 방식이다. 쓰기 성능은 향상되지만, 데이터 일관성 문제와 데이터 손실 위험이 존재한다.

2. 로컬 캐시 vs 글로벌 캐시

  - **로컬 캐시**: 각 서버 인스턴스가 자체 캐시를 가지는 방식이다. 네트워크 오버헤드 부담이 없어 글로벌 캐시보다도 빠르지만 데이터 일관성 문제를 해결하는 데 비용이 많이 들 수 있다. 
  - **글로벌 캐시**: Redis와 같은 분산 캐시를 사용하여 여러 서버 인스턴스가 동일한 캐시 데이터를 공유한다. 데이터 일관성 문제를 비교적 쉽게 해결하지만, 네트워크 오버헤드 비용 부담이 있다. 




## 모니터링 환경 구축


### 더미 데이터 생성

- 성능 병목 API 및 슬로우 쿼리를 식별하기 위해서는 대량의 더미 데이터가 필요하다. 가장 빠르게 대량의 데이터를 적재하기 위해 DB Procedure를 이용한다.
- 각 엔티티별로 1만에서 1천만 건의 더미 데이터를 생성한다.
- 데이터 설정은 실제 비즈니스 시나리오에 따라 적절하게 선정한다.

```sql
DELIMITER //
          
DROP PROCEDURE IF EXISTS GenerateConcertData;

CREATE PROCEDURE GenerateConcertData()
BEGIN
    DECLARE i INT DEFAULT 0;

    -- Concert 생성
    SET i = 0;
    WHILE i < 10000 DO
            INSERT INTO concert (title, created_at, request_at)
            VALUES (CONCAT('Concert ', i), NOW(), NOW());
            SET i = i + 1;
        END WHILE;

    COMMIT;
END //
DELIMITER ;

CALL GenerateConcertData();
```



### 성능 테스트

대량의 트래픽을 시뮬레이션하여 부하를 테스트한다. 이때 성능 테스트 도구로는 Gatling을 사용한다.

##### Gatling 선정 이유 (feat. Jmeter, nGrinder와의 비교)

Gatling은 높은 성능과 효율적인 자원 관리를 제공하여 대규모 부하 테스트에 적합하다.
코드 기반의 시나리오 작성이 가능하여 유지보수가 용이하며, Java로도 쉽게 테스트 시나리오를 작성할 수 있어 러닝 커브가 적고, 재사용성이 용이하다.

JMeter는 컴퓨터 자원을 많이 소비하며, 경험적으로 보았을 때 대용량 데이터 테스트 시 금방 뻗는 문제가 있다.
nGrinder는 분산 부하 테스트를 지원하지만, 설정과 관리가 복잡하며, 사용자 친화적인 인터페이스가 부족하여 초기 설정 시 어려움을 겪을 수 있다.

Gatling은 직관적인 UI와 실시간 보고서 생성 기능이 뛰어나, 테스트 결과를 쉽게 분석하고 시각화할 수 있다.

##### Gatling을 이용한 성능 테스트 환경 구성
- 시나리오 작성, 사용자 수 설정, 요청 패턴을 정의하여 실제 트래픽을 시뮬레이션 한다.

```java
public class PaymentHistorySimulation extends Simulation {
	
	private OpenInjectionStep[] getOpenInjectionSteps() {
		return new OpenInjectionStep[] {
			rampUsersPerSec(1).to(300).during(Duration.ofSeconds(30 *2)),
			constantUsersPerSec(300).during(Duration.ofSeconds(30*2)),
			rampUsersPerSec(300).to(1).during(Duration.ofSeconds(30*2))
		};
	}

	private ScenarioBuilder createScenario() {
		return scenario("Payment History Scenario")
			.exec(session -> {
				Map<String, Object> params = generateParams();
				return session.setAll(params);
			})
			.exec(http("결제 내역 조회")
				.get("/api/user-balance/payment/history/#{userId}")
				.check(status().is(200))
			)
			.pause(2);
	}
}
```



### 모니터링


#### 스프링 어플리케이션 모니터링 


스프링부트로 동작하는 어플리케이션의 성능을 모니터링하기 위해 Prometheus와 Grafana를 활용한 모니터링 환경을 구성한다. 
손쉽게 주요 지표를 실시간으로 확인하고, 성능을 최적화할 수 있다.


##### Prometheus 설정

`docker-compose-prometheus-grafana.yml` 파일을 작성하여 Prometheus와 Grafana를 도커에 띄운다.  

```yaml
#docker-compose-prometheus-grafana.yml
services:

  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus
    volumes:
      - ./prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
    ports:
      - "9090:9090"

  grafana:
    image: grafana/grafana:latest
    container_name: grafana
    environment:
      - GF_SECURITY_ADMIN_USER=admin
      - GF_SECURITY_ADMIN_PASSWORD=admin
    ports:
      - "3000:3000"
    depends_on:
      - prometheus
```

Prometheus 설정 파일인 `prometheus.yml`을 작성하여 어플리케이션 메트릭을 수집한다.

```yaml
## prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']

  - job_name: 'reservation-service'
    metrics_path: /reservation/actuator/prometheus
    static_configs:
      - targets: ['host.docker.internal:24071']
  
  // ... 
```

##### Grafana 대시보드 설정

Grafana를 통해 Prometheus에서 수집한 메트릭을 시각화한다. 
Grafana 대시보드 중 `Spring Boot 2.1 System Monitor`를 사용하여 스프링 어플리케이션의 다양한 지표를 확인할 수 있다.
CPU 사용량, 메모리 사용량, GC 활동, 쓰레드 정보, HTTP 요청 수 등을 확인할 수 있다.


![image](./documents/performance-test/tool/prometheus-grafana.png)





#### DB 모니터링 

실시간으로 쿼리 성능을 분석하기 위해 모니터링 환경을 구성한다.


Percona PMM(Percona Monitoring and Management)은 MySQL, MongoDB 등의 데이터베이스 성능을 모니터링하고 관리하는 도구이다. 
오픈소스로서 무료로 사용할 수 있고 실시간 쿼리 성능 및 시스템 성능을 분석할 수 있다.

PMM을 설정하려면, 먼저 도커 컴포즈 파일에서 `pmm-server` 서비스를 정의한다. 
이후 클라이언트 설정을 해주어야 한다. 
서버 컨테이너에 접속하여 다음 명령어를 실행한다.

```bash
pmm-admin config --server-insecure-tls --server-url=http://admin:admin@pmm-server:80
pmm-admin add mysql --query-source=perfschema --username=pmm --password=your_password --service-name=dev-mysql --host=dev-database --port=3306 --server-insecure-tls
pmm-admin add mysql --query-source=perfschema --username=pmm --password=your_password --service-name=stage-mysql --host=stage-database --port=3306 --server-insecure-tls
```

MySQL 데이터베이스에서 발생하는 지표 및 쿼리를 실시간으로 확인할 수 있다. 


![image](./documents/performance-test/tool/percona-mysql.png)
![image](./documents/performance-test/tool/percona-query.png)






</details>






<details>
<summary><b>대기열 시스템 리팩토링 (RDB -> Redis)</b></summary>



# 1. AS-IS (현재 프로세스)

현재 시스템은 RDB를 메인 데이터베이스로 사용하여 대기열 시스템을 관리한다.

### 1. 대기열 토큰 생성 및 인입

1. 유저가 대기열 토큰 생성 요청을 보낸다.
2. `대기열 비즈니스 로직`에서 요청을 받아 토큰을 생성하고 데이터베이스(RDB)에 저장한다.
3. 데이터베이스에 유저 ID와 상태값을 기준으로 중복 여부를 확인하고, 중복이 아닐 경우 새로운 대기열 토큰을 생성한다.
4. 대기열 토큰 카운터를 증가시킨다.
5. 토큰의 상태는 `WAITING`으로 설정된다.

### 2. 대기열 토큰 정보 조회

1. 유저가 특정 토큰의 정보를 조회하는 요청을 보낸다.
2. `대기열 비즈니스 로직`에서 데이터베이스(RDB)에서 해당 유저의 토큰 정보를 조회하고 반환한다.
3. 조회된 정보에서 누적 카운트와 autoincrement 값을 이용하여 대기열 순번을 계산한다.

### 3. 대기열 -> 처리열 이동

1. `토큰 스케줄링 로직`에서 일정 시간마다 토큰 이동 스케줄링을 수행한다.
2. 데이터베이스(RDB)에서 이동 가능한 토큰을 조회하여 처리열로 이동시키고, 상태를 `PROCESSING`으로 업데이트한다.
3. 대기열 토큰 카운터를 감소시킨다.

### 4. 만료 토큰 처리

1. `토큰 스케줄링 로직`에서 일정 시간마다 토큰 만료 스케줄링을 수행한다.
2. 데이터베이스(RDB)에서 만료된 토큰을 조회하여 상태를 `EXPIRED`로 업데이트한다.


### 5. 결제 완료 이벤트 수신

1. `카프카 이벤트`로 결제 완료 메시지를 수신한다.
2. 처리열 데이터베이스(RDB)에서 해당 유저 토큰을 찾아 `COMPLETE`로 업데이트 한다.

### 시퀀스 다이어그램

```mermaid
sequenceDiagram
    participant User as 유저
    participant BusinessLogic as 대기열 비즈니스 로직
    participant SchedulingLogic as 토큰 스케줄링 로직
    participant RDB as RDB
    
    rect rgb(173, 216, 230)
    Note right of User: 대기열 입장
    User->>BusinessLogic: 대기열 토큰 생성 요청
    BusinessLogic->>BusinessLogic: 요청 전달 및 토큰 생성
    BusinessLogic->>RDB: 토큰 중복 확인 및 생성 및 저장
    RDB-->>BusinessLogic: 토큰 생성 후 응답
    BusinessLogic-->>User: 토큰 상태는 'WAITING'
    end
    
    rect rgb(144, 238, 144)
    Note right of User: 대기열 순번 조회
    User->>BusinessLogic: 특정 토큰 정보 조회 요청
    BusinessLogic->>RDB: 토큰 정보 조회
    RDB-->>BusinessLogic: 토큰 정보 반환
    BusinessLogic-->>User: 토큰 정보 반환
    end
    
    rect rgb(255, 182, 193)
    Note left of SchedulingLogic: 토큰 이동 스케줄링
    loop 반복
        SchedulingLogic->>RDB: 이동 가능한 토큰 조회
        RDB-->>SchedulingLogic: 이동 가능한 토큰 반환
        SchedulingLogic->>RDB: 토큰 상태를 'PROCESSING'으로 업데이트
        SchedulingLogic-->>SchedulingLogic: 이동 완료
    end
    end
    
    rect rgb(255, 228, 181)
    Note left of SchedulingLogic: 토큰 만료 스케줄링
    loop 반복
        SchedulingLogic->>RDB: 만료된 토큰 조회
        RDB-->>SchedulingLogic: 만료된 토큰 반환
        SchedulingLogic->>RDB: 토큰 상태를 'EXPIRED'로 업데이트
        SchedulingLogic-->>SchedulingLogic: 만료 처리 완료
    end
    end
```


# 2. 쟁점 사항


## 1. 현재 활성 가능한 토큰의 개수를 어떻게 추적할 것인가?

활성 가능한 토큰의 개수를 추적해야 한다. 기존의 방식은 RDB에서 인덱스를 활용하여 row 수를 세는 것이었다.

Redis를 이용할 때 다음과 같은 두 가지 방법을 고려해볼 수 있다.

#### 1. 처리열을 Set 구조로 구성

- 이 방법은 활성 토큰을 Set에 저장하여 관리하는 것이다.
- Key는 각 토큰이며, Member에는 유저 정보와 만료 일시 등의 메타정보를 포함한다.
- `SMEMBERS` 명령을 사용하여 Tokens Set과 메타정보를 조회할 수 있다.
- 장점: Set의 크기를 통해 활성화된 토큰 수를 쉽게 파악할 수 있다.
- 단점: 각 토큰에 대해 만료 시간을 설정할 수 없다. 만료된 토큰을 삭제하기 위해 별도의 스케줄링 로직이 필요하다.


#### 2. 처리열을 별도의 자료 구조 없이, 활성 토큰을 Key-Value 구조로 저장

- 이 방법은 토큰을 단순히 Key-Value 형태로 저장한다.
- Key는 토큰 ID이며, Value는 유저 정보와 만료 일시 등의 메타정보를 포함한다.
- 각 토큰은 Redis의 `SET` 명령을 통해 저장되며, 만료 시간(TTL)은 `EXPIRE` 명령을 통해 설정된다.
- `GET` 명령을 사용하여 특정 토큰의 정보를 조회할 수 있다.
- 장점: 구조가 단순하여 구현이 쉽고, 각 토큰의 TTL을 설정할 수 있어 자동 만료 관리가 가능하다.
- 단점: 현재 활성화된 토큰 수를 알 수 없다. 따라서 별도의 카운팅 로직이 필요하다.


두 가지 방법은 장단점이 있어 보인다. Key-Value 구조는 단순하지만 카운팅이 어렵고, Set 구조는 관리가 용이하지만 만료 처리에 추가 로직이 필요하다.



#### 3. 결정

**Key-Value 구조**를 사용하여 별도의 카운터로 활성 토큰 수를 추적하는 방안을 선택한다.

- **Redis Expiration 기능 활용:** 이 방법은 각 토큰에 만료 시간을 설정하여 자동으로 만료되게 할 수 있다. Redis가 자체적으로 만료된 키를 삭제하므로, 애플리케이션의 스케줄링 부담이 줄어든다.
- **카운터 관리:** 활성 토큰 수를 별도의 카운터로 관리함으로써, 카운터만 잘 동기화하면 현재 활성화된 토큰 수를 정확하게 추적할 수 있다. 관리 부담이 하나 더 는다고 볼 수도 있지만, 한편으론 관리 포인트를 하나로 집중시킬 수 있어 분산된 책임으로 작업이 가능하다.

결론적으로, Key-Value 구조와 별도의 카운터를 사용하는 방식은 시스템의 단순성과 효율성을 동시에 달성할 수 있는 최적의 선택이다.



## 2. 대기열 조회시 대기열에는 없고 처리열에는 있는 경우 어떻게 처리할 것인가?

유저가 대기열 조회 요청에서, 이미 대기열을 벗어나 처리열로 이동했을 경우 어떤 응답을 내릴지 결정해야 한다. 

에러 응답 후 처리열 재요청 방식과 서버에서 애초에 처리열까지 조회 범위를 산정하여 응답하는 방식 두 가지를 고려했다. 

비즈니스 유스케이스를 고려할 때, 두 번째 방식인 **서버에서 대기열과 처리열을 통합하여 조회하는 방식**을 채택했다. 사용자 경험과 응답 일관성 측면에서 좋다고 생각했다. 

따라서, 서버는 대기열 조회를 먼저 수행하고, 해당 토큰이 대기열에 존재하지 않을 경우 처리열을 조회하여 응답한다. 
이 때 분기 처리는 `facade`에서 처리하여, 대기열과 처리열 서비스의 응답을 조합하여 응답한다.


#### 구현 예시

```java
/**
 * 먼저 대기열 조회 후 대기열에 존재하지 않는 경우 처리열 조회
 * 반환 값은 통합된 정보로 반환
 */
public WaitingQueueTokenGeneralResponse retrieveToken(String userId) {
    WaitingQueueTokenGeneralInfo waitingToken = waitingQueueService.retrieveToken(userId);

    if (waitingToken.isEmpty()) {
        return WaitingQueueTokenGeneralResponse.from(processingQueueService.retrieve(userId))
                                               .withUserId(userId);
    }

    return WaitingQueueTokenGeneralResponse.from(waitingToken)
                                           .withUserId(userId);
}
```

1. `waitingQueueService.retrieveToken(userId)`를 호출하여 대기열에서 해당 유저의 토큰을 조회한다.
2. 조회된 토큰이 대기열에 없는 경우, `processingQueueService.retrieve(userId)`를 호출하여 처리열에서 토큰을 조회한다.
3. 최종적으로 대기열 또는 처리열에서 조회된 정보를 `WaitingQueueTokenGeneralResponse` 객체로 반환한다.


## 3.  userId 기반 토큰 생성 및 조회 방법

기존 방식에서는 `tokenvalue`를 UUID로 생성했다. 이때 속성 필드로 `userId`를 두어 인덱스를 생성했다. 즉, 유저에게 토큰을 전달해야 할 때, `userId`를 파라미터로 받아서 해당 토큰을 찾아 리턴할 수 있었다. 그러나 Redis로 토큰을 관리할 때는 `userId`로 검색이 불가능하다. Redis에서는 `sortedset`의 키로 토큰 값을 사용하기 때문이다. 이 경우, `userId`로 조회 요청을 받을 때 어떻게 응답을 제공할 수 있을까?

즉, 고유한 토큰을 생성하면서도, 사용자가 `userId` 기반으로 토큰 조회를 요청할 때 찾아서 리턴해야 한다.

### 해결 방안 1: 토큰 값을 `userId`로 설정하기

key 값을 `userId`로 설정하여 사용하는 방법이 있다. 그러나 이 방식은 보안 취약성 면에서 좋지 않다.

`userId`를 토큰 값으로 사용하는 경우, 토큰 자체의 의미를 갖기 힘들다. 누구나 `userId`를 알고 있다면 해당 유저의 토큰에 쉽게 접근할 수 있다.


### 해결 방안 2: 해시 함수로 토큰 생성하기

이 문제를 해결하기 위해 해시 함수를 사용할 수 있다. 해시 함수의 특징은 입력 값에 대해 항상 동일한 해시 값을 생성하는 멱등성을 가지며, 암호화의 성격도 포함하고 있다. `userId`가 고유하다면, 해시 함수를 사용하여 고유한 정해진 길이의 토큰가 생성되는 것은 보장된다. 따라서 `userId` 기반으로 토큰을 조회할 때 해당 해시 값을 사용하여 Redis에서 토큰 키 값을 조회하고 리턴할 수 있다.

### 해시 함수 구현 예시

아래는 `QueueTokenGenerator` 클래스의 해시 함수 구현 예시이다.

```java
public class QueueTokenGenerator {
  private static final String SECRET_KEY = "secret-key";

  public static String generateToken(String userId) {
    try {
      String input = userId + SECRET_KEY;
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error generating token", e);
    }
  }
}
```

다음과 같은 장점이 있다.

1. **보안 강화**: `userId`와 `SECRET_KEY`를 결합하여 해시 값을 생성하므로, 단순히 `userId`를 아는 것으로는 토큰 값을 예측할 수 없다.
2. **고유성 보장**: `userId`가 고유하다면, 해시 함수에 의해 생성되는 토큰 값도 고유하다.
3. **조회 가능성**: `userId`를 입력으로 해시 함수를 호출하면 동일한 토큰 값을 생성할 수 있으므로, 해당 토큰 값을 키로 사용하여 Redis에서 조회가 가능하다.


# 3. TO-BE (Redis로 리팩토링한 프로세스)

### 컴포넌트 변경 사항

1. **대기열 관리**: 기존 RDB -> Redis의 Sorted Set
2. **처리열 관리**: 기존 RDB -> Redis의 Key-Value 구조
3. **토큰 상태 관리**: 기존 스케줄링 -> 레디스 TTL 설정으로 자동 만료
4. **카운터 관리**: 기존 RDB -> 대기열에서 불필요, 처리 중인 토큰 추적시 Redis로 카운팅
5. **순번 계산**: 기존 누적 카운터에 근거한 포지션 필드 + AI 계산 -> 레디스 Sorted Set의 Rank

### 프로세스 변경 사항

- 기존 토큰 서비스를 대기열 서비스와 처리열 서비스로 분리.
- 대기열 서비스는 대기열 인입 요청만 담당하며, 요청을 모두 Redis 대기열(Sorted Set)에 저장.
- 처리열 서비스는 주기적으로 Redis의 처리열(Key-Value)과 대기열(Sorted Set)을 폴링하여 서버 가용량을 확인하고, 이동 가능한 토큰을 대기열(Sorted Set)에서 가져옴.
- 가져온 토큰들을 Kafka 이벤트로 발행하여, 여러 인스턴스가 동시에 작업할 수 있도록 self로 컨슘하여 인스턴스 별로 이관 작업을 수행.
- 처리열 서비스는 토큰을 Redis의 처리열(Key-Value)에 인입하고, 대기열(Sorted Set)에서 제거.
- 결제 완료 이벤트를 수신하여 처리열에서 토큰을 제거.
- 처리열 인입과 제거시 Redis 카운터를 사용하여 활성 유저수 추적, 서버의 가용량과 현재 가용량을 비교시 사용.
- 유저의 요청에 대한 응답으로, 폴링 서비스는 기존과 동일하게 대기열 서비스를 거쳐 대기열 정보를 조회하여 응답.


### 시퀀스 다이어그램

```mermaid
sequenceDiagram
    participant User as 유저
    participant QueueService as 대기열 서비스
    participant ProcessingService as 처리열 서비스
    participant PollingService as 폴링 서비스
    participant WaitingQueue as 대기열 (Redis Sorted Set)
    participant ProcessingQueue as 처리열 (Redis Key-Value)
    participant TokenCounter as 토큰 카운터 (Redis)
    participant Kafka as Kafka

    rect rgb(173, 216, 230)
    Note right of User: 대기열 토큰 생성 요청
    User-->>QueueService: 대기열 토큰 생성 요청
    QueueService-->>WaitingQueue: 대기열에 토큰 인입
    end
    
    rect rgb(255, 182, 193)
    Note left of ProcessingService: 처리열로 이동 스케줄링
    loop 주기적 폴링
        ProcessingService->>TokenCounter: 처리열 카운트 폴링
        TokenCounter-->>ProcessingService: 현재 처리열 크기 반환
        ProcessingService->>WaitingQueue: 가용량 계산 및 대기열에서 가용 토큰 fetch
        WaitingQueue-->>ProcessingService: 가용 토큰 반환
        ProcessingService-->>Kafka: 토큰 정보를 Kafka로 발행 (개별 건)
    end
    end
    
    rect rgb(255, 228, 181)
    Note left of ProcessingService: 토큰 이관 이벤트 처리
    loop 이벤트 수신 및 처리
        Kafka-->>ProcessingService: 토큰 이관 이벤트 수신
        ProcessingService->>ProcessingQueue: 처리열에 토큰 인입 및 대기열에서 제거 (원자성 필요)
        ProcessingService->>TokenCounter: 활성 토큰 수 증가
    end
    end
    
    rect rgb(255, 228, 181)
    Note left of ProcessingService: 결제 완료 이벤트 처리
    loop 결제 완료 이벤트 수신
        Kafka-->>ProcessingService: 결제 완료 이벤트 수신
        ProcessingService->>ProcessingQueue: 처리열에서 토큰 제거
        ProcessingService->>TokenCounter: 활성 토큰 수 감소
    end
    end
    
    rect rgb(144, 238, 144)
    Note right of User: 유저의 순번 조회 요청
    User->>PollingService: 대기열 순번 조회
    PollingService->>QueueService: 대기열 순번 요청
    QueueService->>WaitingQueue: 대기열/처리열 조회
    QueueService->>ProcessingQueue: 
    ProcessingQueue-->>QueueService: 순번 정보 반환
    WaitingQueue-->>QueueService:     
    QueueService-->>PollingService: 순번 정보 응답
    PollingService-->>User: 순번 정보 응답
    end
```

### 설명

1. **대기열 서비스**:
  - 유저의 대기열 토큰 생성 요청을 받아 Redis의 Sorted Set(대기열)에 저장한다.

2. **처리열 서비스**:
  - 주기적으로 Redis의 처리열(Key-Value)과 대기열(Sorted Set)을 폴링하여 서버의 가용 유저 수를 확인한다.
  - 처리 가능한 유저 수만큼 대기열(Sorted Set)에서 토큰을 가져와 Kafka 이벤트로 발행한다.
  - Kafka 이벤트를 self로 구독하여 각 인스턴스가 동시에 작업을 수행한다.
  - 토큰을 처리열(Key-Value)에 인입하고 대기열(Sorted Set)에서 제거한다.
  - 토큰 인입 시 Redis 카운터를 사용하여 활성화된 토큰 수를 증가시킨다.
  - 결제 완료 이벤트를 수신하여 처리열(Key-Value)에서 해당 토큰을 제거하고, Redis 카운터에서 활성화된 토큰 수를 감소시킨다.

3. **유저 요청 응답**:
  - 유저의 대기열 순번 조회 요청에 대해 폴링 서비스는 기존과 동일하게 대기열 서비스를 거쳐 Redis의 대기열과 처리열을 조회하여 응답한다.



# 4. 성능 테스트 

## 목적 

- RDB와 Redis의 성능 비교 분석 
- 각 방식의 부하 최대치를 탐구


## 테스트 환경 및 부하 설정

### 시스템 및 도구

- 시스템: M1 맥북, CPU 8코어, 도커 Mysql
- 성능 테스트 도구: Gatling

### 부하 환경 조성

- 유니크한 랜덤 유저의 대기열 인입 요청(토큰 생성 요청)
- 부하 시나리오:
  - 초당 1명에서 300명으로 1분 동안 증가
  - 초당 300명을 1분 동안 유지
  - 초당 300명에서 1명으로 1분 동안 감소

### 설정 세부사항

- 처리열 최대 크기: 1000
- 처리열 만료 시간: 30초 (테스트 용이성을 위해 빠른 전환율 설정)
- 테스트 실행 시간: 최대 1분 
- RDB 커넥션풀 설정: 50개


## 토큰 인입 요청 시나리오

이 성능 테스트 시나리오는 `WaitingQueueTokenController`의 `/api/waiting-queue-token` 엔드포인트를 사용하여 대기열 토큰을 생성하고 인입하는 작업을 수행한다. 

Gatling을 사용하여 대량의 토큰 생성 요청을 시뮬레이션한다.

부하 환경은 다음과 같은 파라미터들을 통해 조정한다.  

1. 초당 1명에서 300명으로 1분 동안 증가
2. 초당 300명을 1분 동안 유지
3. 초당 300명에서 1명으로 1분 동안 감소



```java
public class WaitingQueueTokenGenerationSimulation extends Simulation {
	
	// ... 생략
	
	{
		setUp(createScenario()
			.injectOpen(getOpenInjectionSteps())
			.protocols(httpProtocolBuilder))
			.maxDuration(Duration.ofMinutes(1))
			.assertions(global().requestsPerSec().gte(1.0));
	}

	private OpenInjectionStep[] getOpenInjectionSteps() {
		return new OpenInjectionStep[]{
			rampUsersPerSec(100).to(1000).during(Duration.ofSeconds(20)),
			constantUsersPerSec(1000).during(Duration.ofSeconds(20)),
			rampUsersPerSec(1000).to(1).during(Duration.ofSeconds(20))
		};
	}

	private ScenarioBuilder createScenario() {
		return scenario("Waiting Queue Token Generation Scenario")
			.asLongAs(session -> userIdCounter.get() <= MAX_USER_ID)
			.on(exec(session -> {
					int userId = userIdCounter.getAndIncrement();
					return session.set("userId", "user" + userId);
				})
					.exec(http("대기열 토큰 생성")
						.post("/api/waiting-queue-token")
						.body(StringBody(session -> {
							LocalDateTime now = LocalDateTime.now();
							return String.format("{ \"userId\": \"%s\", \"requestAt\": \"%s\" }", session.getString("userId"), now.toString());
						}))
						.check(status().is(201))
					)
					.pause(2)
			);
	}
}
```




## Redis 구현의 성능 한계치 


먼저 아래와 같은 고부하 상황으로 1차 테스트를 진행한다.  

```
- 초당 100명의 유저로 시작하여 20초 동안 초당 1000명까지 증가
- 초당 1000명의 유저를 20초 동안 유지
- 초당 1000명의 유저에서 20초 동안 1명으로 감소
```

다음과 같은 상당히 높은 오류율이 확인된다.


```

================================================================================
---- Global Information --------------------------------------------------------
> request count                                     294923 (OK=43740  KO=251183)
> min response time                                      0 (OK=2      KO=0     )
> max response time                                  28726 (OK=28726  KO=23722 )
> mean response time                                  1167 (OK=4610   KO=567   )
> std deviation                                       3885 (OK=7981   KO=2050  )
> response time 50th percentile                          5 (OK=1710   KO=3     )
> response time 75th percentile                         90 (OK=3460   KO=33    )
> response time 95th percentile                       5937 (OK=25413  KO=4129  )
> response time 99th percentile                      25176 (OK=26820  KO=11772 )
> mean requests/sec                                4915.383 (OK=729    KO=4186.383)
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                         20108 (  7%)
> 800 ms <= t < 1200 ms                               1160 (  0%)
> t >= 1200 ms                                       22472 (  8%)
> failed                                            251183 ( 85%)
---- Errors --------------------------------------------------------------------
> j.n.SocketException: Too many open files                       212681 (84.67%)
> j.i.IOException: Premature close                                15589 ( 6.21%)
> j.n.BindException: Can't assign requested address               14510 ( 5.78%)
> status.find.is(201), but actually found 500                      4293 ( 1.71%)
> i.n.c.ConnectTimeoutException: connection timed out after 1000   2210 ( 0.88%)
0 ms: localhost/127.0.0.1:24031
> j.n.SocketException: Connection reset by peer                    1829 ( 0.73%)
> i.n.c.ConnectTimeoutException: connection timed out after 1000     69 ( 0.03%)
0 ms: localhost/[0:0:0:0:0:0:0:1]:24031
> j.n.ConnectException: Connection refused                            2 ( 0.00%)
================================================================================
```

초당 요청 약 5,000건으로 상당히 높은 부하이다.

대기열 sortedSet에 약 52,000건의 토큰이 생성되었지만, 대부분의 요청은 실패한 것을 볼 수 있다.


![redis-max-1.png](documents%2Fperformance-test%2Ftoken%2Fredis%2Fredis-max-1.png)



부하 수치를 낮춰서 다시 시도해 보자.  



```
- 초당 100명의 사용자가 500명까지 20초 동안 증가
- 500명을 20초 동안 유지
- 500명에서 1명까지 20초 동안 감
```


```
================================================================================
---- Global Information --------------------------------------------------------
> request count                                     124882 (OK=44811  KO=80071 )
> min response time                                      0 (OK=2      KO=0     )
> max response time                                  23132 (OK=23132  KO=21946 )
> mean response time                                  2574 (OK=3655   KO=1968  )
> std deviation                                       4757 (OK=6427   KO=3340  )
> response time 50th percentile                        548 (OK=618    KO=513   )
> response time 75th percentile                       3155 (OK=3832   KO=1887  )
> response time 95th percentile                      12357 (OK=21990  KO=10481 )
> response time 99th percentile                      22107 (OK=22405  KO=13422 )
> mean requests/sec                                2047.246 (OK=734.607 KO=1312.639)
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                         23168 ( 19%)
> 800 ms <= t < 1200 ms                               2114 (  2%)
> t >= 1200 ms                                       19529 ( 16%)
> failed                                             80071 ( 64%)
---- Errors --------------------------------------------------------------------
> j.n.SocketException: Too many open files                        60781 (75.91%)
> j.i.IOException: Premature close                                14638 (18.28%)
> status.find.is(201), but actually found 500                      4310 ( 5.38%)
> j.n.SocketException: Connection reset by peer                     258 ( 0.32%)
> i.n.c.ConnectTimeoutException: connection timed out after 1000     84 ( 0.10%)
0 ms: localhost/[0:0:0:0:0:0:0:1]:24031
================================================================================

```

초당 요청은 2,000건으로, 처음보다 낮은 부하이지만 여전히 큰 부하로 보인다.


다시 한 번 부하를 낮춰보자.  

```
- 초당 100명의 사용자가 200명까지 20초 동안 증가
- 200명을 20초 동안 유지
- 200명에서 1명까지 20초 동안 감소
```

대기열 카운트 45000 건까지 올라가는 것이 확인되었지만, 여전히 에러는 발생한다. 


```
================================================================================
---- Global Information --------------------------------------------------------
> request count                                      62841 (OK=44294  KO=18547 )
> min response time                                      1 (OK=1      KO=7     )
> max response time                                  22740 (OK=17232  KO=22740 )
> mean response time                                  2376 (OK=1258   KO=5047  )
> std deviation                                       3285 (OK=2288   KO=3733  )
> response time 50th percentile                        617 (OK=150    KO=3867  )
> response time 75th percentile                       3705 (OK=1205   KO=6665  )
> response time 95th percentile                       7201 (OK=6853   KO=14635 )
> response time 99th percentile                      14661 (OK=10595  KO=15427 )
> mean requests/sec                                1047.35 (OK=738.233 KO=309.117)
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                         31800 ( 51%)
> 800 ms <= t < 1200 ms                               1404 (  2%)
> t >= 1200 ms                                       11090 ( 18%)
> failed                                             18547 ( 30%)
---- Errors --------------------------------------------------------------------
> j.i.IOException: Premature close                                11037 (59.51%)
> status.find.is(201), but actually found 500                      7212 (38.88%)
> i.n.c.ConnectTimeoutException: connection timed out after 1000    297 ( 1.60%)
0 ms: localhost/[0:0:0:0:0:0:0:1]:24031
> j.n.SocketException: Connection reset by peer                       1 ( 0.01%)
================================================================================
```

초당 1,000건의 요청을 수용하기 힘들다. 좀 더 낮출 필요가 있다. 


```
- 초당 10명의 사용자가 50명까지 20초 동안 증가
- 50명을 20초 동안 유지
- 50명에서 1명까지 20초 동안 감소
```

결과는 어떨까? 

```
================================================================================
---- Global Information --------------------------------------------------------
> request count                                      32573 (OK=32573  KO=0     )
> min response time                                      1 (OK=1      KO=-     )
> max response time                                   1144 (OK=1144   KO=-     )
> mean response time                                    84 (OK=84     KO=-     )
> std deviation                                        179 (OK=179    KO=-     )
> response time 50th percentile                         19 (OK=19     KO=-     )
> response time 75th percentile                         64 (OK=64     KO=-     )
> response time 95th percentile                        510 (OK=510    KO=-     )
> response time 99th percentile                        921 (OK=921    KO=-     )
> mean requests/sec                                542.883 (OK=542.883 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                         31774 ( 98%)
> 800 ms <= t < 1200 ms                                799 (  2%)
> t >= 1200 ms                                           0 (  0%)
> failed                                                 0 (  0%)
================================================================================
```


드디어 에러 없이 성공했다 ! 

초당 500 건의 요청은 수용할 수 있음을 확인했다.

그렇다면 여기서 좀 더 올리면 어떨까? 


```
- 초당 10명의 사용자가 100명까지 20초 동안 증가
- 100명을 20초 동안 유지
- 100명에서 1명까지 20초 동안
```

```

================================================================================
---- Global Information --------------------------------------------------------
> request count                                      48336 (OK=44296  KO=4040  )
> min response time                                      1 (OK=1      KO=4     )
> max response time                                  13179 (OK=13179  KO=11299 )
> mean response time                                   426 (OK=367    KO=1070  )
> std deviation                                        962 (OK=920    KO=1155  )
> response time 50th percentile                        109 (OK=81     KO=1036  )
> response time 75th percentile                        535 (OK=400    KO=1366  )
> response time 95th percentile                       1505 (OK=1425   KO=1842  )
> response time 99th percentile                       1949 (OK=1844   KO=8551  )
> mean requests/sec                                  805.6 (OK=738.267 KO=67.333)
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                         37729 ( 78%)
> 800 ms <= t < 1200 ms                               2776 (  6%)
> t >= 1200 ms                                        3791 (  8%)
> failed                                              4040 (  8%)
---- Errors --------------------------------------------------------------------
> status.find.is(201), but actually found 500                      3909 (96.76%)
> j.n.SocketException: Connection reset by peer                      82 ( 2.03%)
> j.i.IOException: Premature close                                   48 ( 1.19%)
> i.n.c.ConnectTimeoutException: connection timed out after 1000      1 ( 0.02%)
0 ms: localhost/[0:0:0:0:0:0:0:1]:24031
================================================================================
```


이번엔 실패한다! 

초당 500건까지는 처리 가능했으나 초당 800건은 불가하다는 결론을 얻었다.
따라서 500~800 건 사이에서 최대 수용치가 있음을 알 수 있다.


#### 결론 및 요약  


![redis-max-gatling.png](documents%2Fperformance-test%2Ftoken%2Fredis%2Fredis-max-gatling.png)


유의미한 포인트를 추려보면 다음과 같다. 

- 95번째 백분위수에서 응답 시간이 510ms로, 대부분의 요청이 800ms 이하에서 처리되었다.
- 99번째 백분위수에서도 응답 시간이 921ms로, 여전히 1,000ms 이내에서 대부분의 요청이 처리되었다.
- 응답 시간이 800ms에서 1,200ms 사이에 위치한 요청이 2% 정도 존재하여, 높은 부하 상황에서도 비교적 안정적인 응답 시간을 유지하고 있음을 보여준다.


응답 시간의 분포는 대부분 800ms 이하로 나타났다. 
최대 응답 시간은 1,144ms였고, 95번째 백분위수는 510ms로 안정적인 수치로 보인다. 
다만, 응답 시간이 800ms에서 1,200ms 사이에 위치한 요청이 2% 정도 존재하여, 시스템의 개선 여지가 있음을 시사한다.

따라서 Redis 기반의 대기열은 한 인스턴스에서 초당 분당 약 30,000건 가량의 토큰 인입 요청을 수용할 수 있음으로 결론 내릴 수 있다.



## RDB 구현의 성능 한계치 


우선 작은 수치부터 시작하여 테스트를 진행했다. 

```
- 초당 10명의 사용자가 20초 동안 유지
- 초당 10명의 사용자를 20초 동안 유지
- 초당 10명의 사용자를 20초 동안 1명으로 감소
```



```

================================================================================
---- Global Information --------------------------------------------------------
> request count                                       7115 (OK=7115   KO=0     )
> min response time                                      4 (OK=4      KO=-     )
> max response time                                   3250 (OK=3250   KO=-     )
> mean response time                                   514 (OK=514    KO=-     )
> std deviation                                        626 (OK=626    KO=-     )
> response time 50th percentile                        278 (OK=278    KO=-     )
> response time 75th percentile                        728 (OK=728    KO=-     )
> response time 95th percentile                       2128 (OK=2128   KO=-     )
> response time 99th percentile                       2519 (OK=2519   KO=-     )
> mean requests/sec                                118.583 (OK=118.583 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          5457 ( 77%)
> 800 ms <= t < 1200 ms                                684 ( 10%)
> t >= 1200 ms                                         974 ( 14%)
> failed                                                 0 (  0%)
================================================================================
```


요청이 많지 않음에도 불구하고 상당히 느린 응답 속도를 보인다. 저장된 토큰 카운트도 7,187건으로 매우 작다.


![rdb-max-count1.png](documents%2Fperformance-test%2Ftoken%2Frdb%2Frdb-max-count1.png)


두 번째 테스트에서는 수치를 좀 더 올려보자 


```
- 초당 50명의 사용자가 200명까지 20초 동안 증가
- 초당 200명의 사용자를 20초 동안 유지
- 초당 200명의 사용자를 20초 동안 1명으로 감소
```

테스트 결과는 다음과 같다.

```

================================================================================
---- Global Information --------------------------------------------------------
> request count                                      13097 (OK=10000  KO=3097  )
> min response time                                      1 (OK=4      KO=1     )
> max response time                                  22178 (OK=18351  KO=22178 )
> mean response time                                 11129 (OK=8853   KO=18479 )
> std deviation                                       7297 (OK=6736   KO=2814  )
> response time 50th percentile                      13581 (OK=7011   KO=18658 )
> response time 75th percentile                      17723 (OK=16009  KO=19386 )
> response time 95th percentile                      19700 (OK=17746  KO=21055 )
> response time 99th percentile                      21169 (OK=17817  KO=21715 )
> mean requests/sec                                218.283 (OK=166.667 KO=51.617)
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          1306 ( 10%)
> 800 ms <= t < 1200 ms                                461 (  4%)
> t >= 1200 ms                                        8233 ( 63%)
> failed                                              3097 ( 24%)
---- Errors --------------------------------------------------------------------
> status.find.is(201), but actually found 400                      3030 (97.84%)
> j.i.IOException: Premature close                                   65 ( 2.10%)
> status.find.is(201), but actually found 500                         2 ( 0.06%)
================================================================================
```


벌써 상당한 오류가 확인된다. 


다시 부하를 좀 더 줄여보자. 



```
- 초당 50명의 사용자가 150명까지 20초 동안 증가
- 초당 150명의 사용자를 20초 동안 유지
- 초당 150명의 사용자를 20초 동안 1명으로 감소
```

이전보다는 오류가 줄었지만 여전히 많은 오류와 지연이 발생하고 있다.


```
================================================================================
---- Global Information --------------------------------------------------------
> request count                                      12593 (OK=10000  KO=2593  )
> min response time                                      4 (OK=4      KO=103   )
> max response time                                  20589 (OK=14368  KO=20589 )
> mean response time                                  8817 (OK=7060   KO=15595 )
> std deviation                                       5369 (OK=4552   KO=1491  )
> response time 50th percentile                       9826 (OK=7423   KO=15280 )
> response time 75th percentile                      13426 (OK=10787  KO=16508 )
> response time 95th percentile                      16550 (OK=13496  KO=18180 )
> response time 99th percentile                      18340 (OK=13984  KO=19679 )
> mean requests/sec                                209.883 (OK=166.667 KO=43.217)
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          1329 ( 11%)
> 800 ms <= t < 1200 ms                                499 (  4%)
> t >= 1200 ms                                        8172 ( 65%)
> failed                                              2593 ( 21%)
---- Errors --------------------------------------------------------------------
> status.find.is(201), but actually found 400                      2590 (99.88%)
> status.find.is(201), but actually found 500                         3 ( 0.12%)
================================================================================
```

커넥션 풀도 확인해 보면 50개의 풀을 모두 사용하고 150개의 pending이 발생함을 확인할 수 있다.


![rdb-cp.png](documents%2Fperformance-test%2Ftoken%2Frdb%2Frdb-cp.png)


쿼리 수행 시간에서도 상당한 지연이 발생하고 있다. 


![rdb-query.png](documents%2Fperformance-test%2Ftoken%2Frdb%2Frdb-query.png)


다음과 같은 쿼리가 락 타임을 발생시키고 있음을 확인할 수 있다.

```sql
SELECT
  `wqtce1_0`.`waiting_queue_token_counter_id`,
  `wqtce1_0`.`count`
FROM
  `waiting_queue_token_counter_entity` `wqtce1_0`
WHERE
  `wqtce1_0`.`waiting_queue_token_counter_id` = ? FOR
UPDATE
```

락 타임이 길게 잡히는 것이 문제의 원인임을 확인할 수 있다.

```
락 타임 통계:

- 평균 로드 시간: 10.86 초
- 총 로드 시간: 54:19
- 총 쿼리 시간의 99.99%가 락 타임에 사용됨
- 총 45.90 초의 락 타임
```


![rdb-slow-query.png](documents%2Fperformance-test%2Ftoken%2Frdb%2Frdb-slow-query.png)




#### 결론 및 요약



![rdb-max-gatling.png](documents%2Fperformance-test%2Ftoken%2Frdb%2Frdb-max-gatling.png)



유의미한 포인트를 추려보면 다음과 같다.

- 95번째 백분위수에서 응답 시간이 2,128ms로, 상당히 긴 지연 시간이 발생하였다.
- 99번째 백분위수에서는 응답 시간이 2,519ms로, 여전히 긴 지연 시간을 보였다.
- 응답 시간이 1,200ms 이상인 요청이 14% 정도 존재하여, RDB에서는 높은 부하 상황에서 성능이 급격히 저하됨을 알 수 있다.

응답 시간의 분포는 대부분 800ms 이상으로 나타났다. 
최대 응답 시간은 3,250ms였고, 95번째 백분위수는 2,128ms로 매우 긴 지연 시간을 보인다. 
이러한 결과는 RDB가 높은 부하를 처리하는 데 있어서 큰 제약이 있음을 시사한다.

따라서 RDB 기반의 대기열은 한 인스턴스에서 초당 약 100~150건의 요청을 수용할 수 있다.




## 비교 분석

마지막으로 Redis와 RDB를 비교 분석하고 정리해보자. 

### Gatling 리포트를 기준으로 한 성능 비교

**1분 요청 수 (Request Count)**
- Redis: 32,573건 (성공: 32,573건, 실패: 0건)
- RDB: 7,115건 (성공: 7,115건, 실패: 0건)

**초당 요청 수 (Requests per Second)**
- Redis: 평균 542.883건/초
- RDB: 평균 118.583건/초

Redis는 RDB에 비해 분당 5배 가량 더 많은 요청을 처리할 수 있었다.


**응답 시간 (Response Time)**
- Redis:
  - 최소 응답 시간: 1ms
  - 최대 응답 시간: 1,144ms
  - 평균 응답 시간: 84ms
  - 95번째 백분위수: 510ms
  - 99번째 백분위수: 921ms
- RDB:
  - 최소 응답 시간: 4ms
  - 최대 응답 시간: 3,250ms
  - 평균 응답 시간: 514ms
  - 95번째 백분위수: 2,128ms
  - 99번째 백분위수: 2,519ms

응답 시간에서는 더 큰 차이를 볼 수 있다. Redis의 응답 시간이 RDB에 비해 월등히 빠르다.
Redis는 99%의 요청이 1초 이내에 처리된 반면, RDB는 95%의 요청만 2초 이내에 처리되었다.


### 그라파나에서 확인되는 차이

Grafana를 통해서도 다음과 같이 확인된다. 

**초당 요청 수 (Requests per Second)**
![compare-request-per-sec.png](documents%2Fperformance-test%2Ftoken%2Fcompare-request-per-sec.png)

위 그래프에서 보듯이, Redis는 RDB에 비해 지속적으로 더 높은 초당 요청 수를 유지하고 있다.

**요청 시간 (Request Duration)**
![compare-request-duration.png](documents%2Fperformance-test%2Ftoken%2Fcompare-request-duration.png)

요청 시간 그래프에서도 Redis가 RDB보다 일관되게 짧은 응답 시간을 보이고 있으며, RDB의 경우 요청 시간이 상대적으로 불안정하고 길게 나타난다.

**GC 횟수 (GC Count)**
![copare-gc-count.png](documents%2Fperformance-test%2Ftoken%2Fcopare-gc-count.png)

GC 횟수에서도 Redis가 RDB보다 낮은 수치를 보이고 있어, 메모리 관리에서도 Redis가 더 효율적임을 알 수 있다.

### 결론 및 요약

대기열 시스템을 레디스로 구성할 경우 RDB를 사용하는 것 대비 훨씬 큰 가용성과 성능을 기대할 수 있다.  





</details>



<details>
<summary><b>레디스 기반의 은행 창구 대기열 큐 메커니즘 분석 및 최적 방안 탐구</b></summary>


# 개요

대기열 큐를 구현하는 방식으로 두 가지 메커니즘을 고려해볼 수 있다. 첫 번째는 은행 창구 방식이고 두 번째는 놀이 공원 방식이다. 은행 창구 방식은 한 명이 처리열에서 빠져나가면 한 명이 대기열에 들어오는 방식이며, 놀이 공원 방식은 일정 시간 동안 n명을 들여보내고 m 시간이 지나면 자동으로 빠져나가는 방식이다.


### 은행 창구 방식

은행 창구 방식은 실제 은행 창구에서 사람들이 줄을 서서 기다리는 것과 유사하다. 이 방식에서는 한 명이 처리열에서 빠져나갈 때마다 대기열에서 한 명이 들어온다. 이를 통해 현재 처리 중인 인원 수를 정확히 카운팅하는 것이 중요하다. 예를 들어, 은행 창구 방식에서는 다음과 같은 로직이 필요하다:

- **변수 정의**:
  - n: 현재 처리 중인 사람 수
  - m: 대기열에서 기다리는 사람 수

- **처리 로직**:
  1. 한 명이 처리열에서 빠져나가면(n--), 대기열에서 한 명이 처리열로 이동(m--, n++).
  2. 이를 통해 전체 서버의 가용량 대비 현재 몇 명을 수용할 수 있는지 파악.

- **예시**:
  - 현재 처리 중인 인원이 5명(n=5)이고, 대기열에 10명(m=10)이 있다면:
    1. 한 명이 처리 완료되면(n=4).
    2. 대기열에서 한 명이 처리열로 이동(m=9, n=5).

### 놀이 공원 방식

놀이 공원 방식은 놀이 공원의 특정 놀이기구에 사람들이 일정 시간 동안 타고 있다가, 일정 시간이 지나면 자동으로 빠져나가는 것과 유사하다. 이 방식에서는 일정 시간 동안 n명을 들여보내고, m 시간이 지나면 자동으로 빠져나가게 된다.

- **변수 정의**:
  - n: 일정 시간에 입장한 사람 수
  - m: 일정 시간 후 자동으로 빠져나가는 시간

- **처리 로직**:
  1. 일정 시간마다 n명을 들여보낸다.
  2. m 시간이 지나면, 해당 유저들은 자동으로 빠져나가며 새로운 사이클이 시작된다.

- **예시**:
  - 30분마다 20명(n=20)이 입장하고, 60분(m=60)이 지나면 자동으로 퇴장.
  - 예를 들어, 9:00에 20명이 입장하면 9:30에 다시 20명이 입장하지만, 9:00에 입장한 20명은 10:00에 퇴장.

이와 같이, 은행 창구 방식과 놀이 공원 방식은 각각의 특징과 처리 방식을 가지고 있다.

본 글의 목적은 **`은행 창구 방식`** 에서의 다양한 구현 방식을 탐구하고, 그 장단점들을 분석하여 최적의 방법을 모색하는 것이다.


# 0. 공통: 대기 토큰을 Sorted Set으로 관리

## 아키텍처

![공통 부분_ 대기 토큰을 Sorted Set으로 관리.png](documents%2Farchitecture%2Fqueue%2F%EA%B3%B5%ED%86%B5%20%EB%B6%80%EB%B6%84_%20%EB%8C%80%EA%B8%B0%20%ED%86%A0%ED%81%B0%EC%9D%84%20Sorted%20Set%EC%9C%BC%EB%A1%9C%20%EA%B4%80%EB%A6%AC.png)



## 프로세스
1. 다수의 유저가 동시에 요청을 보낸다.
2. 각 유저를 토큰화하여 Redis Sorted Set에 저장한다.



# 1. 활성 토큰을 상태로 관리하는 방식

## 아키텍처

![활성 토큰을 상태로 관리하는 방식.png](documents%2Farchitecture%2Fqueue%2F%ED%99%9C%EC%84%B1%20%ED%86%A0%ED%81%B0%EC%9D%84%20%EC%83%81%ED%83%9C%EB%A1%9C%20%EA%B4%80%EB%A6%AC%ED%95%98%EB%8A%94%20%EB%B0%A9%EC%8B%9D.png)

## 프로세스

1. 다수의 유저가 동시에 요청을 보낸다.
2. 개별 건에 대해서, Active 유저를 key-value 저장소에서 전체 조회하여 현재 활성화 할 수 있는 유저 수를 계수한다. O(N)
3. 활성화가 가능한 경우
  - a. 해당 유저를 Active 상태를 포함하여 토큰화한다. (e.g. active-xxxxx)
  - b. 해당 토큰을 key-value 저장소에 저장한다. O(1)
4. 활성화가 불가능한 경우
  - a. 해당 유저를 Waiting 상태를 포함하여 토큰화한다. (e.g. waiting-xxxxx)
  - b. 해당 토큰을 Sorted Set에 저장한다. O(logN)

## 장점
- 로직이 아주 심플하다.
- 별도의 스케줄링 작업이 필요하지 않다.

## 단점

- 로직의 결합도가 너무 높다: 확장에 유연할까? 부하 분산이 쉬울까?
- 대기열 인입 요청은 본 시스템 중 부하가 가장 큰 구간이다. 해당 구간에서 너무 많은 작업을 하므로 부하가 예상된다. (key-value 저장소에서 전체 조회시 O(N))


# 2. 활성 토큰을 별도의 토큰으로 분리하는 방식 1: Hashset과 스케줄링을 이용한 카운트

## 아키텍처

![활성 토큰을 별도의 토큰으로 분리하는 방식 1_ Hashset과 스케줄링을 이용한 카운트.png](documents%2Farchitecture%2Fqueue%2F%ED%99%9C%EC%84%B1%20%ED%86%A0%ED%81%B0%EC%9D%84%20%EB%B3%84%EB%8F%84%EC%9D%98%20%ED%86%A0%ED%81%B0%EC%9C%BC%EB%A1%9C%20%EB%B6%84%EB%A6%AC%ED%95%98%EB%8A%94%20%EB%B0%A9%EC%8B%9D%201_%20Hashset%EA%B3%BC%20%EC%8A%A4%EC%BC%80%EC%A4%84%EB%A7%81%EC%9D%84%20%EC%9D%B4%EC%9A%A9%ED%95%9C%20%EC%B9%B4%EC%9A%B4%ED%8A%B8.png)


## 프로세스

### 로직 1

1. 다수의 유저가 동시에 요청을 보낸다.
2. 각 유저를 토큰화하여 Redis Sorted Set에 저장한다. O(logN)


### 로직 2

스케줄링으로 다음과 같은 스텝을 반복한다.

1. 활성 토큰 개수를 Set size를 조회하여 확인한다. O(1)
2. 활성 토큰 개수가 임계치 미만인 경우, 즉 수용 가능한 경우
  - a. 해당 개수 만큼 대기 토큰에서 인입 순으로 가져온다. O(logN + M: N은 sorted set의 크기, M은 가져오는 요수의 수)
  - b. 가져온 대기 토큰을 활성 토큰으로 변환한다.
  - c. 변환한 활성 토큰을 Set에 저장한다. O(logN)
  - d. 변환한 활성 토큰을 만료 시간 및 기타 메타 정보를 value로 설정하여 HashSet에 저장한다.
  - e. 해당하는 대기 토큰을 Sorted Set에서 제거한다. O(logN)
3. 활성 토큰 개수가 임계치 이상인 경우, 즉 수용 불가한 경우
  - a. 아무런 작업을 수행하지 않는다.

### 로직 3

스케줄링으로 다음과 같은 스텝을 반복한다.

1. HashSet을 전체 조회한다. O(N)
2. 조회한 토큰 중 만료 시간이 지난 활성 토큰을 확인한다.
3. 만료 시간이 지난 활성 토큰에 대해서, hashset에서 제거한다. O(1)
4. 만료 시간이 지난 활성 토큰에 대해서, Set에서 제거한다. O(1)



## 장점

- 로직이 분산되어 확장에 용이하다.
- 유저의 대기열 인입 요청에서 부하를 감당하기 수월하다.

## 단점

- Set, HashSet, 두 개의 스케줄링 -> 관리 포인트가 많다.
- HashSet 전체 조회 -> O(N)의 복잡도를 가진다. (단, 비동기 작업이므로 부하 감당은 가능하다.)



# 3. 활성 토큰을 별도의 토큰으로 분리하는 방식 2: Counter와 Redis keyspace notification을 이용한 카운트

## 아키텍처
![활성 토큰을 별도의 토큰으로 분리하는 방식 2_ Counter와 Redis keyspace notification을 이용한 카운트.png](documents%2Farchitecture%2Fqueue%2F%ED%99%9C%EC%84%B1%20%ED%86%A0%ED%81%B0%EC%9D%84%20%EB%B3%84%EB%8F%84%EC%9D%98%20%ED%86%A0%ED%81%B0%EC%9C%BC%EB%A1%9C%20%EB%B6%84%EB%A6%AC%ED%95%98%EB%8A%94%20%EB%B0%A9%EC%8B%9D%202_%20Counter%EC%99%80%20Redis%20keyspace%20notification%EC%9D%84%20%EC%9D%B4%EC%9A%A9%ED%95%9C%20%EC%B9%B4%EC%9A%B4%ED%8A%B8.png)


## 프로세스

### 로직 1

1. 다수의 유저가 동시에 요청을 보낸다.
2. 각 유저를 토큰화하여 Redis Sorted Set에 저장한다. O(logN)



### 로직 2

스케줄링으로 다음과 같은 스텝을 반복한다.

1. 활성 토큰 개수를 Counter로 조회하여 확인한다. O(1)
2. 활성 토큰 개수가 임계치 미만인 경우, 즉 수용 가능한 경우
  - a. 해당 개수 만큼 대기 토큰에서 인입 순으로 가져온다. O(logN + M: N은 sorted set의 크기, M은 가져오는 요수의 수)
  - b. 가져온 대기 토큰을 활성 토큰으로 변환한다.
  - c. 변환한 활성 토큰을 key-value 저장소에 TTL과 함께 저장한다. O(1)
  - d. 저장한 개수를 카운터에 기록한다. O(1)
  - e. 해당하는 대기 토큰을 Sorted Set에서 제거한다.  O(logN)
3. 활성 토큰 개수가 임계치 이상인 경우, 즉 수용 불가한 경우
  - a. 아무런 작업을 수행하지 않는다.



### 로직 3

레디스의 keyspace notification을 이용하여 만료 시간이 지난 활성 토큰을 확인한다.

1. 레디스는 key-value 저장소 요소 중 만료 이벤트를 keyspace notification으로 발행한다. O(1)
2. 어플리케이션은 keyspace notification을 구독하여 만료 이벤트를 수신한다. O(1)
3. 만료 이벤트를 수신하고 카운터 변수를 -1을 하여 계수 현황을 동기화한다. O(1)


## 장점

- 로직이 분산되어 확장에 용이하다.
- 유저의 대기열 인입 요청에서 부하를 감당하기 수월하다.


## 단점
- 카운터라는 별도의 관리 포인트가 생긴다.
- keyspace notification을 이용한 만료 이벤트 수신에서 유실 문제가 있다. 레디스의 keyspace notification은 `at least once`와 같은 유실 방지 정책을 지원하지 않아 메시지의 필연적 도달을 보장하지 않는다.



# 4. 활성 토큰을 별도의 토큰으로 분리하는 방식 3: Counter와 kafka pub/sub 이용한 카운트

## 아키텍처

![활성 토큰을 별도의 토큰으로 분리하는 방식 3_ Counter와 kafka pub.png](documents%2Farchitecture%2Fqueue%2F%ED%99%9C%EC%84%B1%20%ED%86%A0%ED%81%B0%EC%9D%84%20%EB%B3%84%EB%8F%84%EC%9D%98%20%ED%86%A0%ED%81%B0%EC%9C%BC%EB%A1%9C%20%EB%B6%84%EB%A6%AC%ED%95%98%EB%8A%94%20%EB%B0%A9%EC%8B%9D%203_%20Counter%EC%99%80%20kafka%20pub.png)

## 프로세스

### 로직 1

다음과 같은 로직을 WaitingQueueService가 수행한다.

1. 다수의 유저가 동시에 요청을 보낸다.
2. 각 유저를 토큰화하여 Redis Sorted Set에 저장한다. O(logN)
3. 저장한 토큰을 Kafka로 발행한다. O(1)



### 로직 2

다음과 같은 로직을 ActiveQueueService가 수행한다.

1. Kafka로부터 토큰을 수신한다. O(1)
2. 활성 토큰 개수를 Counter로 조회하여 확인한다. O(1)
3. 활성 토큰 개수가 임계치 미만인 경우, 즉 수용 가능한 경우
  - a. 가장 최신의 토큰을 가져온다. O(1)
  - b. 가져온 대기 토큰을 활성 토큰으로 변환한다.
  - c. 변환한 활성 토큰을 key-value 저장소에 TTL과 함께 저장한다. O(1)
  - d. 저장한 개수를 카운터에 기록한다. O(1)
  - e. TTL 값을 카프카로 발행한다. O(1)



### 로직 3

다음과 같은 로직을 ExpirationService가 수행한다.

1. Kafka로부터 TTL 값을 수신한다. O(1)
2. TTL 값을 현재 시점 대비 계산하여 만료 여부를 확인한다.
3. 만료 시간이 된 경우
  - a. 카운터를 1 차감하여 동기화한다. O(1)
  - b. 카프카에 ACK로 응답하여 다음 메시지를 수신할 수 있도록 한다.
4. 만료 시간이 아닌 경우
  - a. 카프카에 NACK로 응답하여 다음 메시지를 수신하지 않도록 한다.



## 추가 개선

이 방식에서 조금 꺼림칙한 부분은 로직 3에서 Kafka를 다루는 방식이다. ACK를 주기 전까지는 계속 retry가 반복되며, Kafka에는 계속 이벤트들이 쌓이게 된다. Kafka는 고가용성의 메시지 큐(MQ)인데, 이 용도에 맞게 Kafka가 사용되는 것인가에 대한 의문이 생긴다.

기존의 문제를 해결하는 그 목적 자체를 달성하면서도 이 문제를 개선해볼 수 있을까?

레디스를 사용해보면 어떨까? 다음과 같은 개선 방안을 탐구해보자.

### 개선 방안: Redis 활용

1. **TTL 정보 저장**:
  - TTL 정보를 Redis의 SortedSet에 저장한다. SortedSet은 자동으로 rank로 정렬되기 때문에 가장 앞의 요소를 쉽게 가져올 수 있다.
  - 이는 Kafka가 순서를 보장함으로써 기존 방식에서 맨 먼저 발행된 이벤트, 즉 TTL이 가장 빠른 이벤트를 컨슘하는 것과 동일한 효과를 낸다.

2. **소비 메커니즘**:
  - Kafka 대신 스케줄러를 사용하여 Redis SortedSet을 폴링한다. Kafka에서의 컨슘 메커니즘도 결국 폴링이기 때문에, 이를 어플리케이션에서 구현하는 방식으로 해석할 수 있다.


가능할 것 같다!

정리해보면 구체적인 구현 방안은 다음과 같다.


- **ActiveQueueService**:
  - 활성 토큰에 대한 정보를 Kafka에 발행하는 대신, Redis에 만료 SortedSet을 만들어 해당 SortedSet에 저장한다. 이때 TTL 기준으로 정렬되도록 한다.

- **ExpirationService**:
  - Redis SortedSet을 폴링하여 만료된 TTL을 확인한다.
  - TTL이 만료된 토큰을 받아서 만료된 값에 따라 카운터 값을 동기화한다.
  - SortedSet 조회는 맨 앞의 요소를 가져오므로 O(1)로 수행할 수 있다. 만약 조회할 요소의 개수가 늘어난다면 조회 개수를 동적으로 조정해도 될 것이다.


결론적으로, Kafka를 사용할 때와 메커니즘 자체는 동일하지만, 보다 적절하게 문제를 해결할 수 있는 방식이다. 다만, 이 방식에서는 Sorted Set에서 만료된 값을 지워주어야 하므로, 멀티 인스턴스 환경에서 동시 스케줄링 작동 시 동시성 이슈가 발생할 수 있음을 고려하여 로직을 구현해야 한다.

이렇게 Redis의 SortedSet을 활용하여 보다 TTL 처리와 카운터 동기화를 O(1)로 해결할 수 있다.


### 최종 아키텍처
![활성 토큰을 별도의 토큰으로 분리하는 방식 3 refactored_ Counter와 kafka pub.png](documents%2Farchitecture%2Fqueue%2F%ED%99%9C%EC%84%B1%20%ED%86%A0%ED%81%B0%EC%9D%84%20%EB%B3%84%EB%8F%84%EC%9D%98%20%ED%86%A0%ED%81%B0%EC%9C%BC%EB%A1%9C%20%EB%B6%84%EB%A6%AC%ED%95%98%EB%8A%94%20%EB%B0%A9%EC%8B%9D%203%20refactored_%20Counter%EC%99%80%20kafka%20pub.png)


</details>







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



## 사용되는 쿼리


### 예약 가능 날짜 조회

- 설명: 사용자가 특정 콘서트의 예약 가능 날짜를 확인하기 위해 사용된다. ConcertOption 테이블과 concert 테이블에서 concertId와 concertDate 조건에 맞는 데이터를 조회한다.
- 빈도: ★★★★★
  - 사용자가 예약 가능 날짜를 조회할 때마다 호출되므로 자주 사용될 수 있다.
- 복잡도: ★★★☆☆
  - 두 개의 테이블을 조회하며, 날짜와 ID 조건을 사용하여 필터링한다.
  - 특히, concertId를 기준으로 ConcertOption 테이블과 Concert 테이블을 조인한다.




<details>
<summary><b>쿼리 1:</b> Seat 테이블에서 concertOptionId로 특정 콘서트 옵션에 해당하는 좌석을 조회하는 쿼리</summary>

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
</details>


<details>

<summary><b>쿼리 2:</b> ConcertOption 테이블에서 concertOptionId로 특정 콘서트 옵션과 관련된 콘서트 정보를 조회하는 쿼리</summary>


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

</details>


### 예약 가능 좌석 조회
- **설명:** 특정 콘서트 옵션에 대해 예약 가능한 좌석을 조회한다. `Seat` 테이블과 `ConcertOption` 테이블을 조인하여 `concertOptionId`와 `requestAt` 조건에 맞는 데이터를 가져온다.
- **빈도:** ★★★★☆
  - 예약 가능한 좌석 정보를 조회할 때마다 호출되며, 사용 빈도가 높다.
- **복잡도:** ★★★☆☆
  - 좌석의 예약 가능 여부를 필터링해야 하므로 단순한 조회보다 복잡하다. 좌석이 예약되었는지 여부를 확인하기 위해 `occupied` 필드를 검사하며, 또한 `concertOptionId`를 기준으로 `Seat` 테이블과 `ConcertOption` 테이블을 조인하여 관련 데이터를 가져온다. 이러한 과정에서 여러 조건을 사용하여 데이터를 필터링하고, 조인 연산을 통해 테이블 간의 관계를 처리해야 한다. 특히, 좌석의 예약 가능 상태를 정확히 판별하기 위해 `requestAt` 매개변수를 사용하여 요청 시점에서의 예약 가능 상태를 확인한다.
  - 따라서 단순한 조회 쿼리보다 더 많은 조건과 연산을 포함하게 되어 복잡도가 증가한다.


<details>
<summary><b>쿼리 1:</b> Seat 테이블에서 concertOptionId로 좌석 정보를 조회하는 쿼리</summary>

```sql
select
        seat 
from
     Seat seat 
where
     seat is not null 
     and seat.concertOption.concertOptionId = ?
```

</details>

<details>
<summary><b>쿼리 2:</b> ConcertOption 테이블에서 concert_id로 옵션과 콘서트 정보를 조회하는 쿼리</summary>

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

</details>


### 예약 요청

- **설명:** 특정 콘서트 옵션에 대한 좌석 정보를 조회하고, 좌석 예약을 처리한다. `ConcertOption` 테이블과 `Seat` 테이블을 조인하여 필요한 데이터를 가져오고, 필요한 경우 임시 예약 테이블에 데이터를 삽입하거나 기존 데이터를 업데이트한다.

- **빈도:** ★★★★☆
  - 예약 요청은 사용자가 특정 콘서트의 좌석을 예약할 때마다 발생하므로 자주 사용된다.
  - 다만 1명당 거의 1:1로 대응되는 만큼 발생 빈도수는 한정적이다.

- **복잡도:** ★★★★☆
  - 여러 테이블을 조회하고, 데이터를 삽입하거나 업데이트한다. 좌석의 예약 가능 여부를 확인하고, 임시 예약을 처리하는 과정에서 동시성 제어와 데이터 무결성을 유지해야 한다. 또한, 여러 조건을 만족하는 데이터를 조회하고, 필요한 경우 행 잠금을 사용하여 동시성 문제를 해결한다.



<details>
<summary><b>쿼리 2:</b> ConcertOption 테이블에서 `concertOptionId`로 조회하는 쿼리</summary>

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

</details>



<details>
<summary><b>쿼리 2:</b> Seat 테이블에서 특정 조건을 만족하는 좌석 데이터를 조회하는 쿼리</summary>

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
</details>

<details>
<summary><b>쿼리 3:</b> TemporalReservation 테이블에 새로운 임시 예약 데이터를 삽입하는 쿼리</summary>

```sql
insert into temporal_reservation (concert_option_id, created_at, expire_at, is_canceled, is_confirmed, request_at, reserve_at, seat_id, user_id)
values (?, ?, ?, ?, ?, ?, ?, ?, ?)
```
</details>



<details>
<summary><b>쿼리 4:</b> `Seat` 테이블에서 특정 좌석의 상태를 업데이트하는 쿼리</summary>
```sql
update seat
set concert_option_id=?,
  occupied=?,
  seat_number=?
where seat_id=?
```

</details>



### 임시 예약 확정

- **설명:** 임시 예약 확정을 처리하는 쿼리들로, 사용자가 임시로 예약한 좌석을 확정된 예약으로 전환하는 작업을 수행한다. `temporal_reservation`, `concert_option`, `concert`, `seat` 테이블을 조회하여 필요한 정보를 가져오고, 임시 예약을 확정하거나 새로운 예약을 삽입한다.

- **빈도:** ★★★☆☆
  - 임시 예약 확정은 사용자가 예약을 최종적으로 확정할 때마다 발생하는 중요한 작업이므로 비즈니스가 수행된다는 가정하에 반드시 n개에 데이터만큼 발생한다.
  - 다만 1명당 거의 1:1로 대응되는 만큼 발생 빈도수는 한정적이다.

- **복잡도:** ★★★★★
  - 이 쿼리들은 여러 테이블 간의 조인과 데이터 삽입 및 업데이트 작업을 포함하며, 특히 동시성 제어와 데이터 무결성을 유지해야 한다. 임시 예약을 확정하는 과정에서 여러 조건을 만족해야 하며, 데이터의 일관성을 유지하기 위해 행 잠금을 사용하여 동시성 문제를 해결한다. 이러한 작업은 높은 복잡도를 가진다.



<details>
<summary><b>쿼리 1:</b>임시 예약 ID와 연결된 콘서트 옵션, 콘서트, 좌석의 상세 정보를 가져오는 쿼리 </summary>

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
</details>


<details>
<summary><b>쿼리 2:</b> 기존 임시 예약 데이터를 업데이트하는 쿼리로, 임시 예약의 상태를 확정된 상태로 변경하는 쿼리</summary>

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
</details>




### 예약 상태 조회

- **설명:** 특정 사용자와 콘서트 옵션에 대한 예약 정보를 조회하며, 예약된 좌석의 세부 정보를 가져온다. `Reservation`, `ConcertOption`, `Seat`, 그리고 `TemporalReservation` 테이블을 사용하여 예약 및 임시 예약 상태를 조회한다.

- **빈도:** ★★★★☆
  - 예약 상태 조회는 사용자가 자신의 예약 현황을 확인할 때마다 발생하며, 특정 이벤트 전후에 빈번하게 사용될 것이다.

- **복잡도:** ★★★★☆
  - 이 쿼리들은 여러 테이블을 조인하여 데이터를 조회하고, 사용자와 콘서트 옵션에 대한 다양한 조건을 만족해야 한다. 또한, 데이터 무결성을 유지하고 최신 상태를 반영해야 하므로 복잡도가 높다.


<details>
<summary><b>쿼리 1:</b>  Reservation 테이블에서 특정 사용자와 콘서트 옵션에 대한 예약 데이터를 조회하는 쿼리
</summary>

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

</details>



<details>
<summary><b>쿼리 2:</b> ConcertOption 테이블에서 특정 콘서트 옵션에 대한 세부 정보를 조회하는 쿼리</summary>

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

</details>


<details>
<summary><b>쿼리 3:</b> Seat 테이블에서 특정 좌석에 대한 정보를 조회하는 쿼리 </summary>


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

</details>



<details>
<summary><b>쿼리 4:</b> TemporalReservation 테이블에서 특정 사용자와 콘서트 옵션에 대한 임시 예약 데이터를 조회하는 쿼리</summary>


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

</details>






### 콘서트 생성

- **설명:** `concert` 테이블에 새로운 콘서트를 삽입한다.

- **빈도:** ★☆☆☆☆
  - 콘서트 생성 쿼리는 관리자나 운영자가 새로운 콘서트를 등록할 때 사용되며, 일반 사용자가 자주 호출하지는 않는다. 따라서 빈도는 낮다.

- **복잡도:** ★☆☆☆☆
  - 단일 테이블에 데이터를 삽입하는 단순한 구조로, 입력 값의 유효성만 검증하면 되므로 복잡도는 낮다.


<details>
<summary><b>쿼리 1:</b> Concert를 생성하는 쿼리</summary>


```sql
insert for
        io.reservationservice.api.business.domainentity.Concert 
    into
        concert (created_at, request_at, title) 
    values
        (?, ?, ?)
```

</details>



### 콘서트 옵션 생성

- **설명:** 특정 콘서트에 대한 다양한 옵션을 설정하고 저장하며, 각 콘서트 옵션에 대한 세부 정보를 데이터베이스에 추가한다. 또한, 콘서트 옵션과 관련된 좌석 정보를 함께 저장하여 사용자가 선택할 수 있도록 한다.

- **빈도:** ★★★☆☆
  - 콘서트 옵션 생성은 새로운 콘서트가 추가될 때마다 발생하며, 이벤트나 프로모션을 기획할 때 사용된다. 빈도는 상대적으로 낮지만, 이벤트 계획 시 집중적으로 발생할 수 있다.

- **복잡도:** ★★★★☆
  - 이 쿼리들은 여러 테이블에 데이터를 삽입해야 하며, 콘서트 옵션과 관련된 다양한 정보를 처리한다. 특히, 좌석 정보와 관련된 테이블을 함께 업데이트해야 하므로 복잡도가 높다.


<details>
<summary><b>쿼리 1:</b> concert 테이블에서 콘서트 ID로 콘서트를 조회하는 쿼리.</summary>


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
</details>


<details>
<summary><b>쿼리 2:</b> concert_option 테이블에 새로운 콘서트 옵션을 삽입하는 쿼리</summary>

```sql
insert into
    concert_option (concert_id, concert_date, concert_duration, created_at, description, price, request_at, title) 
values
    (?, ?, ?, ?, ?, ?, ?, ?)
```
</details>



<details>
<summary><b>쿼리 3:</b> seat 테이블에 새로운 좌석 정보를 삽입하는 쿼리</summary>

```sql
insert into
    seat (concert_option_id, created_at, occupied, seat_number) 
values
    (?, ?, ?, ?)
```

</details>



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


#### 종합 결론

따라서, 현재 검색 조건이 없더라도 `title`과 `createdAt` 컬럼에 인덱스를 설정하여 향후 검색 요구에 대비할 수 있다.


<details>
<summary>JPA 엔티티 인덱스 설정 코드</summary>



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

</details>






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

#### 종합 결론


1. **단일 인덱스**
  - `concertDate`
  - `concertOptionId`

2. **복합 인덱스**
  - `concertId, concertDate`


<details>
<summary>JPA 엔티티 인덱스 설정 코드</summary>

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


</details>




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

#### 종합 결론


1. **단일 인덱스**
  - `concertOptionId`
  - `seatNumber`

2. **복합 인덱스**
  - `concertOptionId, seatNumber`
  - `concertOptionId, occupied`

- **예약 가능 좌석 조회** 쿼리는 `concertOptionId`와 `seatNumber`를 조건으로 사용하여 특정 콘서트 옵션 내의 좌석을 조회하기 때문에 이 두 컬럼에 대한 복합 인덱스가 필요하다.
- **좌석 예약 여부 확인** 쿼리는 `concertOptionId`와 `occupied` 컬럼을 자주 사용하므로 이들에 대한 복합 인덱스가 유용하다.
- 단일 인덱스는 `concertOptionId`와 `seatNumber`에 설정하여 기본적인 조회 성능을 향상시킬 수 있다. 다만, `concertOption`에 대한 복합 인덱스가 이미 존재하므로 `concertOption`에 대한 단일 인덱스는 불필요하다.



<details>
<summary>JPA 엔티티 인덱스 설정 코드</summary>

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

</details>




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

#### 종합 결론


1. **단일 인덱스**
  - `userId`
  - `concertOptionId`

2. **복합 인덱스**
  - `userId, concertOptionId`
  - `concertOptionId, reserveAt`


- **사용자 예약 현황 조회** 쿼리는 `userId`와 `concertOptionId`를 조건으로 사용하여 특정 사용자와 콘서트 옵션에 대한 예약 내역을 조회하기 때문에 이 두 컬럼에 대한 복합 인덱스가 필요하다.
- **예약 내역 시간 순 조회** 쿼리는 `concertOptionId`와 `reserveAt`를 조건으로 사용하여 특정 콘서트 옵션의 예약 내역을 시간 순으로 조회하기 때문에 이 두 컬럼에 대한 복합 인덱스가 유용하다.
- 단일 인덱스는 `userId`와 `concertOptionId`에 설정하여 기본적인 조회 성능을 향상시킬 수 있다. 다만, 두 필드 모두 복합 인덱스로 사용되고 있으므로, 단일 인덱스는 제외한다.


<details>
<summary>JPA 엔티티 인덱스 설정 코드</summary>

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

</details>





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


#### 종합 결론



<details>
<summary>JPA 엔티티 인덱스 설정 코드</summary>

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

</details>






## BEFORE & AFTER

인덱스 적용 전과 후를 비교한다.


### 예약 가능 날짜 조회 API

<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

ConcertOption 테이블에서 concertId와 date로 조회하는 쿼리

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
</details>



<details>
<summary>Before</summary>


```sql
1,SIMPLE,co1_0,,ALL,,,,,992058,3.33,Using where
```

```sql
-> Filter: ((co1_0.concert_id = 123) and (co1_0.concert_date > TIMESTAMP'2024-01-01 00:00:00'))  (cost=107117 rows=33065) (actual time=1.52..2063 rows=100 loops=1)
    -> Table scan on co1_0  (cost=107117 rows=992058) (actual time=1.43..1998 rows=1e+6 loops=1)
```

</details>


<details>
<summary>After</summary>

```sql
1,SIMPLE,co1_0,,range,"idx_concert_date,idx_concert_id_date",idx_concert_id_date,18,,100,100,Using index condition
```


```sql
-> Index range scan on co1_0 using idx_concert_id_date over (concert_id = 123 AND '2024-01-01 00:00:00.000000' < concert_date), with index condition: ((co1_0.concert_id = 123) and (co1_0.concert_date > TIMESTAMP'2024-01-01 00:00:00'))  (cost=119 rows=100) (actual time=0.916..28.9 rows=100 loops=1)
```

</details>



#### 결과 분석

| 항목              | Before                                                                                                      | After                                                                                                            |
|-------------------|-------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| **인덱스 사용**   | 없음 (`FULL` 스캔)                                                                                         | `idx_concert_id_date` 인덱스를 사용하여 `concert_id`와 `concert_date`로 인덱스 룩업 수행                          |
| **필터링 단계**   | 테이블 스캔 후 `concert_id`와 `concert_date`에 대한 필터링 수행                                              | 인덱스 룩업 단계에서 `concert_id`와 `concert_date` 조건을 만족하는 레코드를 효율적으로 검색                       |
| **비용**          | 107117                                                                                                      | 119                                                                                                              |
| **예상 행 수**    | 992058                                                                                                      | 100                                                                                                              |
| **실제 실행 시간**| 1.52..2063 ms                                                                                               | 0.916..28.9 ms                                                                                                   |
| **실제 처리된 행 수**| 1e+6                                                                                                       | 100                                                                                                              |

Before의 쿼리는 인덱스를 사용하지 않고 전체 테이블을 스캔하여 데이터를 필터링하기 때문에 실행 시간이 길고 비용이 높다. After의 쿼리는 적절한 인덱스를 사용하여 `concert_id`와 `concert_date`에 대한 조건을 만족하는 데이터를 효율적으로 검색하여 실행 시간을 대폭 단축시킨다.





<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

`ConcertOption` 테이블에서 `concertOptionId`로 특정 콘서트 옵션과 관련된 콘서트 정보를 조회하는 쿼리


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

</details>



<details>
<summary>Before</summary>

```sql
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=250e-6..333e-6 rows=1 loops=1)
```

</details>


<details>
<summary>After</summary>

```sql
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=167e-6..250e-6 rows=1 loops=1)
```

</details>


#### 결과 분석

큰 변화 없음


### 예약 가능 좌석 조회


<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

Seat 테이블에서 concert_option_id로 특정 콘서트 옵션에 해당하는 좌석을 조회하는 쿼리


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

</details>


<details>
<summary>Before</summary>

```sql
1,SIMPLE,s,,ALL,,,,,996761,10,Using where
```


```sql
-> Filter: (s.concert_option_id = 123)  (cost=103169 rows=99676) (actual time=28.2..1983 rows=1 loops=1)
    -> Table scan on s  (cost=103169 rows=996761) (actual time=4.96..1896 rows=1.01e+6 loops=1)
```

</details>


<details>
<summary>After</summary>


```sql
1,SIMPLE,s,,ref,"idx_concert_option_seat_number,idx_concert_option_occupied",idx_concert_option_seat_number,9,const,1,100,
```

```sql
-> Index lookup on s using idx_concert_option_seat_number (concert_option_id=123)  (cost=1.1 rows=1) (actual time=0.534..0.539 rows=1 loops=1)
```


</details>



#### 결과 분석

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




<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

ConcertOption 테이블과 Concert 테이블을 조인하여 concert_option_id로 특정 콘서트 옵션과 관련된 콘서트 정보를 조회하는 쿼리

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


</details>



<details>
<summary>Before</summary>


```sql
1,SIMPLE,co1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```


```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=83e-6..125e-6 rows=1 loops=1)
```
</details>



<details>
<summary>After</summary>

```sql
1,SIMPLE,co1_0,,const,"PRIMARY,idx_concert_option_id",PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```


```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=42e-6..125e-6 rows=1 loops=1)
```

</details>



#### 결과 분석

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



<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

`ConcertOption` 테이블에서 `concertOptionId`로 조회하는 쿼리

```sql
EXPLAIN ANALYZE
SELECT *
FROM concert_option concert_option
WHERE concert_option.concert_option_id = 123
    FOR SHARE;
```

</details>




<details>
<summary>Before</summary>

```sql
1,SIMPLE,concert_option,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=84e-6..125e-6 rows=1 loops=1)
```

</details>


<details>
<summary>After</summary>

```sql
1,SIMPLE,concert_option,,const,"PRIMARY,idx_concert_option_id",PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=83e-6..124e-6 rows=1 loops=1)
```

</details>



#### 결과 분석

큰 차이 없음



<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

`Seat` 테이블에서 특정 조건을 만족하는 좌석 데이터를 조회하는 쿼리

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

</details>



<details>
<summary>Before</summary>

```sql
1,SIMPLE,s,,ALL,,,,,996761,1,Using where
```

```sql
-> Filter: ((s.seat_number = 1) and (s.concert_option_id = 123))  (cost=101189 rows=9968) (actual time=3932..3932 rows=0 loops=1)
    -> Table scan on s  (cost=101189 rows=996761) (actual time=14.6..3810 rows=1.01e+6 loops=1)
```

</details>


<details>
<summary>After</summary>

```sql
1,SIMPLE,s,,ref,"idx_seat_number,idx_concert_option_seat_number,idx_concert_option_occupied",idx_concert_option_seat_number,18,"const,const",1,100,
```


```sql
-> Index lookup on s using idx_concert_option_seat_number (concert_option_id=123, seat_number=1)  (cost=1.1 rows=1) (actual time=0.0417..0.0417 rows=0 loops=1)
```

</details>



#### 결과 분석

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





<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>


`Seat` 테이블에서 특정 좌석의 상태를 업데이트하는 쿼리
```sql
EXPLAIN 
update seat
set concert_option_id=123,
  occupied=1,
  seat_number=1
where seat_id=123
```


</details>




<details>
<summary>Before</summary>

```sql
1,UPDATE,seat,,range,PRIMARY,PRIMARY,8,const,1,100,Using where
```

</details>



<details>
<summary>After</summary>

```sql
1,UPDATE,seat,,range,PRIMARY,PRIMARY,8,const,1,100,Using where
```

</details>



#### 결과 분석

큰 차이 없음




### 임시 예약 확정


<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

임시 예약 ID와 연결된 콘서트 옵션, 콘서트, 좌석의 상세 정보를 가져오는 쿼리

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


</details>



<details>
<summary>Before</summary>


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


</details>



<details>
<summary>After</summary>


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

</details>



#### 결과 분석


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




<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

임시 예약의 상태를 확정된 상태로 변경하는 쿼리

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

</details>


<details>
<summary>Before</summary>


```sql
1,UPDATE,temporal_reservation,,range,PRIMARY,PRIMARY,8,const,1,100,Using where
```

</details>



<details>
<summary>After</summary>


```sql
1,UPDATE,temporal_reservation,,range,PRIMARY,PRIMARY,8,const,1,100,Using where
```

</details>




#### 결과 분석

큰 차이 없음



### 예약 상태 조회




<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

`Reservation` 테이블에서 특정 사용자와 콘서트 옵션에 대한 예약 데이터를 조회하는 쿼리

```sql
EXPLAIN
SELECT
   *
FROM
   reservation r
WHERE r.user_id = 123
  AND r.concert_option_id = 123
```

</details>



<details>
<summary>Before</summary>


```sql
1,SIMPLE,r,,ALL,,,,,994703,1,Using where
```


```sql
-> Filter: ((r.concert_option_id = 123) and (r.user_id = 123))  (cost=105075 rows=9947) (actual time=1.62..2717 rows=1 loops=1)
    -> Table scan on r  (cost=105075 rows=994703) (actual time=1.49..2615 rows=1e+6 loops=1)
```


</details>



<details>
<summary>After</summary>


```sql
1,SIMPLE,r,,ref,"idx_user_concert_option,idx_concert_option_seat",idx_user_concert_option,18,"const,const",1,100,
```

```sql
-> Index lookup on r using idx_user_concert_option (user_id=123, concert_option_id=123)  (cost=1.1 rows=1) (actual time=6.85..6.85 rows=1 loops=1)
```

</details>




#### 결과 분석

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


<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>


`ConcertOption` 테이블에서 특정 콘서트 옵션에 대한 세부 정보를 조회하는 쿼리

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



</details>



<details>
<summary>Before</summary>

```sql
1,SIMPLE,co1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=84e-6..126e-6 rows=1 loops=1)
```

</details>



<details>
<summary>After</summary>


```sql
1,SIMPLE,co1_0,,const,"PRIMARY,idx_concert_option_id",PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=125e-6..167e-6 rows=1 loops=1)
```

</details>



#### 결과 분석


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



<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

`Seat` 테이블에서 특정 좌석에 대한 정보를 조회하는 쿼리

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


</details>



<details>
<summary>Before</summary>

```sql
1,SIMPLE,s1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,co1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=84e-6..126e-6 rows=1 loops=1)
```

</details>



<details>
<summary>After</summary>

```sql
1,SIMPLE,s1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
1,SIMPLE,co1_0,,const,"PRIMARY,idx_concert_option_id",PRIMARY,8,const,1,100,
1,SIMPLE,c1_0,,const,PRIMARY,PRIMARY,8,const,1,100,
```

```sql
-> Rows fetched before execution  (cost=0..0 rows=1) (actual time=42e-6..83e-6 rows=1 loops=1)
```


</details>


#### 결과 분석


| 항목              | Before                                                                                                  | After                                                                                                   |
|-------------------|--------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| **인덱스 사용**   | `PRIMARY` 인덱스를 사용하여 `concert_option_id`와 `concert_id`로 인덱스 룩업 수행                                   | `PRIMARY` 및 `idx_concert_option_id` 복합 인덱스를 사용하여 `concert_option_id`와 `concert_id`에 대한 인덱스 범위 스캔 수행            |
| **비용**          | 0..0                                                                                                         | 0..0                                                                                                           |
| **예상 행 수**    | 1                                                                                                         | 1                                                                                                           |
| **실제 실행 시간**| 84e-6..126e-6 초                                                                                                | 42e-6..83e-6 초                                                                                                |
| **실제 처리된 행 수**| 1                                                                                                         | 1                                                                                                           |

인덱스 사용 방식에 일부 변화가 있다. `Before`에서는 `concert_option_id`와 `concert_id`에 대해 단일 인덱스 `PRIMARY`를 사용하여 조회했고, `After`에서는 `concert_option_id`와 `concert_id`에 대해 복합 인덱스 `idx_concert_option_id`를 추가적으로 사용했다. 비용과 예상 행 수는 동일하게 유지되었으나, 실제 실행 시간은 소폭 감소했다.




<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

`TemporalReservation` 테이블에서 특정 사용자와 콘서트 옵션에 대한 임시 예약 데이터를 조회하는 쿼리

```sql
EXPLAIN
select
   *
from
   temporal_reservation t
where t.user_id = 123
  and t.concert_option_id = 123
```


</details>



<details>
<summary>Before</summary>



```sql
1,SIMPLE,t,,ALL,,,,,994525,1,Using where
```


```sql
-> Filter: ((t.concert_option_id = 123) and (t.user_id = 123))  (cost=105186 rows=9945) (actual time=1.28..1616 rows=1 loops=1)
    -> Table scan on t  (cost=105186 rows=994525) (actual time=1.19..1530 rows=1e+6 loops=1)
```

</details>



<details>
<summary>After</summary>


```sql
1,SIMPLE,t,,ref,"idx_user_concert_option,idx_concert_option_reserve_at",idx_user_concert_option,18,"const,const",1,100,
```

```sql
-> Index lookup on t using idx_user_concert_option (user_id=123, concert_option_id=123)  (cost=1.1 rows=1) (actual time=0.337..0.345 rows=1 loops=1)
```

</details>




#### 결과 분석


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






## 사용되는 쿼리

### 잔액 조회


- **설명:** 잔액 조회는 특정 사용자의 현재 잔액 정보를 조회하는 쿼리이다. 이 쿼리는 `balance` 테이블에서 사용자의 잔액 관련 데이터를 가져온다. 사용자 ID를 조건으로 하여 해당 사용자의 잔액 정보를 반환한다. 반환되는 정보는 잔액 ID, 금액, 생성일자, 수정일자, 사용자 ID 등이다.

- **빈도:** ★★★☆☆
  - 잔액 조회는 사용자가 자신의 잔액을 확인할 때마다 발생하지만, 잔액 사용이나 충전과 같은 다른 주요 활동에 비해 상대적으로 빈도가 낮을 수 있다.

- **복잡도:** ★☆☆☆☆
  - 이 쿼리는 단일 테이블에서 데이터를 조회하며, 간단한 조건(`user_id`)만을 사용하기 때문에 복잡도가 낮다.




<details>
<summary><b>쿼리 1:</b> Balance 테이블에서 userId로 특정 사용자의 잔액 정보를 조회하는 쿼리</summary>


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

</details>



### 잔액 충전/사용 내역 조회

- **설명:** 특정 사용자의 잔액 충전 및 사용 내역을 조회하는 쿼리이다. 이 쿼리는 `balance_transaction` 테이블을 사용하여 사용자의 거래 ID, 금액, 생성 시간, 거래 사유, 거래 유형, 사용자 ID 및 버전 정보를 가져온다. 주로 사용자의 잔액 내역을 확인하거나 재무 분석에 사용된다.

- **빈도:** ★★★☆☆
  - 잔액 충전/사용 내역 조회는 사용자가 자신의 거래 내역을 확인하거나 관리자가 특정 사용자의 잔액 변동 상황을 파악할 때 주로 사용된다. 빈도는 상대적으로 평균적이며, 이벤트나 프로모션 기간 동안 증가할 수 있다.

- **복잡도:** ★★☆☆☆
  - 이 쿼리는 단일 테이블을 대상으로 하며, 사용자 ID에 기반한 단순한 조건을 사용한다. 조인이 없고 데이터 무결성을 유지하는 복잡한 로직이 포함되지 않아 비교적 단순하다.



<details>
<summary><b>쿼리 1:</b> BalanceTransaction 테이블에서 userId로 특정 사용자의 잔액 거래 내역을 조회하는 쿼리</summary>


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

</details>



### 잔액 충전

- **설명:** 이 섹션의 쿼리는 사용자의 잔액을 조회, 충전, 그리고 업데이트하는 데 사용된다. `Balance` 및 `BalanceTransaction` 테이블을 사용하여 사용자의 잔액 상태를 관리하며, 트랜잭션의 무결성을 유지하기 위해 동시성 제어를 포함한다. `select` 쿼리는 사용자의 잔액을 조회하고, `insert` 쿼리는 잔액 충전 트랜잭션을 기록하며, `update` 쿼리는 사용자의 잔액을 업데이트한다.

- **빈도:** ★★★★☆
  - 잔액 조회, 충전, 업데이트는 사용자 활동에 따라 빈번하게 발생한다. 특히 잔액 조회는 사용자가 자신의 잔액을 확인할 때 자주 발생하며, 충전과 업데이트는 결제나 서비스 이용 시 주로 사용된다.

- **복잡도:** ★★★☆☆
  - 쿼리들은 단일 테이블을 대상으로 하며, 비교적 단순한 조건을 사용하여 데이터를 조회, 삽입, 업데이트한다. 그러나 동시성 제어를 위한 `for update` 및 `optimistic locking`의 사용으로 인해 복잡도가 증가할 수 있다.



<details>
<summary><b>쿼리 1:</b> Balance 테이블에서 특정 사용자의 잔액을 조회하는 쿼리</summary>


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

</details>



<details>
<summary><b>쿼리 2:</b> BalanceTransaction 테이블에 새로운 잔액 충전 트랜잭션을 기록하는 쿼리</summary>

```sql
insert into
    balance_transaction (amount, created_at, transaction_reason, transaction_type, user_id, version) 
values
    (?, ?, ?, ?, ?, ?)
```

</details>


<details>
<summary><b>쿼리 3:</b>Balance 테이블에서 사용자의 잔액을 업데이트하는 쿼리.</summary>

```sql
update balance 
set
    amount=?,
    updated_at=?,
    user_id=? 
where
    balance_id=?
```

</details>




### 잔액 사용

- **설명:** 잔액 사용과 관련된 쿼리이다. 이 쿼리들은 사용자의 잔액 조회, 잔액 사용 내역 삽입, 그리고 잔액 업데이트 작업을 수행한다. `Balance` 테이블에서 사용자의 현재 잔액을 조회하고, `BalanceTransaction` 테이블에 잔액 사용 내역을 기록하며, `Balance` 테이블의 잔액을 업데이트한다. 이러한 쿼리들은 트랜잭션의 일관성을 보장하기 위해 `for update` 구문을 사용하여 동시성 문제를 방지한다.

- **빈도:** ★★★☆☆
  - 잔액 사용 쿼리는 사용자가 결제를 하거나 잔액을 사용할 때마다 발생한다. 결제 시스템이 자주 사용되는 서비스라면 빈도가 높아질 수 있다. 그러나 일반적인 사용 패턴에서는 조회 쿼리에 비해 빈도가 다소 낮다.

- **복잡도:** ★★★☆☆
  - 잔액 사용 쿼리는 비교적 간단한 구조로, 단일 테이블에 대한 조회 및 업데이트 작업을 포함한다. 그러나 트랜잭션 처리와 동시성 제어를 위한 `for update` 구문이 사용되어 약간의 복잡도가 추가된다.


<details>
<summary><b>쿼리 1:</b>Balance 테이블에서 특정 사용자의 잔액을 조회하는 쿼리.</summary>

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


</details>


<details>
<summary><b>쿼리 2:</b> BalanceTransaction 테이블에 잔액 사용 내역을 삽입하는 쿼리</summary>

```sql
insert into
        balance_transaction (amount, created_at, transaction_reason, transaction_type, user_id, version) 
    values
        (?, ?, ?, ?, ?, ?)
```

</details>


<details>
<summary><b>쿼리 3:</b>Balance 테이블의 특정 사용자의 잔액을 업데이트하는 쿼리</summary>

```sql
update balance 
    set
        amount=?,
        updated_at=?,
        user_id=? 
    where
        balance_id=?
```

</details>





#### 결제 처리

- **설명:** 결제 처리와 관련된 다양한 데이터베이스 쿼리를 실행한다. 주요 쿼리로는 잔액 조회, 잔액 충전 내역 삽입, 잔액 업데이트, 그리고 결제 내역 삽입이 있다. 이 쿼리들은 `Balance`, `BalanceTransaction`, `PaymentTransaction` 테이블을 사용하여 데이터의 일관성과 무결성을 유지한다.

- **빈도:** ★★★★★
  - 결제와 잔액 관리는 대부분의 사용자 활동에 필수적인 부분으로, 일상적으로 자주 발생한다. 특히 잔액 조회와 잔액 업데이트는 거의 모든 결제 과정에서 필수적으로 수행된다.

- **복잡도:** ★★★☆☆
  - 결제 처리 쿼리들은 주로 단일 테이블에서 실행되지만, 데이터의 일관성을 유지하고 트랜잭션을 처리해야 하므로 중간 수준의 복잡도를 가진다. 특히 `for update` 절을 포함한 쿼리는 동시성 문제를 해결하는 데 중요하다.



<details>
<summary><b>쿼리 1:</b>Balance 테이블에서 특정 사용자의 잔액을 조회하는 쿼리</summary>


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

</details>


<details>
<summary><b>쿼리 2:</b> BalanceTransaction 테이블에 잔액 충전 내역을 삽입하는 쿼리</summary>
```sql
insert into
        balance_transaction (amount, created_at, transaction_reason, transaction_type, user_id, version) 
    values
        (?, ?, ?, ?, ?, ?)
```

</details>



<details>
<summary><b>쿼리 3:</b>Balance 테이블에서 잔액을 업데이트하는 쿼리</summary>

```sql
update balance 
    set
        amount=?,
        updated_at=?,
        user_id=? 
    where
        balance_id=?
```

</details>



<details>
<summary><b>쿼리 4:</b>PaymentTransaction 테이블에 결제 내역을 삽입하는 쿼리</summary>


```sql
insert into
        payment_transaction (amount, created_at, payment_method, payment_status, target_id, user_id) 
    values
        (?, ?, ?, ?, ?, ?)
```


</details>




### 결제 취소

- **설명:** 결제 취소를 처리하는 API에서 사용되는 쿼리들을 설명한다. 이 API는 특정 결제 트랜잭션을 취소하는 기능을 제공하며, 관련된 여러 데이터베이스 작업을 수행하여 트랜잭션의 상태를 업데이트하고 잔액을 조정한다.

- **빈도:** ★★★★☆
  - 결제 취소는 다양한 이유로 빈번하게 발생할 수 있다. 예를 들어, 결제 오류, 사용자 요청, 서비스 중단 등의 이유로 결제가 취소될 수 있다. 특히, 특정 기간 동안의 프로모션이나 이벤트 등에서 취소 빈도가 증가할 수 있다.

- **복잡도:** ★★★★☆
  - 결제 취소는 여러 테이블 간의 상호작용을 필요로 하며, 데이터 무결성을 유지하기 위해 트랜잭션 관리가 중요하다. 결제 상태 업데이트, 잔액 조정, 트랜잭션 기록 등 다양한 작업이 포함되어 있어 복잡도가 높다.




<details>
<summary><b>쿼리 1:</b>PaymentTransaction 테이블에서 특정 결제 트랜잭션의 정보를 조회하는 쿼리</summary>


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

</details>



<details>
<summary><b>쿼리 2:</b> 잔액이 존재하는지 확인하고, 결제 취소 시 잔액을 업데이트하는 데 사용되는 쿼리</summary>

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

</details>



<details>
<summary><b>쿼리 3:</b>BalanceTransaction 테이블에 새로운 트랜잭션 기록을 삽입하여 결제 취소 시 잔액 변경을 기록하기 위해 사용되는 쿼리</summary>

```sql
insert into
        balance_transaction (amount, created_at, transaction_reason, transaction_type, user_id, version) 
    values
        (?, ?, ?, ?, ?, ?)
```

</details>



<details>
<summary><b>쿼리 4:</b> PaymentTransaction 테이블에서 결제 취소 시 트랜잭션의 상태를 '취소됨'으로 변경하는 쿼리</summary>

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

</details>

<details>
<summary><b>쿼리 5:</b>Balance 테이블에서 특정 사용자의 잔액을 업데이트하여 결제 취소 시 잔액을 조정하는 쿼리. </summary>

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

</details>





### 결제 내역 조회


- **설명:** 결제 내역을 조회하는 쿼리이다. 이 쿼리는 특정 사용자의 결제 기록을 가져오며, 트랜잭션 ID, 결제 금액, 생성 일자, 결제 방법, 결제 상태, 타겟 ID, 사용자 ID를 포함한 다양한 정보를 조회한다. `PaymentTransaction` 테이블을 사용하여 특정 사용자 ID에 대한 결제 내역을 가져온다.

- **빈도:** ★★★★☆
  - 결제 내역 조회는 사용자가 자신의 결제 이력을 확인하거나 관리자가 결제 상태를 검토할 때 자주 사용된다. 일반적으로 사용 빈도가 높으며, 특정 프로모션이나 결제 문제가 발생한 경우 빈도가 더욱 증가할 수 있다.

- **복잡도:** ★★☆☆☆
  - 이 쿼리는 단일 테이블에서 데이터를 조회하는 단순한 구조를 가지므로 복잡도가 낮다. 다만, 결제 기록의 양이 많을 수 있으므로 효율적인 인덱스 사용이 필요하다.





<details>
<summary><b>쿼리 1:</b>payment_transaction 테이블에서 user_id로 특정 사용자의 결제 트랜잭션을 조회하는 쿼리</summary>


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


</details>





## 인덱스 적절성 판단 및 설정

### Balance
#### 단일 인덱스 검토

1. **userId**
  - **필요성**: 사용자 잔액 조회 및 충전/사용 내역 조회에서 빈번히 사용된다. 사용자의 잔액 정보를 확인하거나 잔액 변동 내역을 조회하는 주요 조건으로 사용된다.
  - **카디널리티**: 높음 (사용자 ID는 유니크하여 카디널리티가 높다)
  - **변경 빈도**: 낮음 (사용자 ID는 잔액 생성 시 설정되며 이후 변경되지 않으므로 인덱스 재정렬 부담이 적다)

2. **createdAt**
  - **필요성**: 잔액 생성일자를 기준으로 조회하는 데 사용된다. 주로 특정 기간 동안의 잔액 변동을 확인할 때 유용하다.
  - **카디널리티**: 중간 (동일한 날짜에 여러 잔액이 생성될 수 있으므로 중복도가 존재할 수 있음)
  - **변경 빈도**: 낮음 (생성된 후 변경되지 않음)

#### 복합 인덱스 검토

1. **userId, createdAt**
  - **필요성**: 특정 사용자의 잔액 변동 내역을 시간순으로 조회할 때 유용하다. 사용자별 잔액 이력 조회에서 빈번히 사용된다.
  - **카디널리티**: 중간 (두 컬럼의 조합은 상당히 유니크할 가능성이 높아 효율적인 인덱스가 될 수 있다)
  - **변경 빈도**: 낮음 (사용자 ID와 생성일자는 변경되지 않으므로 인덱스 재정렬 부담이 적다)

2. **userId, amount**
  - **필요성**: 특정 사용자의 잔액 금액을 조건으로 조회할 때 유용하다. 예를 들어, 잔액이 특정 범위에 속하는 사용자를 조회하는 경우.
  - **카디널리티**: 중간 (두 컬럼의 조합은 특정 금액 범위 내에서 유니크할 가능성이 있다)
  - **변경 빈도**: 중간 (잔액 금액은 변경될 수 있으나 사용자 ID는 변경되지 않으므로 인덱스 재정렬 부담이 적다)




#### 종합 결론


<details>
<summary>JPA 엔티티 인덱스 설정 코드</summary>

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
</details>


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

#### 종합 결론



<details>
<summary>JPA 엔티티 인덱스 설정 코드</summary>

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
</details>


### PaymentTransaction


#### 단일 인덱스 검토


1. **userId**
  - **필요성**: 특정 사용자의 모든 결제 내역을 조회할 때 사용된다. 사용자의 결제 내역을 확인할 때 자주 사용된다.
  - **카디널리티**: 높음 (각 사용자마다 고유 값)
  - **변경 빈도**: 낮음 (결제 생성 시 설정되며 이후 변경되지 않음)

2. **targetId**
  - **필요성**: 특정 예약, 상품, 서비스 등 다양한 분야의 결제 내역을 조회할 때 사용된다.
  - **카디널리티**: 높음 (다양한 타겟 ID 사용)
  - **변경 빈도**: 낮음 (결제 생성 시 설정되며 이후 변경되지 않음)

#### 복합 인덱스 검토

1. **userId, targetId**
  - **필요성**: 특정 사용자가 특정 타겟에 대해 결제한 내역을 조회할 때 유용하다. 결제 내역 조회, 결제 상태 확인 등에서 자주 사용된다.
  - **카디널리티**: 중간 (두 컬럼의 조합으로 유일한 식별자가 됨)
  - **변경 빈도**: 낮음 (결제 생성 시 설정되며 이후 변경되지 않음)

2. **userId, paymentStatus**
  - **필요성**: 특정 사용자의 특정 결제 상태를 조회할 때 유용하다. 결제 상태 별로 사용자의 결제 내역을 확인할 때 자주 사용된다.
  - **카디널리티**: 중간 (결제 상태 값은 제한적이므로 유니크하지 않음)
  - **변경 빈도**: 중간 (결제 상태는 자주 변경될 수 있음)


#### 종합 결론

`userId, targetId`에 대한 복합 인덱스는 자주 사용되는 쿼리의 성능을 최적화한다.




<details>
<summary>JPA 엔티티 인덱스 설정 코드</summary>

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


</details>





## BEFORE & AFTER

인덱스 적용 전과 후를 비교한다.


### 잔액 조회 API


<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

`Balance` 테이블에서 `userId`로 특정 사용자의 잔액 정보를 조회하는 쿼리

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

</details>



<details>
<summary>Before</summary>


```sql
1,SIMPLE,b1_0,,ALL,,,,,2378402,10,Using where
```

```sql
-> Filter: (b1_0.user_id = 123)  (cost=248638 rows=237840) (actual time=18.4..2647 rows=1 loops=1)
    -> Table scan on b1_0  (cost=248638 rows=2.38e+6) (actual time=16.4..2466 rows=2.65e+6 loops=1)
```

</details>



<details>
<summary>After</summary>


```sql
1,SIMPLE,b1_0,,ref,idx_user_created_at,idx_user_created_at,9,const,1,100,
```

```sql
-> Index lookup on b1_0 using idx_user_created_at (user_id=123)  (cost=1.1 rows=1) (actual time=18.4..18.4 rows=1 loops=1)
```

</details>


#### 결과 분석

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


<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

`BalanceTransaction` 테이블에서 `userId`로 특정 사용자의 잔액 거래 내역을 조회하는 쿼리

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


</details>



<details>
<summary>Before</summary>

```sql
1,SIMPLE,bt1_0,,ALL,,,,,1991966,10,Using where
```

```sql
-> Filter: (bt1_0.user_id = 123)  (cost=207496 rows=199197) (actual time=8.98..2675 rows=2 loops=1)
    -> Table scan on bt1_0  (cost=207496 rows=1.99e+6) (actual time=8.37..2492 rows=2e+6 loops=1)
```

</details>



<details>
<summary>After</summary>

```sql
1,SIMPLE,bt1_0,,ref,"idx_user_created_at,idx_user_transaction_type",idx_user_created_at,9,const,2,100,
```

```sql
-> Index lookup on bt1_0 using idx_user_created_at (user_id=123)  (cost=2.2 rows=2) (actual time=27..27.1 rows=2 loops=1)
```

</details>




#### 결과 분석

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



<details>
<summary>EXPLAIN & EXPLAIN ANALYZE</summary>

`payment_transaction` 테이블에서 `user_id`로 특정 사용자의 결제 트랜잭션을 조회하는 쿼리

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
</details>



<details>
<summary>Before</summary>

```sql
1,SIMPLE,pt1_0,,ALL,,,,,915320,10,Using where
```

```sql
-> Filter: (pt1_0.user_id = 123)  (cost=95987 rows=91532) (actual time=23.2..797 rows=1 loops=1)
    -> Table scan on pt1_0  (cost=95987 rows=915320) (actual time=21.9..750 rows=1e+6 loops=1)
```

</details>



<details>
<summary>After</summary>

```sql
1,SIMPLE,pt1_0,,ref,idx_user_target,idx_user_target,9,const,1,100,
```

```sql
-> Index lookup on pt1_0 using idx_user_target (user_id=123)  (cost=0.817 rows=1) (actual time=8.63..8.64 rows=1 loops=1)
```

</details>






#### 결과 분석

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




<details>
<summary><b> MSA 아키텍처에서 느슨한 결합 추구하기</b></summary>


# MSA 아키텍처에서 느슨한 결합 추구하기

이 글에서는 MSA 기반의 아키텍처에서 느슨한 결합을 추구하는 방법을 살펴본다.
그 방법론 중 하나인 이벤트 드리븐 아키텍처(EDA)를 중심으로 현 프로젝트에서 어떻게 적용하였는지를 주로 다룬다.
또한 트랜잭션이 분리된 서비스 간의 논리적인 일관성을 보장하기 위한 방법론도 함께 다룬다.

먼저 EDA가 무엇인지 설명하고, 현재의 대기열 기반 예약 시스템에 EDA를 적용하여 느슨한 결합을 구현하는 방법을 살펴보자.

## 이벤트 드리븐 아키텍처(EDA)란?

이벤트 드리븐 아키텍처(Event-Driven Architecture)는 시스템에서 발생하는 이벤트를 기반으로 동작하는 소프트웨어 설계 패턴이다.
그렇다면 이벤트란 무엇일까? 이벤트는 특정 동작이나 상태 변화를 나타낸다. 이벤트를 쉽게 이해하는 방법은 반대되는 개념을 살펴보는 것이다.
EDA와 반대되는 동기 통신에서는 한 구성 요소가 다른 구성 요소에 요청을 보내고 응답을 기다린다. 이 방식은 강한 결합이다. 한 모듈의 변경이 다른 모듈에 영향을 미치고, 응답을 기다리는 동안 다른 작업을 수행하지 못한다. 장애가 전파될 가능성도 있다. 동기 통신의 예로는 REST API 호출과 RPC가 있다.

EDA는 이벤트를 발행하고 소비하는 방식으로 작동하며, 비동기 통신을 통해 이러한 문제를 해결한다.
예를 들어, 사용자가 예약을 완료하면 예약 완료 이벤트가 발행되고, 이를 구독한 모듈들이 후속 작업을 수행한다.

## 느슨한 결합이란 게 왜 필요할까?


느슨한 결합(Loose Coupling)은 시스템의 구성 요소 간 의존성을 최소화하여 독립적으로 변경하고 확장할 수 있도록 하는 설계 원칙이다.
예를 들어, 예약 시스템에서 대기열과 예약 API가 밀접하게 결합되어 있다면, 한 부분의 변경이 다른 부분에 큰 영향을 미칠 수 있다.

앞서 EDA와 반대되는 동기 통신을 살펴보았다.
동기 통신에서는 한 구성 요소가 다른 구성 요소에 직접 의존하여 변경 시 상호 영향이 크다. 왜 그럴까? 동기 통신에서는 한 구성 요소가 요청을 보내고 다른 구성 요소가 응답을 반환할 때까지 기다리기 때문이다. 이 과정에서 요청과 응답이 밀접하게 연관되어 있어, 한 쪽의 변경이 다른 쪽의 동작에 즉각적인 영향을 미치게 된다. 예를 들어, API 계약이 변경되면 이를 호출하는 모든 클라이언트도 함께 변경되어야 한다. 이는 시스템의 유연성을 떨어뜨리고, 장애가 전파될 가능성을 높인다.

반면 EDA를 통해 느슨한 결합을 구현하면, 구성 요소 간 의존성을 줄여 변경 시 영향을 최소화할 수 있다. EDA에서는 이벤트를 통해 간접적으로 통신하므로, 한 구성 요소의 변경이 다른 구성 요소에 직접적인 영향을 미치지 않는다. 이벤트 발행자는 이벤트를 발행하기만 하면 되고, 이벤트 수신자는 이를 구독하여 처리한다. 이러한 방식으로 각 구성 요소가 독립적으로 동작한다. 따라서 시스템의 유연성과 확장성이 향상된다.


## 현재 시스템 아키텍처

현재 대기열 기반 예약 시스템의 아키텍처를 살펴보자. 두 파트로 나뉜다.

1. 대기열 부분
2. 예약 API 부분

![대기열 기반의 예약 시스템 전체 아키텍처 AS-IS - note.png](..%2Farchitecture%2F%EB%8C%80%EA%B8%B0%EC%97%B4%20%EA%B8%B0%EB%B0%98%EC%9D%98%20%EC%98%88%EC%95%BD%20%EC%8B%9C%EC%8A%A4%ED%85%9C%20%EC%A0%84%EC%B2%B4%20%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98%20AS-IS%20-%20note.png)


이 두 부분은 서로 독립적으로 동작하지만, 통신이 필요한 경우도 존재한다. 예를 들어 사용자가 예약을 완료하면, 대기열에서 해당 사용자를 제거해야 한다. 위의 그림에서는 이러한 요구 사항을 카프카를 이용한 이벤트 드리븐 프로세스를 이용해 풀어내고 있는 부분을 볼 수 있다.




다음 파트에서는 먼저 마이크로 서비스 간의 EDA를 탐구해보고, 하나의 서비스 내에서의 EDA를 살펴본다.




# 마이크로 서비스 간 EDA 적용 사례

## 어디서 어떻게? ▸ 결제 완료와 활성 토큰 제거 프로세스


앞서 설명한 것처럼, 시스템은 두 파트로 나뉘어 운영된다. 마이크로서비스 아키텍처에서도 동일하게 큐 관련 부분과 API 부분으로 구성된다. API 요청은 오케스트레이션 계층에서 클라이언트 요청을 받아, 이를 적절한 도메인 API 서버에 전달하는 방식으로 처리된다.

활성 토큰을 보유한 유저들은 예약 시스템의 API를 통해 요청을 할 수 있다. 예약 시스템 API는 활성 토큰을 검증하여 유효한 경우에만 API 사용을 허용한다. 유저의 요청이 승인되면, 임시 예약이 생성되어 좌석을 선점하게 된다. 이때 유저는 일정 시간 내에 결제를 완료해야 하며, 결제가 완료되면 예약이 확정된다. 예약이 확정되면 해당 유저의 활성 토큰을 제거해야 한다. 이는 대기열의 다음 순번 유저가 활성 토큰을 부여받아 서버에 접근할 권한을 얻도록 하기 위함이다.

이 과정에서 중요한 점은 결제 완료 상태를 큐 시스템에 알려주는 것이다.

이때, 결제 완료를 큐 시스템 부분에 알려주어야 한다. 어떻게 알려주어야 할까?


## 트랜잭션 처리의 한계

첫 번째 방법은 동기적으로 결제 완료를 큐 시스템에 알려주는 것이다. 이렇게 하면 결제 서비스가 결제 완료 후 즉시 큐 시스템에 활성 토큰 제거를 요청하게 된다. 그러나 이 접근 방식에는 몇 가지 문제가 있다.

먼저, 결제 요청과 활성 토큰 제거 액션이 강하게 결합된다. 이는 한쪽의 실패가 전체 트랜잭션에 영향을 미칠 수 있음을 의미한다. 예를 들어, 유저의 결제가 정상적으로 진행되고 예약이 확정되었지만, 큐 시스템에서 활성 토큰 제거 과정에서 오류가 발생한다면 어떻게 될까? 이 경우, 이미 완료된 결제와 예약이 모두 롤백될 수 있다. 이렇게 되면 유저는 결제가 완료되었음에도 불구하고 예약이 취소되는 불편한 상황에 직면하게 된다. 이는 결제 시스템의 신뢰성을 떨어뜨리고 사용자 경험을 저해할 수 있다.

![synchronous-process-problem1.png](..%2Fdiagram%2Fsynchronous-process-problem1.png)



또한, 동기적 처리 방식은 성능 문제를 초래할 수 있다. 결제 서비스가 큐 시스템의 응답을 기다리는 동안, 다른 작업을 처리하지 못해 전체 시스템의 응답 시간이 길어질 수 있다. 이는 특히 많은 사용자가 동시에 예약을 시도하는 상황에서 심각한 성능 저하로 이어질 수 있다.

이 문제를 해결하는 방법으로 이벤트 드리븐 아키텍처를 활용할 수 있다.



## 이벤트 드리븐 아키텍처의 적용

결제가 완료되면 API 오케스트레이션 서비스는 "결제 완료" 이벤트를 발행한다. 큐 시스템은 이 이벤트를 구독하여 해당 유저의 활성 토큰을 제거하고, 대기열의 다음 유저에게 새로운 활성 토큰을 발급한다. 이렇게 하면 결제와 예약이 독립적으로 처리되어 한쪽의 실패가 다른 쪽에 영향을 미치지 않게 된다.

이벤트 드리븐 아키텍처를 적용함으로써 우리는 트랜잭션 처리의 한계를 극복할 수 있다. EDA는 비동기적으로 이벤트를 처리하여 시스템 구성 요소 간 결합도를 낮추고, 유연성과 확장성을 높이는 데 중점을 둔다. 결제 완료와 활성 토큰 제거 과정에 EDA를 적용하면 다음과 같은 이점을 얻을 수 있다.

- 비동기 이벤트 처리는 결제와 활성 토큰 제거를 분리하여 한쪽의 장애가 다른 쪽에 영향을 미치지 않게 한다.
- 결제 완료 후 큐 시스템 응답을 기다리지 않고 즉시 다음 작업을 수행하여 성능을 향상시킨다.
- 이벤트 기반 시스템은 결제 완료 이벤트를 구독하여 새로운 기능 추가와 확장을 유연하게 할 수 있다.


### 플로우

```mermaid
graph TD
  subgraph API Orchestration Service
    A[User Initiates Payment] --> B[Process Payment]
    B -->|async| C[Publish Payment Completed Event]
  end
  
  subgraph Queue Management Service
    C -->|async| D[Receive Payment Event]
    D --> E[Remove Active Token]
    E --> F[Issue New Token to Next User]
  end

```

```mermaid
sequenceDiagram
    participant User as 유저
    participant API as API 오케스트레이션
    participant PaymentService as 결제 서비스
    participant Kafka as 카프카
    participant QueueService as 큐 시스템

    User->>API: 결제 요청
    API->>PaymentService: 결제 처리
    PaymentService->>API: 결제 완료 (성공)

    API-->>Kafka: "결제 완료" 이벤트 발행
    QueueService-->>Kafka: 결제 완료 이벤트 수신
    QueueService->>QueueService: 활성 토큰 제거 및 다음 유저 활성 토큰 발급
```



### 코드 구현

1. **결제 처리 및 이벤트 발행**:
  - 결제가 완료되면, 결제 서비스는 결제 상태를 업데이트하고 "결제 완료" 이벤트를 발행한다.
  - `applicationEventPublisher.publishEvent(paymentResponse.toPaymentInternalEventAsComplete());`

2. **이벤트 핸들러**:
  - 이벤트 핸들러는 발행된 이벤트를 수신하고 이를 처리한다.
  - 결제 완료 이벤트를 수신하면, 큐 시스템에 메시지를 전송하여 활성 토큰을 제거하고 새로운 토큰을 발급한다.
  - `streamBridge.send("concert-reservation-payment", messageBuilder.build());`

3. **큐 시스템에서의 처리**:
  - 큐 시스템은 수신된 메시지를 처리하여 활성 토큰을 제거하고, 다음 사용자에게 새로운 토큰을 발급한다.
  - `@EventListener QueuePaymentEventHandler.handlePaymentCompleteEvent(PaymentMessagePayload event);`
  - `processingQueueFacade.completeToken(event.toCompletedTokenHandlingRequest());`



```java
// API Orchestration Service의 발행 부분

public class KafkaMessageProducingInternalEventHandler {
	public void handlePaymentInternalEvent(PaymentInternalEvent event) {
			PaymentMessagePayload payload = PaymentMessagePayload.of(event.getPaymentTransactionId(), event.getUserId(), event.getPaymentType());

			MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(objectMapper.writeValueAsString(payload))
					.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
						.setHeader("payment-type", payload.getPaymentType());
			streamBridge.send("concert-reservation-payment", messageBuilder.build());
	}
}
```


```java
// Queue Management Service의 컨슘 부분 

public class QueuePaymentEventHandler {

	@EventListener
	public void handlePaymentCompleteEvent(PaymentMessagePayload event) {
		processingQueueFacade.completeToken(event.toCompletedTokenHandlingRequest());
	}
}
```



# 서비스 내에서의 EDA 탐구

본 섹션에서는 각 서비스 내에서 내부 이벤트가 사용되는 부분을 살펴보자.

## 1. 결제 완료 후 카프카 메시지를 발행하는 로직

첫 번째는 결제 완료 이벤트를 발행하는 부분이다. 오케스트레이션 계층에서 결제 프로세스를 주관한다. 결제 서비스로부터 완료 응답을 받으면 이를 카프카 메시지로 발행한다. 이때 책임 분리를 위해 내부 이벤트를 발행한다.

코드를 살펴보면 다음과 같다.

```java
public class PaymentFacade {

	public PaymentResponse processPayment(PaymentProcessRequest request) {
        // ...
			reservationService.confirmReservation(createConfirmCommand(request));
		// ... 
		// 내부 이벤트 발행		
        applicationEventPublisher.publishEvent(paymentResponse.toPaymentInternalEventAsComplete());
		return paymentResponse;
	}
}
```

왜 굳이 내부 이벤트를 발행하는 것일까? Facade에서 바로 카프카 메시지를 발행해도 될 텐데 말이다. 여기에는 두 가지 이유가 있다.

1. **클린 아키텍처 관점에서 파사드의 책임이 아님**:
  - 파사드는 비즈니스 로직을 호출하고 결과를 반환하는 역할을 담당한다. 외부 시스템과의 통신은 인프라스트럭처 레이어의 책임이므로, 파사드에서 직접 카프카 메시지를 발행하는 것은 클린 아키텍처 원칙에 어긋난다. 내부 이벤트를 발행하여 이 작업을 인프라스트럭처 레이어로 위임하면 책임이 명확히 분리된다.

2. **비동기 프로세스로 분리하여 결합도와 효율성 향상**:
  - 결제 완료 시점에서 바로 카프카 메시지를 발행한다면, 발행 자체가 비즈니스 로직과 결합된다. 비동기 이벤트를 통해 처리하면 결제 서비스와 카프카 발행 로직 간의 결합도를 낮출 수 있다. 물론, 어플리케이션 이벤트를 발행한다는 측면에서 결합은 불가피하긴 하다. 하지만 카프카 메시지 발행 로직은 외부 시스템과의 연동이므로 외부와의 의존성이 있는 반면, 어플리케이션 이벤트는 자체 시스템에서 동작하므로 의존성이 적다. 따라서 이를 비동기적으로 처리하면, 결제 로직에 대한 응집성을 높일 수 있다.

결제 완료 후 발행한 내부 이벤트는 다음과 같이 `KafkaMessageProducingInternalEventHandler`에서 처리된다.

```java
public class KafkaMessageProducingInternalEventHandler {

	@EventListener
	public void handlePaymentInternalEvent(PaymentInternalEvent event) {

// ... 
			MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(objectMapper.writeValueAsString(payload))
					.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
					.setHeader("payment-type", payload.getPaymentType());
			streamBridge.send("concert-reservation-payment", messageBuilder.build());
// ... 	
}
```

`PaymentInternalEvent`를 수신하여 카프카 메시지를 발행한다.

## 2. 예외 처리 로직에서 알림 프로세스

예외를 핸들링 하는 부분에서도 내부 이벤트를 사용할 수 있다.
본 프로젝트에서는 스프링 AOP(Aspect-Oriented Programming)를 활용하여, 특정 포인트컷에서 발생하는 예외를 포착하고 이를 처리하고 있다. 이때, 내부 이벤트를 통해 예외 정보를 전달하고, 알림을 발행하는 과정을 살펴보자.

예외가 발생하면 `GlobalExceptionAlertAspect` 클래스가 이를 포착하여 내부 이벤트를 발행한다. 이 이벤트는 `GlobalExceptionAlertInternalListener`에서 처리되어 슬랙 메시지 등의 알림을 발송한다.

```java
@Aspect
public class GlobalExceptionAlertAspect {

	private final GlobalExceptionAlertInternalPublisher globalExceptionAlertInternalPublisher;

	// ... 

	@AfterThrowing(pointcut = "apiMethods() && notAsync()", throwing = "throwable")
	public void alertApplicationWideMethods(JoinPoint jp, Throwable throwable) {
		if (!isMarkedException(throwable)) {
			globalExceptionAlertInternalPublisher.publish(new GlobalExceptionAlertEvent(jp.getSignature().getName(), throwable));
			markException(throwable);
		}
	}
    // ...
}
```

이 코드에서 `GlobalExceptionAlertAspect`는 특정 포인트컷에서 발생한 예외를 포착하여 `GlobalExceptionAlertEvent`를 발행한다.

```java
public class GlobalExceptionAlertInternalPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;

	public void publish(GlobalExceptionAlertEvent alertEvent) {
		applicationEventPublisher.publishEvent(alertEvent);
	}
}
```

`GlobalExceptionAlertInternalPublisher`는 포착된 예외 정보를 내부 이벤트로 발행한다. 이 내부 이벤트는 `GlobalExceptionAlertInternalListener`에서 처리된다.

```java
public class GlobalExceptionAlertInternalListener {
	private final MessageServiceClientAdapter messageServiceClientAdapter;

	@Async
	@EventListener
	public void handleGlobalExceptionAlert(GlobalExceptionAlertEvent event) {
			messageServiceClientAdapter.reserveSlackMessage(requestByGlobalExceptionAlertEvent(event));
	}
}
```

이 클래스는 `GlobalExceptionAlertEvent`를 수신하여 슬랙 메시지를 발송한다. 비동기 프로세스를 통해 별도의 스레드에서 백그라운드로 알림을 보내도록 한다.

여기서도 마찬가지로 내부 이벤트를 사용하여 예외를 처리하는 이점은 다음과 같다.

1. **책임 분리**: 예외 처리 로직과 알림 발송 로직을 분리하여, 각 책임을 명확히 한다.
2. **비동기 처리**: 예외 발생 시 비동기적으로 알림을 발송하여, 주 비즈니스 로직의 성능과 응답 시간을 저하시키지 않고, 비동기 스레드를 통해 어플리케이션의 유휴 자원을 최대한 효율적으로 활용한다.
3. **유연한 확장성**: 새로운 알림 채널이나 처리 로직을 추가할 때, 기존 코드를 수정할 필요 없이 이벤트 리스너를 추가하는 방식으로 쉽게 확장할 수 있다.




## 3. Cache Eviction 전략에서의 내부 이벤트 활용

이와 비슷한 구현으로 로컬 캐시 Eviction을 위해 인스턴스별 동기화 프로세스를 진행할 때도 내부 이벤트를 활용한다.

캐시 Eviction 전략에서, Eviction이 발동되는 조건은 특정 API를 통한 변경도 있지만, 포괄적으로 도메인 엔티티 자체가 변경되는 것을 전부 포함할 수도 있다. 이럴 때 엔티티 변경 자체를 전부 추적하는 방법으로 도메인 이벤트 발행 패턴을 사용할 수 있다.

도메인 이벤트 발행 패턴은 스프링의 `AbstractAggregateRoot`를 이용하여 구현할 수 있다. `AbstractAggregateRoot`는 도메인 엔티티에서 발생하는 이벤트를 쉽게 관리할 수 있도록 도와준다. 이를 통해 엔티티 변경을 감지하고 필요한 이벤트를 발행할 수 있다.

다음과 같은 구현을 살펴보자.

```java
public class PaymentTransaction extends AbstractAggregateRoot<PaymentTransaction> {
    // ... 생략 

	@PostPersist
	@PostUpdate
	@PostRemove
	private void publishSeatChangedEvent() {
		registerEvent(new LocalCacheEvictEvent("paymentTransaction", String.valueOf(userId)));
	}
}
```

`PaymentTransaction` 클래스는 `AbstractAggregateRoot`를 상속받아 도메인 이벤트를 관리한다. `@PostPersist`, `@PostUpdate`, `@PostRemove`와 같은 JPA 엔티티 라이프사이클 이벤트를 통해, 엔티티가 생성되거나 업데이트되거나 삭제될 때 `publishSeatChangedEvent` 메서드가 호출된다. 이 메서드가 `LocalCacheEvictEvent`를 등록하면 어떤 일이 벌어질까?

`AbstractAggregateRoot`는 내부적으로 이벤트를 수집하고 관리하는 기능을 제공한다. `registerEvent` 메서드를 호출하면, `AbstractAggregateRoot`는 이를 내부 리스트에 저장한다. 이후 트랜잭션이 성공적으로 완료되면, 스프링의 이벤트 퍼블리셔(ApplicationEventPublisher)를 통해 이 이벤트가 발행된다.

이 과정을 통해, `PaymentTransaction` 엔티티의 상태가 변경될 때마다 `LocalCacheEvictEvent`가 자동으로 발행된다. 이후 부터는 다음과 같은 리스너가 해당 이벤트를 받게 되므로 비동기 처리 프로세스로서 캐시 만료 전략을 수행하게 된다.

```java
public class CacheEvictEventListener {

	private final CacheEvictionPropagationService cacheEvictionPropagationService;

	@TransactionalEventListener
	@Async
	public void handleLocalCacheEvictEvent(LocalCacheEvictEvent event) {
		try {
			cacheEvictionPropagationService.propagateCacheEviction(PropagateCacheEvictionCommand.of(event.getCacheName(), event.getKey()));
		} catch (Exception e) {
			log.error("Failed to evict cache for LocalCacheEvictEvent: {} - {} - {}", event.getCacheName(),event.getKey(), e.getMessage());
		}
	}
}
```  

여기서의 캐시 Eviction 전략은 인스턴스별로 유레카 서비스를 통해 셀프 호출을 하는 프로세스이다. 한 인스턴스에서 캐시 Eviction이 발생할 때 다른 인스턴스들까지 동기화하기 위해 cacheEvictionPropagationService를 통해 재귀 호출을 한다.

내부 이벤트 발행을 통해 이러한 동기화 작업을 느슨한 결합을 달성한 것을 볼 수 있다.

한편, 해당 프로세스는 redis pub/sub을 이용하는 것으로 구현할 수도 있다. redis pub/sub은 각 인스턴스가 메시지를 구독하고 처리하여 캐시 동기화를 효율적으로 수행할 수 있는 방법을 제공한다. 이를 이용하는 것도 EDA 기반 느슨한 결합의 좋은 예라고 할 수 있을 것이다.


## 4. balance transaction 처리에서의 내부 이벤트 활용

마지막으로, 잔액 처리 프로세스에서 내부 이벤트를 사용할 수 있다.

balance 충전과 사용 로직을 생각해보자. 유저의 액션 히스토리가 필요한 상황, 즉 트랜잭션 히스토리가 기록되어야 한다. 이 요구 사항은 원자적인 니즈가 있는 요구사항일까?

![payment-and-record-transaction.png](..%2Farchitecture%2Ftransaction%2Fpayment-and-record-transaction.png)





경우에 따라 다를 수도 있겠지만 대부분의 금융 애플리케이션에서 원자적인 요구 사항일 것이다. 왜냐하면, 정확한 거래 내역을 유지하는 것은 회계적 정확성, 사용자 신뢰성, 그리고 법적 규제를 준수하기 위해 필수적이기 때문이다.

가장 쉬운 방법은 동기적으로 이 문제를 해결하는 것이다. 다음은 동기적으로 처리하는 로직의 예시다.


```java
public class BalanceCharger {

	@Transactional
	public BalanceChargeInfo charge(BalanceChargeCommand command) {
		return balanceRepository.findSingleByConditionOptionalWithLock(onUser(command))
			//... 메인 로직 
			.map(savedBalance -> {
				transactionRepository.save(BalanceTransaction.createChargedEvent(new BalanceChargeEvent(savedBalance.getUserId(), command.getAmount(), command.getTransactionReason())));
				return BalanceChargeInfo.from(savedBalance);
			})
			//... 
	}
}
```

```java
public class BalanceUseManager {

	@Transactional
	public BalanceUseInfo use(BalanceUseCommand command) {
		// ... 메인 로직 
		transactionRepository.save(BalanceTransaction.createUsedEvent(new BalanceUseEvent(savedBalance.getUserId(), command.getAmount(), command.getTransactionReason())));
		// ...
	}
}

```
위의 예시에서 보듯이, 동기적으로 처리하는 방식은 단순하고 직관적이다. 하지만 앞서 계속 살펴보았듯, 높은 결합도는 여러 문제로 이어질 수 있다.

바로 이 지점에서 도메인 이벤트 발행을 사용할 수 있다. 도메인 엔티티에서 `AbstractAggregateRoot`를 이용해 잔액 충전과 사용 이벤트를 각각 등록해주는 것이다.


`AbstractAggregateRoot`를 사용하면 도메인 엔티티에서 발생하는 이벤트를 쉽게 관리할 수 있다. `Balance` 클래스는 `AbstractAggregateRoot`를 상속받아 잔액 충전과 사용 시 각각의 이벤트를 등록한다.

```java
@Entity
public class Balance extends AbstractAggregateRoot<Balance> {

	// ... 

	public Balance charge(BigDecimal amount, TransactionReason transactionReason) {
		// ... 
		this.registerEvent(new BalanceChargeEvent(this.userId, amount, transactionReason));
		return this;
	}

	public Balance use(BigDecimal amount, TransactionReason transactionReason) {
		// ...
		this.registerEvent(new BalanceUseEvent(this.userId, amount, transactionReason));
		return this;
	}
}
```

위의 코드에서 `Balance` 클래스는 잔액 충전 시 `BalanceChargeEvent`, 잔액 사용 시 `BalanceUseEvent`를 각각 등록한다. `registerEvent` 메서드를 통해 이벤트가 등록되면, `AbstractAggregateRoot`가 이를 관리한다.

발생하는 이벤트는 `BalanceTransactionEventListener`에서 받아서 처리한다.

```java
public class BalanceTransactionEventListener {

	@TransactionalEventListener(phase = BEFORE_COMMIT)
	public void handleBalanceChargedEvent(BalanceChargeEvent event) {
		transactionRepository.save(BalanceTransaction.createChargedEvent(event));
	}

	@TransactionalEventListener(phase = BEFORE_COMMIT)
	public void handleBalanceUsedEvent(BalanceUseEvent event) {
		transactionRepository.save(BalanceTransaction.createUsedEvent(event));
	}
}
```

`BalanceTransactionEventListener`는 `BalanceChargeEvent`와 `BalanceUseEvent`를 처리하여 트랜잭션을 저장한다.

여기서 `@TransactionalEventListener(phase = BEFORE_COMMIT)`을 사용한 이유는, 이벤트가 트랜잭션이 커밋되기 전에 처리되어야 하기 때문이다. 이는 트랜잭션이 성공적으로 완료되기 전에 이벤트를 처리하여 데이터 일관성을 보장한다.


![충전과 충전 기록이 같은 트랜잭션으로 묶되, 도메인 이벤트를 사용하는 경우.png](..%2Farchitecture%2Ftransaction%2F%EC%B6%A9%EC%A0%84%EA%B3%BC%20%EC%B6%A9%EC%A0%84%20%EA%B8%B0%EB%A1%9D%EC%9D%B4%20%EA%B0%99%EC%9D%80%20%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98%EC%9C%BC%EB%A1%9C%20%EB%AC%B6%EB%90%98%2C%20%EB%8F%84%EB%A9%94%EC%9D%B8%20%EC%9D%B4%EB%B2%A4%ED%8A%B8%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94%20%EA%B2%BD%EC%9A%B0.png)

이러한 방식으로 도메인 이벤트 발행을 통해 잔액 처리 로직을 간단하고 명확하게 유지하면서도, 트랜잭션 히스토리를 비동기적으로 처리하여 유연한 아키텍처를 구성할 수 있다.





# 추가 개선 포인트 탐구


## 1. 대기열 관리를 한 서비스에서 담당하지 말고 쪼갠다

현재는 `queue-management-service`에서 대기열 인입, 카운터를 이용한 처리열 계수, 처리열 인입 등 대기 토큰과 처리 토큰을 분리하지 않고 전부 다루고 있다.

![대기열 asis 부분 확대.png](..%2Farchitecture%2Fqueue%2F%EB%8C%80%EA%B8%B0%EC%97%B4%20asis%20%EB%B6%80%EB%B6%84%20%ED%99%95%EB%8C%80.png)

이 부분을 분리할 수 있다. 어떻게 할 수 있을까?

대기열 관리를 다음과 같이 세 개의 서비스로 분리할 수 있다.`waiting-queue-service`, `active-queue-service`, `expiration-service`.

### 서비스 분리

- **Waiting-Queue-Service**:
  - 대기 토큰만 다룬다.
  - 대기 토큰을 인입시키고, 해당 토큰에 대한 인입 이벤트를 발행하는 것으로 책임을 다한다.

- **Active-Queue-Service**:
  - 활성 토큰만 다룬다.
  - 대기 토큰 이벤트를 수신하면, 활성 토큰을 넣을 수 있을지를 카운터를 통해 판단한다.
  - 가능하다면 활성 토큰을 넣고, 활성 토큰에 대한 이벤트를 발행한다.

- **Expiration-Service**:
  - 활성 토큰에 대한 이벤트를 받는다.
  - 해당 이벤트에 따라 TTL 여부를 체크하고, 카운터 값을 동기화해준다.

### 결론

대기열 관리를 각기 다른 서비스로 분리함으로써 책임을 명확히 할 수 있다. Redis와 Kafka를 이용해 이벤트 드리븐을 활용하면 이 방식에서의 최선 전략을 구현할 수 있다.

![대기열 tobe 부분 확대 .png](..%2Farchitecture%2Fqueue%2F%EB%8C%80%EA%B8%B0%EC%97%B4%20tobe%20%EB%B6%80%EB%B6%84%20%ED%99%95%EB%8C%80%20.png)


보다 자세한 내용은 여기서 볼 수 있다.



## 2. 유저의 대기열 정보 요청을 기존 polling에서 websocket으로의 변경 탐구


현재 유저가 대기열 요청을 할 때는 polling 방식으로 본인의 위치 정보 등을 요청하고 있다. 해당 요청을 처리하는 서비스는 `client-channel-service`이다. 현재는 polling 요청에 1:1로 대응하는 response를 제공하며, 이때 토큰 정보를 `queue-service`에 물어봐서 제공하고 있다.

이벤트를 이용해서 개선해 볼 수 있을까?

### 이벤트를 이용한 개선 가능성 탐구

개선된 아키텍처에서는 `waiting-queue-service`와 `active-queue-service`로 구분되어 각각 대기 토큰과 활성 토큰을 담당한다. 각 서비스가 대기 토큰 및 활성 토큰을 발행하면, 이를 `client-channel-service`에서 소비하고 해당 토큰들에 대해 WebSocket을 통해 클라이언트에게 실시간으로 알린다.

이런 방식이 가능할까? 여기에 몇 가지 문제점이 있다:

1. **토큰 정보의 실시간 변경**:
  - 토큰 정보는 실시간으로 변경된다. 동적으로 변경될 때마다 그 정보를 최신화하려면, 지속적인 이벤트 발행이 필요하다.
  - 중복 데이터를 잘 필터링하고, 최신 토큰 정보를 정확하게 전달하는 구현이 복잡하다. 또한, 이벤트 발행 비용이 증가할 수 있어 효율성에 대한 의문이 생긴다.

2. **개인화된 정보 제공의 어려움**:
  - WebSocket을 통해 정보를 브로드캐스트하면 부하는 줄일 수 있지만, 개인별로 토큰 정보를 알고자 하는 니즈를 어떻게 만족시킬 것인가에 대한 문제가 있다.
  - 클라이언트가 모든 정보를 받아 필터링하는 방식이 가능할까? 개인화된 정보를 실시간으로 제공하는 방법을 찾는 것이 과제이다.


### 해결 방안 탐구

이러한 문제점을 해결하기 위해 다음과 같은 방안을 고려해본다.

1. **최신화된 정보 제공**:
  - WebSocket을 사용하여 각 클라이언트에게 최신 정보를 푸시한다.
  - `waiting-queue-service`와 `active-queue-service`에서 토큰 정보가 변경될 때마다 해당 정보를 `client-channel-service`에 전달하고, 이 서비스가 각 클라이언트에게 실시간으로 업데이트된 정보를 보낸다.

2. **개인화된 정보 제공**:
  - 각 클라이언트는 자신의 고유 토큰 ID를 구독하고, `client-channel-service`는 해당 클라이언트에게만 관련 정보를 전송한다.
  - 이를 통해 불필요한 데이터 전송을 줄이고, 개인화된 정보를 제공할 수 있다.



### 결론

WebSocket을 이용하여 유저의 대기열 정보 요청을 처리하는 것은, 여러 클라이언트의 지속적인 polling 요청 부하를 줄이는 데 효과적일 수 있다. 다만, 실시간으로 변경되는 토큰 정보를 효율적으로 관리하고, 개인화된 정보를 제공하는 방식의 구현이 필요하다. 또한 웹소켓 특성상 연결이 지속되는 것에 대한 자원 소모가 오히려 polling 방식보다 더 비효율적일 수 있다.



# 개선 사항을 반영한 아키텍처

![대기열 기반의 예약 시스템 전체 아키텍처 TO-BE.png](..%2Farchitecture%2F%EB%8C%80%EA%B8%B0%EC%97%B4%20%EA%B8%B0%EB%B0%98%EC%9D%98%20%EC%98%88%EC%95%BD%20%EC%8B%9C%EC%8A%A4%ED%85%9C%20%EC%A0%84%EC%B2%B4%20%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98%20TO-BE.png)


# 부록: 이벤트 방식의 문제와 해결 방안

마이크로 서비스 아키텍처에서는 서비스에 대한 분리가 핵심이다. 이것의 장점으로는 각 서비스가 독립적으로 동작할 수 있어 책임을 나누게 되고, 각 서비스는 자기 할 일에 대해서만 잘하면 된다. 또한, 장애 격리가 가능하여 한 서비스의 문제가 다른 서비스에 영향을 미치지 않는다. 이로 인해 시스템의 안정성과 유지보수성이 크게 향상된다.

그런데 장점만 있을까? 단점도 있다.


## 1. 트랜잭션 분리에 따른 데이터 정합성


마이크로서비스 아키텍처에서는 서비스가 분리되어 있기 때문에 각 도메인 서비스가 자신의 트랜잭션을 책임진다. 이를 중재하기 위한 오케스트레이션이 도입된다. 예를 들어, 결제를 처리하고 예약을 완료하는 기능을 수행하는 API를 살펴보자.



### 예제: 예약 완료 기능

오케스트레이션 서비스가 클라이언트의 요청을 받아 결제 처리, 예약 완료, 토큰 만료 처리를 순차적으로 수행해야 한다. 여기서 결제 처리와 예약 완료는 비즈니스 관점에서 '논리적인 원자성'이 보장되어야 한다.


```mermaid
sequenceDiagram
    participant 클라이언트
    participant 오케스트레이션 서비스
    participant 결제 서비스
    participant 예약 서비스
    participant 토큰 서비스

    클라이언트->>오케스트레이션 서비스: 예약 요청
    오케스트레이션 서비스->>결제 서비스: 결제 요청
    결제 서비스-->>오케스트레이션 서비스: 결제 완료

    alt 결제 성공
        오케스트레이션 서비스->>예약 서비스: 예약 완료 요청
        예약 서비스-->>오케스트레이션 서비스: 예약 완료

        alt 예약 성공
            오케스트레이션 서비스->>토큰 서비스: 토큰 만료 처리 요청
            토큰 서비스-->>오케스트레이션 서비스: 토큰 만료 처리 완료
            오케스트레이션 서비스-->>클라이언트: 예약 완료 응답
        else 예약 실패
            오케스트레이션 서비스->>결제 서비스: 결제 취소 요청
            결제 서비스-->>오케스트레이션 서비스: 결제 취소 완료
            오케스트레이션 서비스-->>클라이언트: 예약 실패 응답
        end
    else 결제 실패
        오케스트레이션 서비스-->>클라이언트: 결제 실패 응답
    end
```

오케스트레이션 서비스가 결제 처리, 예약 완료, 토큰 만료 처리의 순서로 요청을 처리하는 과정을 보여준다. 결제가 성공하면 예약을 시도하고, 예약이 성공하면 토큰을 만료 처리한 후 클라이언트에게 예약 완료 응답을 보낸다. 만약 결제나 예약이 실패하면, 적절한 롤백 작업(결제 취소 등)을 수행하고 실패 응답을 클라이언트에게 보낸다.


#### 모놀리틱 구조 vs. 마이크로서비스 구조

모놀리틱 구조에서는 스프링이나 자바의 트랜잭션 어노테이션을 이용해 쉽게 트랜잭션을 관리할 수 있다. 그러나 마이크로서비스 구조에서는 DB의 원자성을 보장하는 트랜잭션 어노테이션을 사용할 수 없다. 예를 들어, 결제가 완료되었지만 예약이 실패한다면 데이터 정합성이 틀어지는 문제가 발생할 수 있다.


### 해결 방법: 보상 트랜잭션과 사가 패턴

이러한 문제를 해결하기 위해 보상 트랜잭션 또는 사가 패턴을 사용할 수 있다. 사가 패턴은 두 가지 방식으로 접근할 수 있다:

1. **간단한 롤백 로직**:
  - `try-catch` 블록을 사용하여 결제 실패 시 롤백하는 간단한 방식이다. 결제 취소만 처리하면 되므로 구현이 비교적 단순하다.

2. **복잡한 롤백 로직**:
  - 서비스 간의 복잡한 롤백 로직이 필요할 때는 각 서비스가 실패 이벤트를 발행하고, 해당 이벤트를 소비하여 롤백을 처리하거나, 중앙화된 오케스트레이션이 롤백 명령을 내려주는 방식이 있다.

### 현재의 구현: 간단한 `try-catch` 롤백 로직

다음은 `try-catch` 블록을 사용하여 간단한 롤백 로직을 구현한 예제이다.

현재의 구현에서는 다음과 같이 간단한 `try-catch`  블록을 사용한 롤백 로직을 구현했다.


```java
public PaymentResponse processPayment(PaymentProcessRequest request) {
    PaymentResponse paymentResponse = PaymentResponse.from(paymentService.processPayment(request.toCommand()));
    try {
        reservationService.confirmReservation(createConfirmCommand(request));
    } catch (Exception e) {
        paymentService.cancelPayment(paymentResponse.transactionId());
        return createRolledBackResponse(paymentResponse);
    }

    applicationEventPublisher.publishEvent(paymentResponse.toPaymentInternalEventAsComplete());
    return paymentResponse;
}
```

### 결론

위와 같이 MSA에서는 트랜잭션 분리 문제를 신경 써주어야 한다.

트랜잭션 분리에 따른 데이터 정합성 문제를 해결하기 위해 사가 패턴과 보상 트랜잭션을 도입할 수 있다. 간단한 롤백 로직은 try-catch로 구현할 수 있으며, 복잡한 경우에는 중앙 오케스트레이션을 사용하여 실패 이벤트를 관리할 수 있다.





## 2. 이벤트 유실 문제



이벤트를 주고 받을 때 이벤트 유실 문제가 발생할 수 있다. Kafka는 기본적으로 이벤트 유실에 대한 대책을 지원한다. commit 타입 등을 통해 ACK 응답에 대한 옵션을 제공한다. 그러나 어플리케이션까지 이벤트가 도달하는 과정에서는 Kafka의 지원에도 불구하고 어플리케이션에서 이벤트 유실이 발생할 수 있다. 예를 들어, 어플리케이션이 로직을 처리하는 도중 서비스가 셧다운 되거나, ACK를 전송했지만 비즈니스 로직이 실패하는 경우가 있을 수 있다.

여기서는 이 문제를 이전에 다뤄본 경험을 소개한다. 다음과 같은 해결방법이 있다.

1. **Auto-Commit 설정**: `auto-commit`을 `false`로 설정한다.
2. **이벤트 저장**: 이벤트를 수신하면, 이벤트 자체를 먼저 저장한다.
3. **ACK 전송**: 이벤트 저장이 완료되면 그때 ACK를 전송한다.
4. **비즈니스 로직 수행**: 저장된 이벤트에 대한 비즈니스 로직을 수행한다.
5. **이벤트 정리**: 비즈니스 로직이 정상적으로 완료되면, 이벤트를 스케줄링으로 삭제한다.
6. **재실행 처리**: 비즈니스 로직이 중간에 실패하면 해당 이벤트를 다시 꺼내와서 재실행한다.


### 비즈니스 로직이 완료된 후 ACK를 전송하면 안 될까?

비즈니스 로직이 길어지는 경우를 생각해보자. 예를 들어, 다른 서비스를 호출하는 등의 이유로 비즈니스 로직의 레이턴시가 길어지면 ACK 응답이 늦어지게 된다. Kafka의 특성상 메시지를 순차적으로 처리해야 하기 때문에, ACK가 지연되면 다음 메시지가 소비되지 않는다. 이로 인해 메시지가 Kafka 큐에 계속 쌓여 `lag` 현상이 발생할 수 있다.

따라서 유실 방지 메커니즘을 수동으로 구현할 때, 비즈니스 로직을 수행하기 전에 이벤트를 안전하게 저장하고 ACK를 전송하는 방식이 유리하다. 이렇게 하면 Kafka의 이벤트 유실 문제를 해결하면서도, 어플리케이션 레벨에서 발생할 수 있는 유실 문제를 최소화할 수 있다.



## 3. 이벤트 순서 보장 문제


이벤트 기반 아키텍처를 사용할 때 또 한가지 주의점은 이벤트 순서 보장 문제이다.

Kafka는 메시지의 순서를 보장한다. 하지만 이 사실이 어플리케이션 설계상에서 모든 순서 정합성 문제를 자동으로 해결해준다는 것은 아니다. 예를 들어, 특정 메시지가 반드시 다른 메시지보다 먼저 처리되어야 하는 상황에서, 이 메시지들이 서로 다른 토픽에 발행된다면 각 토픽 내에서는 순서가 보장되지만, 전체 비즈니스 로직에서는 순서 정합성 문제가 발생할 수 있다.

이 문제도 실제 경험했던 사례를 소개하며 문제와 해결 과정을 제시하고자 한다.

### 실제 경험 사례

A 서비스가 있고, 이 서비스에서 속성(attribute)을 별도의 서비스에 나누어 저장한다고 가정하자. attribute 서비스가 a-g까지 있다고 치면, A 서비스는 원천 데이터를 저장하고, 그에 딸린 속성들을 저장하라는 이벤트를 발행한다. attribute 서비스는 이 이벤트를 소비하여 속성을 저장하고, A 서비스는 발행과 동시에 메인 데이터에서 속성을 지운다.

그 후, A 서비스는 해당 액션에 대한 완료 이벤트를 발행하고, 여러 마이크로서비스가 이 완료 이벤트를 소비한다. 여기서 문제가 발생할 수 있다.


### 문제 발생 시나리오

논리적으로 A 서비스는 속성 저장 이벤트를 먼저 발행하고, 그 다음에 완료 이벤트를 발행했으므로 발행 순서는 지켜졌다. 그러나 여러 마이크로서비스가 A 서비스의 완료 이벤트를 수신하는 시점에서는 아직 모든 attribute 저장이 완료되지 않았을 수 있다. 이때, 어떤 마이크로서비스가 완료 이벤트를 받자마자 A 서비스에 정보를 조회하면, 기대하는 속성 정보가 아직 저장되지 않아 조회 결과에 포함되지 않을 수 있다. 결과적으로, "이벤트를 받았는데 왜 속성 정보가 없는가?"라는 문제가 발생할 수 있다.



### 해결 방안

이 문제를 해결하기 위해 선택한 방식은 글로벌 캐싱을 이용하는 것이었다. 매우 짧은 TTL(Time To Live)로 레디스에 캐싱하는 플로우를 추가했다. A 서비스는 이벤트를 발행하기 전에 메인 데이터를 캐싱하고, 그 후에 이벤트를 발행하고 데이터를 지운다. 이렇게 하면 다른 서비스들이 단기간 내에 조회를 요청할 때 캐싱된 원천 데이터를 제공하여 일시적인 정합성 불일치 문제를 해결할 수 있다.


1. **데이터 캐싱**: A 서비스는 속성 저장 이벤트를 발행하기 전에 메인 데이터를 글로벌 캐시(예: Redis)에 저장한다.
2. **이벤트 발행**: 메인 데이터를 캐싱한 후 속성 저장 이벤트와 완료 이벤트를 순차적으로 발행한다.
3. **데이터 삭제**: 이벤트 발행 후 메인 데이터에서 속성을 삭제한다.
4. **캐시 응답**: 다른 서비스들이 A 서비스에 조회 요청을 하면, A 서비스는 글로벌 캐시에서 데이터를 조회하여 응답한다.
5. **TTL 설정**: 글로벌 캐시의 데이터는 짧은 TTL을 설정하여 일시적인 정합성 문제를 해결하면서, 캐시의 불필요한 데이터가 오래 남지 않도록 한다.


### 이벤트 드리븐 프로세스에서 순서 문제 해결의 중요성

이 사례를 통해 이야기하고 싶은 것은 이벤트 드리븐 프로세스를 사용할 때 순서 문제는 중요한 고려 사항이라는 점이다. 이벤트 기반 아키텍처에서는 메시지가 정확한 순서로 처리되지 않으면 데이터 정합성이 깨지고, 비즈니스 로직이 올바르게 수행되지 않을 수 있다. 특히, 서로 다른 토픽에 발행된 이벤트들 간의 순서 보장이 필요한지를 잘 고려해야 한다.


## 4. 결론

이와 같은 문제점을 인식하고 적절한 해결 방안을 적용하면, 이벤트 드리븐 아키텍처의 이점을 극대화하면서 안정적이고 확장 가능한 시스템을 구축할 수 있다.






</details>








<details>
<summary><b>장애 대응을 위한 API 부하 테스트</b></summary>


# 요구 사항

> ### **`STEP 19`**
> - 부하 테스트 대상 선정 및 목적, 시나리오 등의 계획을 세우고 이를 문서로 작성
> - 적합한 테스트 스크립트를 작성하고 수행
> - `NiceToHave` → Docker 의 실행 옵션 (cpu, memory) 등을 조정하면서 애플리케이션을 실행하여 성능 테스트를 진행해보면서 적절한 배포 스펙 고려도 한번 진행해보세요!
> ### **`STEP 20`**
> - 위 테스트를 진행하며 획득한 다양한 성능 지표를 분석 및 시스템 내의 병목을 탐색 및 개선해보고 **(가상)** 장애 대응 문서를 작성하고 제출
> - 최종 발표 자료 작성 및 제출





# 부하 테스트 대상 API 선정


## 1. **전체 API 리스트업**

#### **예약 API (`ReservationController`)**
1. **`POST /api/reservations`**: 임시 예약 생성
2. **`POST /api/reservations/confirm`**: 임시 예약 확정
3. **`GET /api/reservations/status/{userId}/{concertOptionId}`**: 예약 상태 조회

#### **콘서트 및 콘서트 옵션 API (`ConcertController`)**
4. **`POST /api/concerts`**: 콘서트 생성
5. **`POST /api/concerts/{concertId}/options`**: 콘서트 옵션 생성

#### **예약 가능 날짜 / 좌석 API (`AvailabilityController`)**
6. **`GET /api/availability/dates/{concertId}`**: 예약 가능 날짜 조회
7. **`GET /api/availability/seats/{concertOptionId}/{requestAt}`**: 예약 가능 좌석 조회

#### **결제 API (`PaymentController`)**
8. **`POST /api/user-balance/payment`**: 결제 처리
9. **`POST /api/user-balance/payment/cancel/{transactionId}`**: 결제 취소
10. **`GET /api/user-balance/payment/history/{userId}`**: 결제 내역 조회

#### **잔액 관리 API (`BalanceController`)**
11. **`GET /api/user-balance/{userId}`**: 잔액 조회
12. **`GET /api/user-balance/histories/{userId}`**: 잔액 충전/사용 내역 조회
13. **`PUT /api/user-balance/charge/{userId}`**: 잔액 충전
14. **`PUT /api/user-balance/use/{userId}`**: 잔액 사용

#### **처리열 토큰 API (`ProcessingQueueTokenController`)**
15. **`GET /api/processing-queue-token/check-availability/{userId}`**: 처리열 토큰 유효성 조회 (유저 ID 포함)
16. **`GET /api/processing-queue-token/check-availability`**: 처리열 토큰 유효성 조회 (유저 ID 없이)

#### **대기열 토큰 API (`WaitingQueueTokenController`)**
17. **`POST /api/waiting-queue-token`**: 대기열 토큰 생성 및 인입
18. **`GET /api/waiting-queue-token/{userId}`**: 대기열 토큰 정보 조회


** 현 시스템에서 오케스트레이션은 주로 다른 서비스 간의 조율을 담당하며, 직접적인 로직을 수행하지 않기 때문에 이번 부하 테스트에서는 제외되었습니다. 따라서, 이번 테스트는 도메인 서비스 중심으로 진행되며, 실제 사용자 요청이 집중될 수 있는 API들을 대상으로 시스템의 성능을 평가합니다.



## 2. **부하 테스트 대상 API 선정**

부하 테스트를 위해 다음 5개의 API를 선정했습니다. 선정된 API들은 실제 서비스 운영 시 트래픽이 집중될 가능성이 높은 작업을 기준으로 선정되었습니다.


1. **`POST /api/waiting-queue-token`**: 대기열 토큰 생성 및 인입
  - **이유**: 대기열 시스템 기반의 예약 시스템에서 부하가 가장 많이 예상되는 구간입니다. 유저들이 특정 시점에 몰리면서 대기열에 인입할 때, 시스템은 이 모든 부하를 처리해야 하므로, 이 API에 대한 부하 테스트는 매우 중요합니다. 대기열 인입 시 시스템의 최대 처리 능력을 평가하는 데 적합합니다.

2. **`GET /api/waiting-queue-token/{userId}`**: 대기열 토큰 정보 조회
  - **이유**: 대기열에 인입된 후, 유저들은 자신의 대기 상태를 확인하기 위해 지속적으로 이 API를 polling합니다. 다수의 사용자가 동시에 반복적으로 조회 요청을 보낼 경우, 시스템에 상당한 부하가 발생할 수 있습니다. 따라서, 이 API의 부하 테스트는 대기열 관리 시스템의 안정성을 평가하는 데 필수적입니다.

3. **`GET /api/availability/seats/{concertOptionId}/{requestAt}`**: 예약 가능 좌석 조회
  - **이유**: 대기열에서 해제된 유저들이 예약을 진행하기 전, 좌석 정보를 조회하게 됩니다. 특히, 특정 시점에 많은 사용자가 동시에 좌석 조회를 시도할 경우, 이 API는 실시간 데이터 조회와 관련된 부하를 처리해야 합니다.

4. **`GET /api/availability/dates/{concertId}`**: 예약 가능 날짜 조회
  - **이유**: 예약 가능한 날짜를 조회하는 과정에서 많은 데이터가 관련될 수 있습니다. 이 API는 좌석 조회와 마찬가지로, 많은 사용자가 동시에 요청을 보낼 경우, 시스템의 성능을 저하시키는 요인이 될 수 있습니다.



# 테스트 환경 설정

### 장비 및 시스템 환경

- **운영 체제**: macOS (Apple M1 Pro)
- **프로세서**: Apple M1 Pro
- **메모리 (RAM)**: 16GB
- **스토리지**: 512GB SSD
- **Docker**: Docker Desktop for Mac
  - **Docker Engine**: 27.1.1
  - **Docker Compose**: v2.28.1-desktop.1
- **가상화**: Apple Hypervisor Framework (ARM 기반)

### Docker 기반 서비스 설정

- **MySQL**:
  - **버전**: 8.0.33

- **Redis**:
  - **버전**: 7.2.5

- **Kafka**:
  - **버전**: 7.7.0-ccs

### 테스트 및 모니터링 툴

- **Gatling**: 3.10.3
- **Prometheus**: 2.53.1
- **Grafana**: 11.1.1


### 기본 더미 데이터 설정

| 테이블 이름              | 데이터 건수   |
|--------------------------|---------------|
| Balance                  | 약 200만 건   |
| Balance Transaction      | 약 200만 건   |
| Concert                  | 약 1만 건     |
| Concert Option           | 약 100만 건   |
| Payment Transaction      | 약 100만 건   |
| Reservation              | 약 100만 건   |
| Temporal Reservation     | 약 100만 건   |






# 테스트 시나리오 및 수행 결과 분석

## 1. **`POST /api/waiting-queue-token`: 대기열 토큰 생성 및 인입**

### 테스트 시나리오

**목적**: 이 테스트는 대규모 유저가 동시에 대기열에 인입할 때 시스템의 처리 능력을 평가합니다. 목표는 대기열 생성 과정에서의 최대 처리량을 확인하고, 시스템이 고부하 상태에서도 안정적으로 동작하는지 평가하는 것입니다.

- **시나리오 이름**: 대기열 토큰 생성 시나리오
- **테스트 흐름**:
  1. 다수의 유저가 동시에 대기열에 인입하려고 시도합니다.
  2. 각 유저는 고유한 요청을 보내며, 서버는 이를 처리하여 대기열 토큰을 발급합니다.
  3. 발급된 토큰이 정상적으로 응답되었는지 확인합니다.
- **부하 조건**:
  - 유저 수: 초당 20명에서 50명까지 유저 증가
  - 지속 시간: 1분, 최대 peak 20초간 일정한 부하를 유지
  - **선정 이유**: 대기열 토큰 생성은 트래픽이 몰릴 때 시스템에 가장 큰 부하를 발생시킬 수 있는 작업입니다. 트래픽이 증가하는 상황에서 서버의 처리 능력을 평가하기 위해 초당 유저 수를 점진적으로 증가시켜 부하를 가합니다. 이 설정은 서버가 트래픽 급증 시 안정적으로 작동하는지 확인하는 데 적합합니다.
  - **예상 트래픽**: 목표 TPS는 500 이상으로 설정하며, 초당 최대 500개의 대기열 토큰 생성 요청이 발생할 수 있는 상황을 시뮬레이션하여 서버의 응답 성능을 평가합니다.


<details>
<summary><b>코드 부분</b></summary>

```java
    {
		setUp(createScenario()
			.injectOpen(getOpenInjectionSteps())
			.protocols(httpProtocolBuilder))
			.maxDuration(Duration.ofMinutes(1))
			.assertions(global().requestsPerSec().gte(500.0));
	}

	private OpenInjectionStep[] getOpenInjectionSteps() {
		return new OpenInjectionStep[]{
			rampUsersPerSec(20).to(50).during(Duration.ofSeconds(20)),
			constantUsersPerSec(50).during(Duration.ofSeconds(20)),
			rampUsersPerSec(50).to(1).during(Duration.ofSeconds(20))
		};
	}

	private ScenarioBuilder createScenario() {
		return scenario("Waiting Queue Token Generation Scenario")
			.asLongAs(session -> userIdCounter.get() <= MAX_USER_ID)
			.on(exec(session -> {
					int userId = userIdCounter.getAndIncrement();
					return session.set("userId", "user" + userId);
				})
					.exec(http("대기열 토큰 생성")
						.post("/api/waiting-queue-token")
						.body(StringBody(session -> {
							LocalDateTime now = LocalDateTime.now();
							return String.format("{ \"userId\": \"%s\", \"requestAt\": \"%s\" }", session.getString("userId"), now.toString());
						}))
						.check(status().is(201))
					)
					.pause(2)
			);
	}
```
</details>




### 수행 결과 분석

#### Gatling 리포트

<details>
<summary><b>1회차 </b></summary>

```
---- Global Information --------------------------------------------------------
> request count                                      35172 (OK=35172  KO=0     )
> min response time                                      2 (OK=2      KO=-     )
> max response time                                    712 (OK=712    KO=-     )
> mean response time                                    66 (OK=66     KO=-     )
> std deviation                                         89 (OK=89     KO=-     )
> response time 50th percentile                         31 (OK=31     KO=-     )
> response time 75th percentile                         87 (OK=87     KO=-     )
> response time 95th percentile                        234 (OK=234    KO=-     )
> response time 99th percentile                        450 (OK=450    KO=-     )
> mean requests/sec                                  586.2 (OK=586.2  KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                         35172 (100%)
> 800 ms <= t < 1200 ms                                  0 (  0%)
> t >= 1200 ms                                           0 (  0%)
> failed                                                 0 (  0%)
```

![token-generation-1.png](..%2Fperformance-test%2Fapi%2Ftoken-generation-1.png)


</details>


<details>
<summary><b>2회차 </b></summary>

```
---- Global Information --------------------------------------------------------
> request count                                      34607 (OK=34607  KO=0     )
> min response time                                      2 (OK=2      KO=-     )
> max response time                                    877 (OK=877    KO=-     )
> mean response time                                    92 (OK=92     KO=-     )
> std deviation                                        117 (OK=117    KO=-     )
> response time 50th percentile                         42 (OK=41     KO=-     )
> response time 75th percentile                        115 (OK=115    KO=-     )
> response time 95th percentile                        366 (OK=366    KO=-     )
> response time 99th percentile                        486 (OK=486    KO=-     )
> mean requests/sec                                576.783 (OK=576.783 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                         34602 (100%)
> 800 ms <= t < 1200 ms                                  5 (  0%)
> t >= 1200 ms                                           0 (  0%)
> failed                                                 0 (  0%)
```

![token-generation-2.png](..%2Fperformance-test%2Fapi%2Ftoken-generation-2.png)


</details>


<details>
<summary><b>3회차 </b></summary>

```
---- Global Information --------------------------------------------------------
> request count                                      33834 (OK=33757  KO=77    )
> min response time                                      2 (OK=2      KO=185   )
> max response time                                   1611 (OK=1611   KO=860   )
> mean response time                                   143 (OK=143    KO=526   )
> std deviation                                        191 (OK=190    KO=138   )
> response time 50th percentile                         62 (OK=62     KO=547   )
> response time 75th percentile                        169 (OK=168    KO=601   )
> response time 95th percentile                        588 (OK=584    KO=702   )
> response time 99th percentile                        822 (OK=821    KO=838   )
> mean requests/sec                                  563.9 (OK=562.617 KO=1.283 )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                         33355 ( 99%)
> 800 ms <= t < 1200 ms                                374 (  1%)
> t >= 1200 ms                                          28 (  0%)
> failed                                                77 (  0%)
---- Errors --------------------------------------------------------------------
> status.find.is(201), but actually found 500                        77 (100.0%)
```


![token-generation-3.png](..%2Fperformance-test%2Fapi%2Ftoken-generation-3.png)

</details>




#### 성능 지표 분석


- **평균 요청 처리 속도**: 약 575.6 requests/sec
- **평균 응답 시간**: 약 100.33ms
- **50번째 백분위수 응답 시간**: 약 44ms
- **95번째 백분위수 응답 시간**: 약 396.6ms
- **최대 응답 시간**: 1611ms (3회차 테스트 중 발생)

이 성능 지표들은 시스템이 대부분의 요청을 비교적 빠르게 처리하지만, 트래픽이 증가함에 따라 응답 시간이 늘어나는 경향이 있음을 보여줍니다. 특히, 3회차 테스트에서는 최대 응답 시간이 1611ms에 달하며, 500 오류가 발생한 요청도 일부 확인되었습니다. 초당 약 500의 요청을 받아낼 수는 있지만, 이 정도의 고부하 상황에서 시스템이 불안정해질 수 있음을 시사합니다.












## 2. **`GET /api/waiting-queue-token/{userId}`: 대기열 토큰 정보 조회**

**목적**: 유저들이 지속적으로 대기열 상태를 확인하는 상황을 시뮬레이션하여, 시스템의 응답 성능과 안정성을 평가하는 것입니다. 특히, 대량의 polling 요청이 있을 때 서버의 성능을 분석합니다.

**테스트 시나리오**:

- **시나리오 이름**: 대기열 토큰 조회 시나리오
- **테스트 흐름**:
  1. 여러 유저가 대기열 상태를 지속적으로 확인하기 위해 polling 요청을 보냅니다.
  2. 서버는 각 유저의 대기열 상태를 반환합니다.
  3. 응답이 200 OK 상태인지 확인하고, 응답 지연 시간 측정.
- **부하 조건**:
  - 유저 수: 초당 200명씩 polling 요청
  - 지속 시간: 1분, 최대 peak 20초간 일정한 부하를 유지
  - **선정 이유**: 대기열 토큰 조회 API는 유저들이 대기 상태를 확인하기 위해 빈번하게 요청을 보내는 작업입니다. 지속적이고 반복적인 부하를 통해 시스템의 안정성과 응답 시간을 평가하기 위해 초당 200명의 유저가 지속적으로 요청을 보내도록 설정되었습니다.
  - **예상 트래픽**: 목표 TPS는 100 이상으로 설정되며, 초당 최대 100개의 polling 요청이 발생할 수 있는 상황을 시뮬레이션하여 서버의 지속적 부하 처리 능력을 평가합니다.



<details>
<summary><b>코드 부분</b></summary>

```java
	{
		setUp(createScenario()
			.injectOpen(getOpenInjectionSteps()))
			.protocols(httpProtocolBuilder)
			.maxDuration(Duration.ofMinutes(1))
		.assertions(global().requestsPerSec().gte(150.0)); 
	}

	private OpenInjectionStep[] getOpenInjectionSteps() {
		return new OpenInjectionStep[] {
			rampUsersPerSec(100).to(200).during(Duration.ofSeconds(20)), 
			constantUsersPerSec(200).during(Duration.ofSeconds(20)), 
			rampUsersPerSec(200).to(100).during(Duration.ofSeconds(20)) 
		};
	}
```

</details>


### 수행 결과 분석

#### Gatling 리포트


<details>
<summary><b>1회차 </b></summary>

```
---- Global Information --------------------------------------------------------
> request count                                       9999 (OK=9999   KO=0     )
> min response time                                      3 (OK=3      KO=-     )
> max response time                                    225 (OK=225    KO=-     )
> mean response time                                    26 (OK=26     KO=-     )
> std deviation                                         30 (OK=30     KO=-     )
> response time 50th percentile                         15 (OK=15     KO=-     )
> response time 75th percentile                         33 (OK=33     KO=-     )
> response time 95th percentile                         89 (OK=89     KO=-     )
> response time 99th percentile                        143 (OK=143    KO=-     )
> mean requests/sec                                 166.65 (OK=166.65 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          9999 (100%)
> 800 ms <= t < 1200 ms                                  0 (  0%)
> t >= 1200 ms                                           0 (  0%)
> failed                                                 0 (  0%)
```


</details>



<details>
<summary><b>2회차 </b></summary>

```
---- Global Information --------------------------------------------------------
> request count                                       9985 (OK=9985   KO=0     )
> min response time                                      3 (OK=3      KO=-     )
> max response time                                    540 (OK=540    KO=-     )
> mean response time                                    18 (OK=18     KO=-     )
> std deviation                                         33 (OK=33     KO=-     )
> response time 50th percentile                          7 (OK=7      KO=-     )
> response time 75th percentile                         18 (OK=18     KO=-     )
> response time 95th percentile                         66 (OK=67     KO=-     )
> response time 99th percentile                        148 (OK=148    KO=-     )
> mean requests/sec                                166.417 (OK=166.417 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          9985 (100%)
> 800 ms <= t < 1200 ms                                  0 (  0%)
> t >= 1200 ms                                           0 (  0%)
> failed                                                 0 (  0%)
```


</details>


<details>
<summary><b>3회차 </b></summary>

```
---- Global Information --------------------------------------------------------
> request count                                       9997 (OK=9997   KO=0     )
> min response time                                      3 (OK=3      KO=-     )
> max response time                                    992 (OK=992    KO=-     )
> mean response time                                    42 (OK=42     KO=-     )
> std deviation                                         75 (OK=75     KO=-     )
> response time 50th percentile                         18 (OK=18     KO=-     )
> response time 75th percentile                         45 (OK=45     KO=-     )
> response time 95th percentile                        150 (OK=150    KO=-     )
> response time 99th percentile                        374 (OK=374    KO=-     )
> mean requests/sec                                166.617 (OK=166.617 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          9980 (100%)
> 800 ms <= t < 1200 ms                                 17 (  0%)
> t >= 1200 ms                                           0 (  0%)
> failed                                                 0 (  0%)
================================================================================
```


</details>



#### 성능 지표 분석


- **평균 요청 처리 속도**: 약 166.56 requests/sec
- **평균 응답 시간**: 약 28.67ms
- **50번째 백분위수 응답 시간**: 약 13.33ms
- **95번째 백분위수 응답 시간**: 약 101.67ms
- **최대 응답 시간**: 992ms (3회차 테스트 중 발생)

이 성능 지표들은 전반적으로 요청을 매우 빠르게 처리하고 있음을 나타냅니다. 특히, 95%의 요청이 102ms 이내에 처리되고 있으며, 최대 응답 시간이 992ms로, 전체적으로 안정적인 성능을 보여줍니다. 다만, 3회차 테스트에서 응답 시간이 비교적 높은 요청이 일부 확인되었으며, 이는 트래픽 증가 시 발생할 수 있는 응답 지연을 의미할 수 있습니다. 전체적인 결과는 목표 TPS와 일치하기 때문에, 시스템이 설정된 부하 조건 하에서 안정적으로 동작하고 있음을 시사합니다.







## 3. **`GET /api/availability/seats/{concertOptionId}/{requestAt}`: 예약 가능 좌석 조회**

**목적**: 대규모 유저가 동시에 좌석 정보를 조회할 때, 시스템의 응답 성능과 처리 능력을 평가하기 위한 것입니다. 실시간 데이터 조회에 대한 서버의 처리 능력을 테스트합니다.

**테스트 시나리오**:

- **시나리오 이름**: 예약 가능 좌석 조회 시나리오
- **테스트 흐름**:
  1. 많은 유저가 동시에 특정 콘서트의 좌석 정보를 조회합니다.
  2. 서버는 좌석 정보 데이터를 반환하며, 각 요청에 대해 200 OK 응답을 보냅니다.
  3. 응답 시간 및 정확성을 평가합니다.
- **부하 조건**:
  - 유저 수: 초당 20명에서 80명까지 증가
  - 지속 시간: 1분, 최대 peak 20초간 일정한 부하를 유지
  - **선정 이유**: 예약 가능 좌석 조회 API는 실시간 데이터를 다루며, 많은 유저가 동시에 좌석 정보를 조회할 때 시스템의 성능이 크게 영향을 받을 수 있습니다. 이러한 상황을 시뮬레이션하기 위해 초당 20명에서 80명까지 유저 수를 증가시키는 부하 조건을 설정하여, 시스템이 급격한 트래픽 증가에 어떻게 반응하는지 평가합니다.
  - **예상 트래픽**: 목표 TPS는 약 20~50 TPS로 설정하며, 초당 최대 80개의 좌석 조회 요청이 발생할 수 있는 상황을 평가하여 시스템의 처리 성능을 분석합니다.


<details>
<summary><b>코드 부분</b></summary>


```java
	{
		setUp(createScenario()
			.injectOpen(getOpenInjectionSteps()))
			.protocols(httpProtocolBuilder)
			.maxDuration(Duration.ofMinutes(1))
			.assertions(global().requestsPerSec().gte(50.0));
	}

	private OpenInjectionStep[] getOpenInjectionSteps() {
		return new OpenInjectionStep[] {
			rampUsersPerSec(20).to(80).during(Duration.ofSeconds(30)),
			constantUsersPerSec(80).during(Duration.ofSeconds(30)),
			rampUsersPerSec(80).to(20).during(Duration.ofSeconds(30))
		};
	}
```

</details>




### 수행 결과 분석

#### Gatling 리포트


<details>
<summary><b>1회차 </b></summary>

```
---- Global Information --------------------------------------------------------
> request count                                       3898 (OK=3898   KO=0     )
> min response time                                      3 (OK=3      KO=-     )
> max response time                                    515 (OK=515    KO=-     )
> mean response time                                    22 (OK=22     KO=-     )
> std deviation                                         37 (OK=37     KO=-     )
> response time 50th percentile                         11 (OK=11     KO=-     )
> response time 75th percentile                         22 (OK=22     KO=-     )
> response time 95th percentile                         77 (OK=77     KO=-     )
> response time 99th percentile                        164 (OK=164    KO=-     )
> mean requests/sec                                 64.967 (OK=64.967 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          3898 (100%)
> 800 ms <= t < 1200 ms                                  0 (  0%)
> t >= 1200 ms                                           0 (  0%)
> failed                                                 0 (  0%)
```


</details>



<details>
<summary><b>2회차 </b></summary>

```
---- Global Information --------------------------------------------------------
> request count                                       3896 (OK=3896   KO=0     )
> min response time                                      4 (OK=4      KO=-     )
> max response time                                    344 (OK=344    KO=-     )
> mean response time                                    15 (OK=15     KO=-     )
> std deviation                                         25 (OK=25     KO=-     )
> response time 50th percentile                          8 (OK=8      KO=-     )
> response time 75th percentile                         16 (OK=16     KO=-     )
> response time 95th percentile                         40 (OK=40     KO=-     )
> response time 99th percentile                        139 (OK=139    KO=-     )
> mean requests/sec                                 64.933 (OK=64.933 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          3896 (100%)
> 800 ms <= t < 1200 ms                                  0 (  0%)
> t >= 1200 ms                                           0 (  0%)
> failed                                                 0 (  0%)

```


</details>



<details>
<summary><b>3회차 </b></summary>

```
---- Global Information --------------------------------------------------------
> request count                                       3899 (OK=3899   KO=0     )
> min response time                                      4 (OK=4      KO=-     )
> max response time                                    290 (OK=290    KO=-     )
> mean response time                                    24 (OK=24     KO=-     )
> std deviation                                         30 (OK=30     KO=-     )
> response time 50th percentile                         12 (OK=12     KO=-     )
> response time 75th percentile                         28 (OK=28     KO=-     )
> response time 95th percentile                         80 (OK=80     KO=-     )
> response time 99th percentile                        164 (OK=164    KO=-     )
> mean requests/sec                                 64.983 (OK=64.983 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          3899 (100%)
> 800 ms <= t < 1200 ms                                  0 (  0%)
> t >= 1200 ms                                           0 (  0%)
> failed                                                 0 (  0%)
```


</details>



#### 성능 지표 분석


- **평균 요청 처리 속도**: 약 64.96 requests/sec
- **평균 응답 시간**: 약 20.33ms
- **50번째 백분위수 응답 시간**: 약 10.33ms
- **95번째 백분위수 응답 시간**: 약 65.67ms
- **최대 응답 시간**: 515ms (1회차 테스트 중 발생)

이 성능 지표들은 시스템이 대부분의 요청을 매우 빠르게 처리할 수 있음을 보여줍니다. 특히, 응답 시간의 분포가 전반적으로 낮으며, 95번째 백분위수 응답 시간도 65ms 내외로 매우 양호합니다. 3회차 테스트에서 최대 응답 시간이 290ms로 상대적으로 낮아지는 등, 시스템이 고부하 상황에서도 안정적으로 작동하고 있음을 시사합니다. 모든 요청에서 실패 없이 200 OK 응답을 받았다는 점도 시스템의 신뢰성을 나타냅니다.






### 개선 방향



## 4. **`GET /api/availability/dates/{concertId}`: 예약 가능 날짜 조회**

**목적**: 대규모의 유저가 동시에 예약 가능한 날짜 정보를 조회하는 상황을 시뮬레이션하여, 서버의 성능과 응답 속도를 평가하는 것입니다.

**테스트 시나리오**:

- **시나리오 이름**: 예약 가능 날짜 조회 시나리오
- **테스트 흐름**:
  1. 다수의 유저가 특정 콘서트의 예약 가능 날짜를 조회합니다.
  2. 서버는 각 요청에 대해 200 OK 응답을 보내고, 날짜 정보를 반환합니다.
  3. 응답 시간과 성능을 평가합니다.
- **부하 조건**:
  - 유저 수: 초당 10명에서 50명까지 증가
  - 지속 시간: 1분, 최대 peak 20초간 일정한 부하를 유지
  - **선정 이유**: 예약 가능한 날짜 조회 API는 대량의 데이터를 처리하며, 많은 유저가 동시에 요청할 때 시스템의 응답 시간이 길어질 수 있습니다. 이러한 상황을 시뮬레이션하기 위해 초당 10명에서 50명까지 유저 수를 증가시키는 부하 조건을 설정하여, 시스템의 성능을 평가합니다.
  - **예상 트래픽**: 목표 TPS는 약 10~50 TPS로 설정되며, 초당 최대 50개의 날짜 조회 요청이 발생할 수 있는 상황을 평가하여 시스템의 데이터 처리 성능을 분석합니다.




<details>
<summary><b>코드 부분</b></summary>

</details>



### 수행 결과 분석

#### Gatling 리포트


<details>
<summary><b>1회차 </b></summary>

```
---- Global Information --------------------------------------------------------
> request count                                       3897 (OK=3897   KO=0     )
> min response time                                      7 (OK=7      KO=-     )
> max response time                                    542 (OK=542    KO=-     )
> mean response time                                    28 (OK=28     KO=-     )
> std deviation                                         41 (OK=41     KO=-     )
> response time 50th percentile                         15 (OK=15     KO=-     )
> response time 75th percentile                         28 (OK=28     KO=-     )
> response time 95th percentile                         81 (OK=81     KO=-     )
> response time 99th percentile                        226 (OK=226    KO=-     )
> mean requests/sec                                  64.95 (OK=64.95  KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          3897 (100%)
> 800 ms <= t < 1200 ms                                  0 (  0%)
> t >= 1200 ms                                           0 (  0%)
> failed                                                 0 (  0%)
```
</details>



<details>
<summary><b>2회차 </b></summary>

```
---- Global Information --------------------------------------------------------
> request count                                       3896 (OK=3896   KO=0     )
> min response time                                      8 (OK=8      KO=-     )
> max response time                                   1036 (OK=1036   KO=-     )
> mean response time                                    55 (OK=55     KO=-     )
> std deviation                                         70 (OK=70     KO=-     )
> response time 50th percentile                         34 (OK=34     KO=-     )
> response time 75th percentile                         68 (OK=68     KO=-     )
> response time 95th percentile                        153 (OK=153    KO=-     )
> response time 99th percentile                        318 (OK=318    KO=-     )
> mean requests/sec                                 64.933 (OK=64.933 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          3888 (100%)
> 800 ms <= t < 1200 ms                                  8 (  0%)
> t >= 1200 ms                                           0 (  0%)
> failed                                                 0 (  0%)
```

</details>


<details>
<summary><b>3회차 </b></summary>

```
---- Global Information --------------------------------------------------------
> request count                                       3897 (OK=3897   KO=0     )
> min response time                                      7 (OK=7      KO=-     )
> max response time                                   1384 (OK=1384   KO=-     )
> mean response time                                    54 (OK=54     KO=-     )
> std deviation                                        111 (OK=111    KO=-     )
> response time 50th percentile                         27 (OK=27     KO=-     )
> response time 75th percentile                         47 (OK=47     KO=-     )
> response time 95th percentile                        160 (OK=160    KO=-     )
> response time 99th percentile                        667 (OK=667    KO=-     )
> mean requests/sec                                  64.95 (OK=64.95  KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          3867 ( 99%)
> 800 ms <= t < 1200 ms                                 26 (  1%)
> t >= 1200 ms                                           4 (  0%)
> failed                                                 0 (  0%)
```



</details>



#### 성능 지표 분석

세 차례의 테스트에서 얻은 성능 지표는 다음과 같습니다.

- **평균 요청 처리 속도**: 약 64.94 requests/sec
- **평균 응답 시간**: 약 45.67ms
- **50번째 백분위수 응답 시간**: 약 25.33ms
- **95번째 백분위수 응답 시간**: 약 131.33ms
- **최대 응답 시간**: 1384ms (3회차 테스트 중 발생)

이 성능 지표는 시스템이 대부분의 요청을 신속하게 처리하고 있음을 보여줍니다. 평균 응답 시간은 비교적 낮고, 95%의 요청이 약 131ms 이내에 처리되었습니다. 그러나 2회차와 3회차 테스트에서 일부 요청이 800ms를 초과하며, 최대 1384ms의 응답 시간이 기록된 점은 주목할 필요가 있습니다. 이는 시스템이 특정 상황에서 부하가 증가하면 응답 시간이 길어질 수 있음을 시사합니다.





# 가상 장애 대응 상황 설정 및 대응


## API별 100배 부하 설정 가상 시나리오

각 API에 대해 예상되는 최대 트래픽의 100배에 달하는 부하를 가정하여, 시스템의 극한 상황에서의 반응을 테스트합니다. 극단적인 고부하 상황에서 발생할 수 있는 병목 구간을 식별하고, 시스템의 안정성 및 성능 개선을 위한 인사이트를 얻습니다.



### 1. `POST /api/waiting-queue-token`

- **부하 조건**: 초당 5000명의 유저가 대기열 토큰을 생성하는 상황을 시뮬레이션합니다.
- **병목 구간 설정**:
  - **Redis의 성능 및 확장성**:
    - 대기열 토큰 생성 요청을 처리하는 Redis는 대량의 쓰기 작업이 발생할 때 성능 저하가 발생할 수 있습니다. 특히, Redis 클러스터의 노드 간 데이터 분산과 네트워크 대역폭 사용이 병목 현상을 초래할 가능성이 있습니다.

##### 개선 방향
- **Redis 클러스터 확장**: Redis 클러스터의 노드 수를 확장하여 데이터 분산과 처리 능력을 향상시킵니다. 클러스터 내 데이터 분산 알고리즘을 재검토하여 부하를 균등하게 분산할 수 있도록 최적화합니다.
- **쓰기 최적화**: Redis의 AOF(Append Only File) 옵션을 최적화하여 쓰기 성능을 향상시키고, 필요 없는 데이터를 주기적으로 삭제하여 메모리 사용량을 줄입니다.

### 2. `GET /api/waiting-queue-token/{userId}`

- **부하 조건**: 초당 20,000명의 유저가 자신의 대기열 상태를 조회하는 상황을 시뮬레이션합니다.
- **병목 구간 식별**:
  - **Redis의 읽기 성능 및 확장성**:
    - 이 API는 대기열 상태를 조회할 때 Redis에서 유저의 위치 정보를 읽어옵니다. 초당 20,000건의 읽기 요청이 발생할 경우, Redis의 처리 능력이 병목이 될 수 있습니다. 읽기 전용 Redis 클러스터를 별도로 구성하거나, 슬레이브 노드 수를 증가시켜 읽기 부하를 분산하는 방안을 고려해야 합니다.

##### 개선 방향
- **읽기 전용 Redis 클러스터 구성**: 읽기 전용 Redis 클러스터를 별도로 구성하여 읽기 부하를 분산시킵니다. 이를 통해 읽기 요청 처리 능력을 향상시키고, 메인 Redis 클러스터의 쓰기 성능에 미치는 영향을 최소화합니다.
- **슬레이브 노드 추가**: 슬레이브 노드를 추가하여 읽기 요청을 슬레이브 노드에서 처리하게 함으로써 Redis의 전반적인 부하를 분산합니다.

### 3. `GET /api/availability/seats/{concertOptionId}/{requestAt}`

- **부하 조건**: 초당 8000명의 유저가 특정 콘서트의 좌석 정보를 조회하는 상황을 시뮬레이션합니다.
- **병목 구간 식별**:
  - **데이터베이스 조회 및 인덱스 효율성**:
    - 좌석 정보 조회 시 다수의 데이터베이스 조회 작업이 발생하며, 인덱스의 효율성이 중요한 역할을 합니다. 데이터베이스의 쿼리 처리 속도와 인덱스의 적절한 사용이 병목이 될 수 있습니다.

##### 개선 방향
- **인덱스 최적화**: 쿼리 성능을 향상시키기 위해 데이터베이스 인덱스를 최적화합니다. 빈번히 사용되는 쿼리에 대해 추가적인 인덱스를 생성하거나, 기존 인덱스를 재구성하여 조회 속도를 개선합니다.
- **쿼리 튜닝**: 데이터베이스 쿼리를 튜닝하여 필요 없는 조인 및 서브쿼리를 줄이고, 효율적인 쿼리 경로를 사용할 수 있도록 합니다. 또한, 동일한 조회 요청이 반복적으로 발생하는 경우 캐시된 결과를 활용하는 쿼리 캐싱을 도입합니다.

### 4. `GET /api/availability/dates/{concertId}`

- **부하 조건**: 초당 5000명의 유저가 동시에 특정 콘서트의 예약 가능 날짜를 조회하는 상황을 시뮬레이션합니다.
- **병목 구간 설정**:
  - **로컬 캐시 문제**:
    - 이 API는 로컬 캐시를 사용하여 예약 가능 날짜 정보를 캐싱합니다. 고부하 상황에서는 로컬 캐시가 빠르게 소진될 수 있으며, 캐시 재생성 과정에서 성능 저하가 발생할 수 있습니다. 캐시 TTL 설정 및 크기를 재검토하고, 필요 시 Redis와 같은 외부 캐시 시스템을 사용하는 글로벌 캐시로의 확장을 고려해야 합니다.

##### 개선 방향
- **캐시 TTL 및 크기 최적화**: 로컬 캐시의 TTL 설정을 재검토하여 캐시 갱신 빈도를 줄이고, 캐시 크기를 확장하여 고부하 상황에서도 충분한 데이터를 캐싱할 수 있도록 합니다.
- **외부 캐시 시스템 도입**: 로컬 캐시의 한계를 보완하기 위해 Redis와 같은 외부 캐시 시스템으로 확장하여 캐시 미스로 인한 DB 부하를 줄이고, 전체적인 응답 속도를 향상시킵니다.
- **캐시 갱신 전략 최적화**: 동일한 캐시 키에 대해 다수의 요청이 동시에 발생할 때 성능 저하를 방지하기 위해, 캐시 갱신 시점을 조정하고 데이터 일관성을 유지하면서도 성능을 높일 수 있는 갱신 전략을 도입합니다.








</details>



</details>




