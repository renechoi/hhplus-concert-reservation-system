Feature: Redis Cache Service

  Scenario: 캐시에 데이터 저장
    When 다음과 같은 Redis 캐시 요청을 보내고 성공 응답을 받는다
      | cacheKey | cacheValue | ttl |
      | testKey  | testValue  | 60  |
    Then Redis 캐시 응답은 다음과 같이 확인되어야 한다
      | cacheKey | cacheValue | ttl | isCached |
      | testKey  | testValue  | 60  | true     |

  Scenario: 캐시에서 데이터 가져오기
    Given 다음과 같은 Redis 캐시 요청을 보내고 성공 응답을 받는다
      | cacheKey | cacheValue | ttl |
      | testKey  | testValue  | 60  |
    When 다음과 같은 Redis 캐시 조회 요청을 보낸다
      | cacheKey |
      | testKey  |
    Then Redis 캐시 조회 응답은 다음과 같이 확인되어야 한다
      | cacheKey | cacheValue |
      | testKey  | testValue  |

  Scenario: 캐시에서 데이터 삭제
    Given 다음과 같은 Redis 캐시 요청을 보내고 성공 응답을 받는다
      | cacheKey | cacheValue | ttl |
      | testKey  | testValue  | 60  |
    When 다음과 같은 Redis 캐시 삭제 요청을 보낸다
      | cacheKey |
      | testKey  |
    When 다음과 같은 Redis 캐시 조회 요청을 보낸다
      | cacheKey |
      | testKey  |
    Then Redis 캐시 조회 응답은 없음을 확인한다
      | cacheKey |
      | testKey  |

  Scenario: 캐시에 데이터 저장 및 만료 확인
    When 다음과 같은 Redis 캐시 요청을 보내고 성공 응답을 받는다
      | cacheKey | cacheValue | ttl |
      | testKey  | testValue  | 1   |
    Then Redis 캐시 응답은 다음과 같이 확인되어야 한다
      | cacheKey | cacheValue | ttl | isCached |
      | testKey  | testValue  | 1   | true     |
    And 스텝 구분을 위한 딜레이 1.0초를 기다린다
    When 다음과 같은 Redis 캐시 조회 요청을 보낸다
      | cacheKey |
      | testKey  |
    Then Redis 캐시 조회 응답은 없음을 확인한다
      | cacheKey |
      | testKey  |