Feature: 콘서트 예약 가능 날짜 및 좌석 조회

  Background:
    Given 다음과 같은 콘서트 생성 요청을 보내고 성공 응답을 받는다
      | title      | requestAt |
      | 콘서트 제목   | now       |
    And 다음과 같은 콘서트 옵션 생성 요청을 보내고 성공 응답을 받는다
      | concertDate      | concertDuration | title | description | price | requestAt | maxSeats |
      | 2026-07-10T20:00 | PT2H            | 옵션 제목 | 옵션 설명       | 10000 | now       | 50       |

  Scenario: 예약 가능 날짜 조회
    When 다음과 같은 콘서트 예약 가능 날짜 조회 요청을 보내고 성공 응답을 받는다
      | concertId |
      | 1         |
    Then 조회한 예약 가능 날짜 응답은 다음과 같이 확인되어야 한다
      | concertOptionId | concertDate      | concertDuration | title    | description | price |
      | 1               | 2026-07-10T20:00 | PT2H            | 옵션 제목 | 옵션 설명      | 10000 |

  Scenario: 예약 가능 좌석 조회
    When 다음과 같은 콘서트 예약 가능 좌석 조회 요청을 보내고 성공 응답을 받는다
      | concertOptionId | requestAt |
      | 1               | now       |
    Then 조회한 예약 가능 좌석 조회 응답은 다음과 같이 확인되어야 한다
      | seatId | seatNumber | occupied |
      | 1      | 1          | false    |
      | 2      | 2          | false    |
    And 총 예약 가능 좌석 개수는 50개로 확인되어야 한다
