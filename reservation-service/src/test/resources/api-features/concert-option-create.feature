Feature: 콘서트 옵션 생성

  Background:
    And 다음과 같은 콘서트 생성 요청을 보내고 성공 응답을 받는다
      | title  | requestAt |
      | 콘서트 제목 | now       |

  Scenario: 새로운 콘서트 옵션 생성
    When 다음과 같은 콘서트 옵션 생성 요청을 보내고 성공 응답을 받는다
      | concertDate      | concertDuration | title | description | price | requestAt | maxSeats |
      | 2024-07-10T20:00 | PT2H            | 옵션 제목 | 옵션 설명       | 10000 | now       |  50|
    Then 콘서트 옵션이 성공적으로 생성되었음을 확인한다
      | concertDate      | concertDuration | title | description | price |
      | 2024-07-10T20:00 | PT2H            | 옵션 제목 | 옵션 설명       | 10000 |
