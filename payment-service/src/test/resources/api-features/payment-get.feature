Feature: 결제 조회 기능

  Background:
    Given 사용자의 id가 1이고 결제 내역이 다음과 같이 존재할 때
      | transactionId | userId | amount | paymentStatus |
      | 1             | 1      | 1000   | COMPLETE       |
      | 2             | 1      | 500    | FAILED        |

  Scenario: 유효한 사용자 id로 결제 내역 조회
    When 사용자의 id가 1인 경우 결제 내역을 조회 요청하고 정상 응답을 받는다
    Then 조회한 결제 내역은 다음과 같아야 한다
      | transactionId | userId | amount | paymentStatus |
      | 1             | 1      | 1000   | COMPLETE       |
      | 2             | 1      | 500    | FAILED        |

  Scenario: 실패한 결제 내역 조회
    When 사용자의 id가 1인 경우 실패한 결제 내역을 조회 요청하고 정상 응답을 받는다
    Then 조회한 실패한 결제 내역은 다음과 같아야 한다
      | transactionId | userId | amount | paymentStatus |
      | 2             | 1      | 500    | FAILED        |

  Scenario: 존재하지 않는 id로 결제 내역 조회
    When 사용자의 id가 99인 경우 결제 내역을 조회 요청하면 204 응답을 받는다
    Then 조회한 결제 내역은 비어 있어야 한다
