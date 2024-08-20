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