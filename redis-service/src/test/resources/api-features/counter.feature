Feature: Redis Counter Service

  Background:
    Given 다음과 같은 Redis 카운터 "testCounter"를 초기화한다

  Scenario: 카운터 증가
    When 다음과 같은 Redis 카운터 증가 요청을 보내고 성공 응답을 받는다
      | counterKey | value |
      | testCounter | 1     |
    Then Redis 카운터 응답은 다음과 같이 확인되어야 한다
      | counterKey  | value |
      | testCounter | 1     |

  Scenario: 카운터 감소
    When 다음과 같은 Redis 카운터 감소 요청을 보내고 성공 응답을 받는다
      | counterKey | value |
      | testCounter | 1     |
    Then Redis 카운터 응답은 다음과 같이 확인되어야 한다
      | counterKey  | value |
      | testCounter | -1    |

  Scenario: 카운터 조회
    Given 다음과 같은 Redis 카운터 증가 요청을 보내고 성공 응답을 받는다
      | counterKey | value |
      | testCounter | 1     |
    When 다음과 같은 Redis 카운터 조회 요청을 보낸다
      | counterKey  |
      | testCounter |
    Then Redis 카운터 조회 응답은 다음과 같이 확인되어야 한다
      | counterKey  | value |
      | testCounter | 1     |
