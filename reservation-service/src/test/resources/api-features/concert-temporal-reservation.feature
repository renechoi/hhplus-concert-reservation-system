Feature: 콘서트 임시 예약

  Background:
    Given 다음과 같은 콘서트 생성 요청을 보내고 성공 응답을 받는다
      | title  | requestAt |
      | 콘서트 제목 | now       |
    And 다음과 같은 콘서트 옵션 생성 요청을 보내고 성공 응답을 받는다
      | concertDate      | concertDuration | title | description | price | requestAt | maxSeats |
      | 2024-07-10T20:00 | PT2H            | 옵션 제목 | 옵션 설명       | 10000 | now       | 50       |

  Scenario: 임시 예약 만료 시나리오
    Given yml loader가 reservation.temporary.expire-seconds 설정을 2로 리턴하도록 mocking한다
    When 다음과 같은 예약 생성 요청을 보내고 성공 응답을 받는다
      | concertOptionId | section | seatRow | seatNumber | userId | requestAt |
      | 1               | A       | 1       | 1          | 1      | now       |
    And 예약 상태 조회 요청을 보내고 성공 응답을 받는다
      | userId | concertOptionId |
      | 1      | 1               |

    And 조회한 예약에 연결된 좌석은 다음과 같은 상태여야 한다
      | occupied |
      | true     |
    And 스텝 구분을 위한 딜레이 3초를 기다린다
    And 예약 상태 조회 요청을 보내고 성공 응답을 받는다
      | userId | concertOptionId |
      | 1      | 1               |
    Then 예약이 만료되어 취소되었음을 확인한다
    And 조회한 예약에 연결된 좌석은 다음과 같은 상태여야 한다
      | occupied |
      | false    |
