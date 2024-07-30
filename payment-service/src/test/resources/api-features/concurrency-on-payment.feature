Feature: 결제 동시성 처리 기능

  Background:
    Given 사용자의 id가 1이고 충전 금액이 5000인 경우 잔액을 충전 요청하고 정상 응답을 받는다

  Scenario: 동일 사용자가 동시에 상이한 결제 요청을 시도한다 - 성공 시나리오
    Given 동일한 사용자가 다음과 같은 결제 요청을 동시에 시도한다
      | userId | targetId | amount | paymentMethod |
      | 1      | A2       | 1000   | CREDIT_CARD   |
      | 1      | A3       | 1000   | CREDIT_CARD   |
    When 사용자의 id가 1인 경우 결제 내역을 조회 요청하고 정상 응답을 받는다
    Then 조회한 결제 내역은 다음과 같아야 한다
      | transactionId | userId | amount | paymentStatus |
      | 1             | 1      | 1000   | COMPLETE       |
      | 2             | 1      | 1000    | COMPLETE        |
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 조회한 사용자의 잔액은 3000이어야 한다

  Scenario: 동일 사용자가 동시에 같은 결제 요청을 시도한다 - 성공 시나리오(동일한 결제도 허용하는 정책)
    Given 동일한 사용자가 다음과 같은 결제 요청을 동시에 시도한다
      | userId | targetId | amount | paymentMethod |
      | 1      | A4       | 1000   | CREDIT_CARD   |
      | 1      | A4       | 1000   | CREDIT_CARD   |
    When 사용자의 id가 1인 경우 결제 내역을 조회 요청하고 정상 응답을 받는다
    Then 조회한 결제 내역은 모두 COMPLETE여야 한다
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 조회한 사용자의 잔액은 3000이어야 한다

  Scenario: 동일 사용자가 잔액 부족 시 동시에 여러 결제 요청을 시도한다
    Given 동일한 사용자가 다음과 같은 결제 요청을 동시에 시도한다
      | userId | targetId | amount | paymentMethod |
      | 1      | A7       | 4000   | CREDIT_CARD   |
      | 1      | A8       | 2000   | CREDIT_CARD   |
      | 1      | A9       | 3500   | CREDIT_CARD   |
    When 사용자의 id가 1인 경우 결제 내역을 조회 요청하고 정상 응답을 받는다
    Then 조회한 결제 내역에서 한 건은 COMPLETE이고 나머지는 FAILED여야 한다
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    And 조회한 사용자의 잔액은 3000 이하여야 한다
