Feature: 잔액 충전 기능
  Scenario: id와 amount가 주어질 때 balance를 충전한다 - 기본 시나리오
    When 사용자의 id가 1이고 충전 금액이 500인 경우 잔액을 충전 요청하고 정상 응답을 받는다
    Then 사용자의 잔액은 500이어야 한다

  Scenario: id와 amount가 주어질 때 balance를 충전한다 - 추가 충전 시나리오
    When 사용자의 id가 2이고 충전 금액이 500인 경우 잔액을 충전 요청하고 정상 응답을 받는다
    Then 사용자의 잔액은 500이어야 한다
    When 사용자의 id가 2이고 추가 충전 금액이 300인 경우 잔액을 추가 충전 요청하고 정상 응답을 받는다
    Then 사용자의 잔액은 800이어야 한다







