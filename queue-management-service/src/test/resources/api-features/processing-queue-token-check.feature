Feature: 처리열 토큰 확인
  Background:
    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 성공 응답을 받는다
      | userId | priority | requestAt |
      | user1  | 1        | now       |
    And 스텝 구분을 위한 딜레이 3초를 기다린다
    Given 다음과 같은 유저 아이디로 대기열 토큰 조회를 요청하면 성공 응답을 받는다
      | userId |
      | user1  |

  Scenario: 생성된 대기열 토큰 조회 - 처리열 인입 시나리오
    Given 유저 아이디와 응답받은 대기열 토큰으로 처리열 토큰 유효성을 조회 요청하면 성공 응답을 받는다
      | userId | tokenValue |
      | user1  | retrieved  |
    Then 조회된 처리열 토큰의 정보가 아래와 같이 확인되어야 한다
      | userId | tokenValue | position | validUntil | status     |
      | user1  | notNull    | notNull  | notNull    | PROCESSING |



  Scenario: 잘못된 userId로 처리열 토큰 유효성을 조회 요청하면 실패 응답을 받는다
    Given 잘못된 유저 아이디와 토큰으로 처리열 토큰 유효성을 조회 요청하면 204 응답을 받는다
      | userId | tokenValue |
      | user2  | retrieved  |

  Scenario: 잘못된 tokenValue로 처리열 토큰 유효성을 조회 요청하면 실패 응답을 받는다
    Given 다음과 같은 유저 아이디와 토큰으로 처리열 토큰 유효성을 조회 요청하면 204 응답을 받는다
      | userId | tokenValue |
      | user1  | invalidToken |

  Scenario: 잘못된 userId와 tokenValue로 처리열 토큰 유효성을 조회 요청하면 실패 응답을 받는다
    Given 다음과 같은 유저 아이디와 토큰으로 처리열 토큰 유효성을 조회 요청하면 204 응답을 받는다
      | userId | tokenValue |
      | user2  | invalidToken |
