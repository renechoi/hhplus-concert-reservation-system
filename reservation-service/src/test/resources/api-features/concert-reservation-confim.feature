Feature: 예약 확정 기능 검증

  Background:
    Given 다음과 같은 콘서트 생성 요청을 보내고 성공 응답을 받는다
      | title  | requestAt |
      | 콘서트 제목 | now       |
    And 다음과 같은 콘서트 옵션 생성 요청을 보내고 성공 응답을 받는다
      | concertDate      | concertDuration | title | description | price | requestAt | maxSeats |
      | 2024-07-10T20:00 | PT2H            | 옵션 제목 | 옵션 설명       | 10000 | now       | 50       |
    And 다음과 같은 예약 생성 요청을 보내고 성공 응답을 받는다
      | concertOptionId | section | seatRow | seatNumber | userId | requestAt |
      | 1               | A       | 1       | 1          | 1      | now       |

  Scenario: 임시 예약 확정
    When 다음과 같은 예약 확정 요청을 보내고 성공 응답을 받는다
      | temporalReservationId | userId | concertOptionId | requestAt |
      | 1                     | 1      | 1               | now       |
    Then 예약이 성공적으로 확정되었음을 확인한다
      | userId | concertOptionId |
      | 1      | 1               |
