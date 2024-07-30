package io.redisservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.redisservice.api.application.dto.request.CounterRequest;
import io.redisservice.api.application.dto.response.CounterResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public class RedisCounterContextHolder implements TestDtoContextHolder {
    private static final ConcurrentHashMap<String, CounterResponse> counterResponseMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, CounterRequest> counterRequestMap = new ConcurrentHashMap<>();
    private static final AtomicReference<String> mostRecentCounterKey = new AtomicReference<>();

    public static void initFields() {
        counterResponseMap.clear();
        counterRequestMap.clear();
        mostRecentCounterKey.set(null);
    }

    public static void putCounterRequest(String counterKey, CounterRequest request) {
        counterRequestMap.put(counterKey, request);
        mostRecentCounterKey.set(counterKey);
    }

    public static CounterRequest getCounterRequest(String counterKey) {
        return counterRequestMap.get(counterKey);
    }

    public static void putCounterResponse(String counterKey, CounterResponse response) {
        counterResponseMap.put(counterKey, response);
        mostRecentCounterKey.set(counterKey);
    }

    public static CounterResponse getCounterResponse(String counterKey) {
        return counterResponseMap.get(counterKey);
    }

    public static String getMostRecentCounterKey() {
        return mostRecentCounterKey.get();
    }

    public static CounterResponse getMostRecentCounterResponse() {
        String recentCounterKey = mostRecentCounterKey.get();
        return recentCounterKey != null ? getCounterResponse(recentCounterKey) : null;
    }
}
