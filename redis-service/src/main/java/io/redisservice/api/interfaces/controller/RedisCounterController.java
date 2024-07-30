package io.redisservice.api.interfaces.controller;

import static io.redisservice.common.model.CommonApiResponse.*;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.redisservice.api.application.dto.request.CounterRequest;
import io.redisservice.api.application.dto.response.CounterResponse;
import io.redisservice.api.application.facade.RedisCounterFacade;
import io.redisservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@RestController
@RequestMapping("/api/redis-counter")
@RequiredArgsConstructor
public class RedisCounterController {

    private final RedisCounterFacade redisCounterFacade;

    @PostMapping("/increment")
    @Operation(summary = "카운터 증가 API")
    public CommonApiResponse<CounterResponse> increment(@RequestBody @Validated CounterRequest request) {
        return OK(redisCounterFacade.increment(request));
    }

    @PostMapping("/decrement")
    @Operation(summary = "카운터 감소 API")
    public CommonApiResponse<CounterResponse> decrement(@RequestBody @Validated CounterRequest request) {
        return OK(redisCounterFacade.decrement(request));
    }

    @GetMapping(value = "/{counterKey}")
    @Operation(summary = "카운터 조회 API")
    public CommonApiResponse<CounterResponse> getCounter(@PathVariable String counterKey) {
        return OK(redisCounterFacade.getCounter(counterKey));
    }
}
