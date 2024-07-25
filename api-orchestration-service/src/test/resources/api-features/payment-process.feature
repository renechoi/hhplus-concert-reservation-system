Feature: 결제 처리 기능

  Background:
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    And 사용자의 초기 잔액을 저장한다
    Given 사용자의 id가 1이고 충전 금액이 5000인 경우 잔액을 충전 요청하고 정상 응답을 받는다
    And 사용자의 충전 후 잔액을 저장한다
    When 사용자 id 1로 대기열에 인입 요청하고 토큰을 받는다

  Scenario: 유효한 결제 요청을 처리한다 - 기본 시나리오
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

    Given 가장 최근의 예약에 대한 결제 요청이 다음과 같이 주어졌을 때
      | userId | amount | paymentMethod | paymentTarget | targetId | requestAt |
      | 1      | 1000   | CREDIT_CARD   | RESERVATION   | 1        |  now      |

    When 결제 요청을 처리하고 정상 응답을 받는다
    Then 결제 상태는 COMPLETE여야 한다
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 최종 사용자의 잔액은 충전된 잔액에서 사용한 잔액 금액 1000이 차감되어 반영되어 있어야 한다
    And 결제 내역이 저장되어야 한다
    And 스텝 구분을 위한 딜레이 1초를 기다린다
    And 예약 상태를 확인하면 임시 예약이 아니라 확정 예약으로 확인되어야 한다
    And 가장 최근의 요청과 동일하게 다시 결제 유효하지 않은 토큰으로 예외 발생해야 한다

  Scenario: 잔액 부족으로 인해 결제 실패 시나리오
    Given 결제 요청이 다음과 같이 주어졌을 때
      | userId | reservationId | amount       | paymentMethod | requestAt |
      | 1      | 100           | 999999999999 | CREDIT_CARD   | now       |
    When 결제 요청을 처리하면 실패 응답을 받는다
    When 사용자의 id가 1인 경우 잔액을 조회 요청하고 정상 응답을 받는다
    Then 최종 사용자의 잔액은 충전 잔액 대비 차감되지 않은 상태여야 한다
