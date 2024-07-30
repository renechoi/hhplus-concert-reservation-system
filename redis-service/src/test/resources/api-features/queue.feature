Feature: Redis Queue Service

  Scenario: 큐에 항목 추가
    When 다음과 같은 Redis 큐 요청을 보내고 성공 응답을 받는다
      | queueName | member  | score |
      | testQueue | member1 | 1.0   |
    Then Redis 큐 응답은 다음과 같이 확인되어야 한다
      | queueName | member  | score | success |
      | testQueue | member1 | 1.0   | true    |

  Scenario: 큐 멤버 조회
    Given 다음과 같은 Redis 큐 요청을 보내고 성공 응답을 받는다
      | queueName | member  | score |
      | testQueue | member1 | 1.0   |
      | testQueue | member2 | 2.0   |
    When 다음과 같은 Redis 큐 멤버 조회 요청을 보낸다
      | queueName |
      | testQueue |
    Then Redis 큐 멤버 조회 응답은 다음과 같이 확인되어야 한다
      | queueName | members         |
      | testQueue | [member1, member2] |

  Scenario: 큐 멤버의 순위 조회
    Given 다음과 같은 Redis 큐 요청을 보내고 성공 응답을 받는다
      | queueName | member  | score |
      | testQueue | member1 | 1.0   |
      | testQueue | member2 | 2.0   |
      | testQueue | member3 | 3.0   |
    When 다음과 같은 Redis 큐 멤버 순위 조회 요청을 보낸다
      | queueName | member  |
      | testQueue | member1 |
    Then Redis 큐 멤버 순위 조회 응답은 다음과 같이 확인되어야 한다
      | queueName | member  | rank |
      | testQueue | member1 | 0    |
    When 다음과 같은 Redis 큐 멤버 순위 조회 요청을 보낸다
      | queueName | member  |
      | testQueue | member3 |
    Then Redis 큐 멤버 순위 조회 응답은 다음과 같이 확인되어야 한다
      | queueName | member  | rank |
      | testQueue | member3 | 2    |