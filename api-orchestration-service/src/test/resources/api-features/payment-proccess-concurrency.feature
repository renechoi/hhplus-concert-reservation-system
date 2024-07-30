Feature: 결제 동시성 처리 기능

  Background:
    Given 사용자의 id가 1이고 충전 금액이 0인 경우 잔액을 충전 요청하고 정상 응답을 받는다
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    And 사용자의 초기 잔액을 저장한다
    Given 사용자의 id가 1이고 충전 금액이 5000인 경우 잔액을 충전 요청하고 정상 응답을 받는다
    And 사용자의 충전 후 잔액을 저장한다
    When 사용자 id 1로 대기열에 인입 요청하고 토큰을 받는다
    And 스텝 구분을 위한 딜레이 2초를 기다린다

    Given 다음과 같은 콘서트 생성 요청을 보내고 성공 응답을 받는다
      | title  | requestAt |
      | random | now       |
    And 다음과 같은 콘서트 옵션 생성 요청을 보내고 성공 응답을 받는다
      | concertDate | concertDuration | title  | description | price  | requestAt | maxSeats |
      | randomAfter | random          | random | random      | random | now       | 50       |
    And 다음과 같은 콘서트 옵션 생성 요청을 보내고 성공 응답을 받는다
      | concertDate | concertDuration | title  | description | price  | requestAt | maxSeats |
      | randomAfter | random          | random | random      | random | now       | 50       |
    And 최근 생성한 콘서트에 대해 다음과 같은 콘서트 예약 가능 날짜 조회 요청을 보내고 성공 응답을 받는다
    And 최근 생성한 콘서트 옵션에 대해 다음과 같은 콘서트 예약 가능 좌석 조회 요청을 보내고 성공 응답을 받는다
    When 최근 생성한 콘서트 옵션에 대해 가장 빠른 날짜와 가장 앞의 좌석에 예약 생성 요청을 보내고 성공 응답을 받는다
      | userId | requestAt |
      | 1      | now       |

  Scenario: 동일 사용자가 동일한 결제 요청을 시도한다 - 동시성 제어 시나리오
    Given 동일한 사용자가 가장 최근의 예약에 대해 다음과 같은 결제 요청을 동시에 시도한다
      | userId | amount | paymentMethod | requestAt | paymentTarget |
      | 1      | 1000   | CREDIT_CARD   | now       | RESERVATION   |
      | 1      | 1000   | CREDIT_CARD   | now       | RESERVATION   |
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 최종 사용자의 잔액은 충전된 잔액에서 사용한 잔액 금액 1000이 차감되어 반영되어 있어야 한다

