package io.reservationservice.api.business.dto.inport;

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
        CONCERT_DATE("콘서트 날짜", "콘서트가 열리는 날짜"),
        RESERVE_AT("예약 시각", "예약이 이루어진 시각"),
        EXPIRE_AT("만료 시각", "예약이 만료되는 시각"),
        CREATED_AT("생성 시각", "예약이 생성된 시각"),
        REQUEST_AT("요청 시각", "예약 요청이 이루어진 시각");

        private final String title;
        private final String description;
    }
}
