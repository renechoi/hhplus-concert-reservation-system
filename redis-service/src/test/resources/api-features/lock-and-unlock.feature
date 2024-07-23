Feature: Redis Lock Service

  Scenario: Redis 락 획득
    When 다음과 같은 Redis 락 요청을 보내고 성공 응답을 받는다
      | lockKey | waitTime | leaseTime | timeUnit |
      | testLock | 10       | 5         | SECONDS  |
    Then Redis 락 응답은 다음과 같이 확인되어야 한다
      | lockKey | waitTime | leaseTime | isLocked |
      | testLock | 10       | 5         | true     |
    And 동일한 key로 lock을 획득하려고 할 때 실패해야함
      | lockKey  | waitTime | leaseTime | timeUnit |
      | testLock | 1        | 5         | SECONDS  |

  Scenario: Redis 락 해제
    Given 다음과 같은 Redis 락 요청을 보내고 성공 응답을 받는다
      | lockKey | waitTime | leaseTime | timeUnit |
      | testLock | 10       | 5         | SECONDS  |
    When 다음과 같은 Redis 락 해제 요청을 보내고 성공 응답을 받는다
      | lockKey |
      | testLock |
    Then Redis 락 해제 응답은 다음과 같이 확인되어야 한다
      | lockKey  | isUnlocked |
      | testLock | true       |
    And 동일한 key로 lock을 획득하려고 할 때 성공해야함
      | lockKey | waitTime | leaseTime | timeUnit |
      | testLock | 10       | 5         | SECONDS  |
