Feature: 콘서트 예약

  Background:
    Given 다음과 같은 콘서트 생성 요청을 보내고 성공 응답을 받는다
      | title  | requestAt |
      | 콘서트 제목 | now       |
    And 다음과 같은 콘서트 옵션 생성 요청을 보내고 성공 응답을 받는다
      | concertDate      | concertDuration | title | description | price | requestAt | maxSeats |
      | 2024-07-10T20:00 | PT2H            | 옵션 제목 | 옵션 설명       | 10000 | now       | 50       |

  Scenario: 새로운 예약 생성
    When 다음과 같은 예약 생성 요청을 보내고 성공 응답을 받는다
      | concertOptionId | section | seatRow | seatNumber | userId | requestAt |
      | 1               | A       | 1       | 1          | 1      | now       |
    Then 예약이 성공적으로 생성되었음을 확인한다
      | userId |
      | 1      |
