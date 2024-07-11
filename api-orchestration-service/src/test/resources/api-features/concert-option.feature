Feature: 콘서트 옵션 기능

  Background:
    And 다음과 같은 콘서트 생성 요청을 보내고 성공 응답을 받는다
      | title  | requestAt |
      | random | now       |

  Scenario: 새로운 콘서트 옵션 생성
    When 다음과 같은 콘서트 옵션 생성 요청을 보내고 성공 응답을 받는다
      | concertDate | concertDuration | title  | description | price  | requestAt | maxSeats |
      | randomAfter | random          | random | random      | random | now       | 50       |
    Then 콘서트 옵션이 성공적으로 생성되었음을 확인한다
      | concertDate | concertDuration | title  | description | price  |
      | randomAfter | random          | random | random      | random |


