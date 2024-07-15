Feature: 콘서트 예약 가능 날짜 및 좌석 조회

  Background:
    When 사용자 id 1로 대기열에 인입 요청하고 토큰을 받는다
    And 스텝 구분을 위한 딜레이 2초를 기다린다
    Given 다음과 같은 콘서트 생성 요청을 보내고 성공 응답을 받는다
      | title  | requestAt |
      | random | now       |
    And 다음과 같은 콘서트 옵션 생성 요청을 보내고 성공 응답을 받는다
      | concertDate | concertDuration | title  | description | price  | requestAt | maxSeats |
      | randomAfter | random          | 옵션 제목 | 옵션 설명      | random | now       | 50       |

  Scenario: 예약 가능 날짜 조회
    When 최근 생성한 콘서트에 대해 다음과 같은 콘서트 예약 가능 날짜 조회 요청을 보내고 성공 응답을 받는다


  Scenario: 예약 가능 좌석 조회
    When 최근 생성한 콘서트 옵션에 대해 다음과 같은 콘서트 예약 가능 좌석 조회 요청을 보내고 성공 응답을 받는다
    And 총 예약 가능 좌석 개수는 50개로 확인되어야 한다


