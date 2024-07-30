Feature: 잔액 충전 기능 - 동시성 시나리오

  Scenario: 동시에 여러 번 balance를 충전한다 - 동시성 시나리오 - 모든 요청 수용 정책
    Given 사용자의 id가 1이고 충전 금액이 1000인 경우 잔액을 충전 요청하고 정상 응답을 받는다
    When 사용자의 id가 1이고 충전 금액이 500인 잔액 충전 요청을 동시에 300번 보낸다
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
#    Then 조회한 사용자의 잔액은 1500이어야 한다
    Then 조회한 사용자의 잔액은 151000이어야 한다


