@YmlLoaderConfig
Feature: 대기열 토큰 생성

  Background:
    Given 레디스 데이터 초기화
    Given yml loader가 processing-queue.policy.max-limit 설정을 0로 리턴하도록 mocking한다

  Scenario: 새로운 대기열 토큰을 생성 및 대기열 인입 - 기본 시나리오
    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 성공 응답을 받는다
      | userId | priority | requestAt |
      | user1  | 1        | now       |
    Then 생성된 대기열 토큰의 성공 응답을 조회하면 아래와 같은 정보가 확인되어야 한다
      | userId | tokenValue | position | validUntil |
      | user1  | notNull    | notNull  | notNull    |


  Scenario: 대기열 토큰 생성 시 필수 필드가 누락된 경우 예외가 발생한다
    Given 다음과 같은 필수 필드가 누락된 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 실패 응답을 받는다
      | userId | priority | requestAt |
      |        | 1        | now       |



  Scenario: 새로운 대기열 토큰을 생성 및 대기열 인입 - max limit exception 시나리오 - Redis 방식에서 제한 없음
    Given yml loader가 waiting-queue.policy.max-limit 설정을 1로 리턴하도록 mocking한다
    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 성공 응답을 받는다
      | userId | priority | requestAt |
      | user1  | 1        | now       |
    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 성공 응답을 받는다
#    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 실패 응답을 받고 예외 메시지를 확인한다
      | userId | priority | requestAt | statusCode | message          |
      | user2  | 1        | now       | 400        | illegal argument |




  Scenario: 새로운 대기열 토큰을 생성 및 대기열 인입 - 동일 유저 시나리오 - Redis 방식에서 제한 없음
    Given yml loader가 waiting-queue.policy.max-limit 설정을 3로 리턴하도록 mocking한다
    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 성공 응답을 받는다
      | userId | priority | requestAt |
      | user1  | 1        | now       |
    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 성공 응답을 받는다
#    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 실패 응답을 받고 예외 메시지를 확인한다
      | userId | priority | requestAt | statusCode | message |
      | user1  | 1        | now       | 400        | Duplicate entry - user1 |