# 정확히 1회 반영 정책에서의 낙관 락 한계

## 요구 사항 
동일 사용자의 동시 요청 → 무조건 1회만 처리하고 나머지는 실패시킨다.

## 낙관 락 구현 
service와 Balance 엔티티의 version 필드를 이용하여 구현한다. 

```java
@Transactional
public BalanceChargeInfo charge(BalanceChargeCommand command) {
    Balance balance = balanceRepository.findByUserId(command.getUserId())
                                       .orElse(createDefaultNewBalance(command.getUserId()));
    balance.charge(command.getAmount(), command.getTransactionReason());
    return BalanceChargeInfo.from(balanceRepository.save(balance));
}
```

```java
@Entity
public class Balance {

    // ...
	@Version
	private Long version;
}
```

## 한계점

### 문제 상황

적은 동시 요청에서는 성공하지만, 요청이 많아지면 실패 확률이 급격히 증가하며, 최종 잔액이 랜덤하게 확인된다. 

다음과 같은 테스트를 통해 확인해보자.

```
Feature: 잔액 충전 기능 - 동시성 시나리오

  Scenario: 동시에 여러 번 balance를 충전한다 - 동시성 시나리오
    Given 사용자의 id가 1이고 충전 금액이 1000인 경우 잔액을 충전 요청하고 정상 응답을 받는다

    When 사용자의 id가 1이고 충전 금액이 500인 잔액 충전 요청을 동시에 300번 보낸다

    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 조회한 사용자의 잔액은 1500이어야 한다
```

이와 같은 테스트에서 요청 횟수를 몇 번으로 하더라도 결과는 항상 1500이어야 한다. 

5번의 요청일 때는 다음과 같이 정상 성공한다. 


![optimistic-lock-test-for-5-request-success.png](captures%2Foptimistic-lock-test-for-5-request-success.png)



그런데? 30번으로 늘려보면 어떨까?
실패한다.

![optimistic-lock-test-for-30-request-fail.png](captures%2Foptimistic-lock-test-for-30-request-fail.png)

1500이 나와야 하는데 2000이 나와버렸다. 

300번으로 늘려보면 어떨까?
당연히 실패하고, 이번의 최종 잔액은 5000으로 확인된다. 

![optimistic-lock-test-for-300-request-fail.png](captures%2Foptimistic-lock-test-for-300-request-fail.png)

즉, 요청이 많아지면 트랜잭션 경합과 롤백 재시도로 인해 잔액이 예측 불가능하게 변할 수 있다.

### 원인

낙관 락의 메커니즘에서 발생하는 문제를 보다 심도 있게 분석하기 위해, 동시 요청 상황의 실제 트랜잭션 타임라인을 시뮬레이션해보자.


#### 시뮬레이션: 요청이 30개일 경우

1. **트랜잭션 1 ~ 16**:
   - 각 트랜잭션이 `balance` 엔티티의 동일한 초기 버전(예: 버전 1)을 읽는다.
   - 각 트랜잭션은 충전 작업을 수행하고 커밋 시점에서 CAS를 사용하여 버전 1을 버전 2로 변경하려고 시도한다.
   - 이 과정에서 가장 먼저 커밋을 시도한 트랜잭션 하나만 성공하고 나머지 트랜잭션은 실패한다. 이로 인해 트랜잭션 1 ~ 16 중 단 1개만 성공하고 나머지는 실패한다.

2. **트랜잭션 17 ~ 30**:
   - 트랜잭션 1 ~ 16 중 하나가 성공하여 `balance` 엔티티의 버전이 2로 변경된다.
   - 트랜잭션 17 ~ 30은 이 변경된 버전 2를 읽고 작업을 수행한다.
   - 각 트랜잭션은 다시 충전 작업을 수행하고 커밋 시점에서 버전 2를 버전 3으로 변경하려고 시도한다.
   - 이 과정에서도 가장 먼저 커밋을 시도한 트랜잭션 하나만 성공하고 나머지는 실패한다. 이로 인해 트랜잭션 17 ~ 30 중 단 1개만 성공하고 나머지는 실패한다.

### 시뮬레이션 타임라인 예시

```
1. T1, T2, ..., T16 각 트랜잭션이 버전 1을 읽음.
2. T1이 가장 먼저 커밋을 시도하여 성공, 버전 1 -> 버전 2로 변경.
3. 나머지 T2, ..., T16은 커밋 시점에서 실패.
4. T17, T18, ..., T30 각 트랜잭션이 버전 2를 읽음.
5. T17이 가장 먼저 커밋을 시도하여 성공, 버전 2 -> 버전 3으로 변경.
6. 나머지 T18, ..., T30은 커밋 시점에서 실패.
```

이 시나리오에서 총 30개의 요청 중 처음 16개의 트랜잭션은 버전 1에서 경합하고, 이후 14개의 트랜잭션은 버전 2에서 경합하게 된다. 이 과정에서 충돌이 발생하면 트랜잭션은 롤백되고 재시도한다.
이는 아무리 동시 요청이라도 한 사이클에 처리할 수 있는 요청 수에는 한계가 있기 때문이다.
예를 들어, 첫 번째 사이클에서는 16개의 요청 중 1개가 성공하고 나머지는 실패하며, 두 번째 사이클에서는 나머지 14개의 요청 중 1개가 성공하고 나머지는 실패하게 된다.

#### 결론

낙관 락은 소수의 동시 요청을 처리할 때는 효과적이지만, 동시 요청이 많아지면 실패 확률이 급격히 증가한다.
이는 한 사이클에 처리할 수 있는 요청 수에 한계가 있기 때문이다.
즉, 각 사이클에서 동시 요청 중 하나의 트랜잭션만 성공하고 나머지는 모두 실패하고, 따라서 단순 낙관 락 구현으로는 요구 사항인 `정확히 1회 반영`에 대해 충족하지 못한다.


## 해결 방안

### 비관 락

비관 락으로는 가능할까?

- **트랜잭션 락 한계**: 각 트랜잭션에 대해 락을 걸게 되므로, 여러 요청이 동시에 들어오면 첫 번째 트랜잭션이 끝날 때까지 다른 요청들은 대기 상태에 있다.
- **동시 요청 처리 문제**: 어떤 시점에서는 동시 요청이 아닌 요청들이 생기게 되고, 첫 번째 트랜잭션이 끝난 이후에 들어오는 요청들은 새로운 트랜잭션으로 처리되어 성공하게 된다.

즉, 비관 락을 사용하더라도 모든 동시 요청을 정확히 1회만 반영하지 못한다.

### 비즈니스 정책 기반 해결

결국, 이 요구사항을 수용하기 위해서는 다음과 같은 비즈니스 정책에 대한 합의가 필요하다.

1. **동시 요청 판별**: 동시 요청을 판별하는 명확한 기준을 정의한다. 예를 들어, 특정 시간 창(예: 100ms) 내에 들어오는 동일한 요청을 동시 요청으로 간주한다.
2. **중복 요청 필터링**: 특정 키(예: 사용자 ID, 요청 시간, 요청 유형)를 기준으로 중복 요청을 식별하고, 하나의 요청만 처리하고 나머지는 실패로 처리한다.

```java
@Transactional
public BalanceChargeInfo charge(BalanceChargeCommand command) {
    if (isDuplicateRequest(command)) {
        throw new DuplicateRequestException();
    }
    Balance balance = balanceRepository.findByUserId(command.getUserId())
                                       .orElse(createDefaultNewBalance(command.getUserId()));
    balance.charge(command.getAmount(), command.getTransactionReason());
    return BalanceChargeInfo.from(balanceRepository.save(balance));
}

private boolean isDuplicateRequest(BalanceChargeCommand command) {
    // 중복 요청 판단 로직 구현
}
```






