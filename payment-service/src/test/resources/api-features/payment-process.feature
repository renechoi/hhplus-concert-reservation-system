Feature: 결제 처리 기능

  Background:
    Given 사용자의 id가 1이고 충전 금액이 5000인 경우 잔액을 충전 요청하고 정상 응답을 받는다

  Scenario: 유효한 결제 요청을 처리한다 - 기본 시나리오
    Given 결제 요청이 다음과 같이 주어졌을 때
      | userId | amount | paymentMethod |
      | 1      | 1000   | CREDIT_CARD   |
    When 결제 요청을 처리하고 정상 응답을 받는다
    Then 결제 상태는 COMPLETE여야 한다
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 조회한 사용자의 잔액은 4000이어야 한다
    And 결제 내역이 저장되어야 한다

  Scenario: 잔액 부족으로 인해 결제 실패 시나리오
    Given 결제 요청이 다음과 같이 주어졌을 때
      | userId | amount | paymentMethod |
      | 1      | 10000  | CREDIT_CARD   |
    When 결제 요청을 처리하면 실패 응답을 받는다
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 조회한 사용자의 잔액은 5000이어야 한다
    Then 결제 상태는 FAILED여야 한다

