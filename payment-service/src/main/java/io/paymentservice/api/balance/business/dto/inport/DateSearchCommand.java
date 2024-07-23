package io.paymentservice.api.balance.business.dto.inport;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface DateSearchCommand {

    @Getter
    @RequiredArgsConstructor
    enum DateSearchCondition {
        BEFORE("이전", "지정된 날짜 이전"),
        AFTER("이후", "지정된 날짜 이후"),
        ON("당일", "지정된 날짜 당일");

        private final String title;
        private final String description;
    }

    @Getter
    @RequiredArgsConstructor
    enum DateSearchTarget {
        CREATED_AT("생성 시각", "생성된 시각"),
        REQUEST_AT("요청 시각", "요청이 시각");

        private final String title;
        private final String description;
    }
}
