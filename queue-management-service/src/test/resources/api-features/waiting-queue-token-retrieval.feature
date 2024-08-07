@YmlLoaderConfig
Feature: 대기열 토큰 조회

  Background:
    Given 레디스 데이터 초기화
    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 성공 응답을 받는다
      | userId | priority | requestAt |
      | user1  | 1        | now       |

  Scenario: 생성된 대기열 토큰 조회 - 기본 시나리오
    Given yml loader가 processing-queue.policy.max-limit 설정을 0로 리턴하도록 mocking한다
    Given 다음과 같은 유저 아이디로 대기열 토큰 조회를 요청하면 성공 응답을 받는다
      | userId |
      | user1  |
    Then 조회된 대기열 토큰의 정보가 아래와 같이 확인되어야 한다
      | userId | tokenValue | position | validUntil | status  |
      | user1  | notNull    | notNull  | notNull    | WAITING |


  Scenario: 생성된 대기열 토큰 조회 - 처리열 인입 시나리오
    Given yml loader가 processing-queue.policy.max-limit 설정을 1로 리턴하도록 mocking한다
    And 스텝 구분을 위한 딜레이 3초를 기다린다
    Given 다음과 같은 유저 아이디로 대기열 토큰 조회를 요청하면 성공 응답을 받는다
      | userId |
      | user1  |
    Then 조회된 대기열 토큰의 정보가 아래와 같이 확인되어야 한다
      | tokenValue | status     |
      | notNull    | PROCESSING |


#  Scenario: 만료된 대기열 토큰 조회 시나리오 -> redis 구현에서는 대기열 만료 없음
#    Given yml loader가 waiting-queue.policy.token-expiry-as-seconds 설정을 1초로 리턴하도록 mocking한다
#    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 성공 응답을 받는다
#      | userId | priority | requestAt |
#      | user2  | 1        | now       |
#    And 스텝 구분을 위한 딜레이 1초를 기다린다
#    When 다음과 같은 유저 아이디로 대기열 토큰 조회를 요청하고 제시된 응답을 받는다
#      | userId | statusCode |
#      | user2  | 204        |


  Scenario: 여러 대기열 토큰의 현재 위치 조회 시나리오
    Given yml loader가 processing-queue.policy.max-limit 설정을 1로 리턴하도록 mocking한다
    And 스텝 구분을 위한 딜레이 3초를 기다린다
    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 성공 응답을 받는다
      | userId | priority | requestAt |
      | user2  | 1        | now       |
    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 성공 응답을 받는다
      | userId | priority | requestAt |
      | user3  | 1        | now       |
    When 다음과 같은 유저 아이디로 대기열 토큰 조회를 요청하면 성공 응답을 받는다
      | userId |
      | user1  |
    Then 조회된 대기열 토큰의 정보가 아래와 같이 확인되어야 한다
      | tokenValue | status     |
      | notNull    | PROCESSING |
    When 다음과 같은 유저 아이디로 대기열 토큰 조회를 요청하면 성공 응답을 받는다
      | userId |
      | user2  |
    Then 조회된 대기열 토큰의 정보가 아래와 같이 확인되어야 한다
      | tokenValue | status  |
      | notNull    | WAITING |
    When 다음과 같은 유저 아이디로 대기열 토큰 조회를 요청하면 성공 응답을 받는다
      | userId |
      | user3  |
    Then 조회된 대기열 토큰의 정보가 아래와 같이 확인되어야 한다
      | tokenValue | status  |
      | notNull    | WAITING |

