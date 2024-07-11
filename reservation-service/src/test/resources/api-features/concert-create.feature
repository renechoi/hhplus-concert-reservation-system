Feature: 콘서트 생성

  Scenario: 새로운 콘서트 생성
    And 다음과 같은 콘서트 생성 요청을 보내고 성공 응답을 받는다
      | title  | requestAt |
      | 콘서트 제목 | now       |
    Then 콘서트가 성공적으로 생성되었음을 확인한다
      | title       |
      | 콘서트 제목  |
