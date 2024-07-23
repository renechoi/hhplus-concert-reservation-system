Feature: 대기열 토큰 생성 - 동시성 시나리오

  Scenario: 여러 사용자가 동시에 대기열에 진입하는 경우
    Given 다음과 같은 다수의 유저 정보가 주어지고 대기열 토큰 생성을 동시에 요청하면 성공 응답을 받는다
      | userId  | priority | requestAt |
      | user1   | 1        | now       |
      | user2   | 1        | now       |
      | user3   | 1        | now       |
      | user4   | 1        | now       |
      | user5   | 1        | now       |
    Then 생성된 각 대기열 토큰의 성공 응답을 조회하면 아래와 같은 정보가 확인되어야 한다
      | userId | tokenValue | position | validUntil |
      | user1  | notNull    | notNull  | notNull    |
      | user2  | notNull    | notNull  | notNull    |
      | user3  | notNull    | notNull  | notNull    |
      | user4  | notNull    | notNull  | notNull    |
      | user5  | notNull    | notNull  | notNull    |

  Scenario: 여러 사용자가 동시에 대기열에 진입하여 동일한 순서 번호를 부여받는 경우를 방지
    Given 다음과 같은 다수의 유저 정보가 주어지고 대기열 토큰 생성을 동시에 요청하면 성공 응답을 받는다
      | userId  | priority | requestAt |
      | user6   | 1        | now       |
      | user7   | 1        | now       |
      | user8   | 1        | now       |
      | user9   | 1        | now       |
      | user10  | 1        | now       |
    Then 생성된 각 대기열 토큰의 성공 응답을 조회하면 position 값이 중복되지 않아야 한다
      | userId | tokenValue | position | validUntil |
      | user6  | notNull    | unique   | notNull    |
      | user7  | notNull    | unique   | notNull    |
      | user8  | notNull    | unique   | notNull    |
      | user9  | notNull    | unique   | notNull    |
      | user10 | notNull    | unique   | notNull    |

  Scenario: 동일한 사용자가 여러 번 대기열 진입을 요청하는 경우 중복 처리를 방지
    Given 다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 여러 번 동시에 요청하면 성공 응답을 받는다
      | userId | priority | requestAt |
      | user11 | 1        | now       |
    Then 생성된 대기열 토큰의 성공 응답을 조회하면 하나의 토큰만 생성되어야 한다
      | userId | tokenValue | position | validUntil |
      | user11 | notNull    | notNull  | notNull    |
    And 중복된 대기열 토큰 요청에 대해서는 실패 응답을 받아야 한다
      | userId | priority | requestAt |
      | user11 | 1        | now       |
