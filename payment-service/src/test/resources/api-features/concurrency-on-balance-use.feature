Feature: 잔액 사용 기능 - 동시성 시나리오

  Background:
    Given 사용자의 id가 1이고 충전 금액이 500000인 경우 잔액을 충전 요청하고 정상 응답을 받는다

  Scenario: 동시에 여러 번 balance를 사용한다 - 동시성 시나리오 - 모든 요청 수용 정책
    When 사용자의 id가 1이고 사용 금액이 1000인 잔액 사용 요청을 동시에 300번 보낸다
    And 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 조회한 사용자의 잔액은 200000이어야 한다
