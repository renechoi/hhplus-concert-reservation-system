Feature: 예약 생성 동시성 테스트

  Background:
    Given 다음과 같은 콘서트 생성 요청을 보내고 성공 응답을 받는다
      | title  | requestAt |
      | 콘서트 제목 | now       |
    And 다음과 같은 콘서트 옵션 생성 요청을 보내고 성공 응답을 받는다
      | concertDate      | concertDuration | title | description | price | requestAt | maxSeats |
      | 2024-07-10T20:00 | PT2H            | 옵션 제목 | 옵션 설명       | 10000 | now       | 100      |

  Scenario: 여러 사용자가 동시에 동일한 좌석을 예약 시도
    When 각기 다른 random 유저가 동일한 좌석에 대해 10개의 예약 생성 요청을 동시에 보낸다
    Then 전체 예약을 repository에서 조회하여 다음과 같은 1개의 예약이 생성되었음을 확인한다
      | temporalReservationId |
      | 1                     |



