package io.redisservice.testhelpers.contextholder;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.redisservice.api.application.dto.LockRequest;
import io.redisservice.api.application.dto.LockResponse;
import io.redisservice.api.application.dto.UnLockRequest;
import io.redisservice.api.application.dto.UnLockResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
public class RedisLockContextHolder implements TestDtoContextHolder{
	private static final ConcurrentHashMap<String, LockResponse> lockResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, LockRequest> lockRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, UnLockResponse> unlockResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, UnLockRequest> unlockRequestMap = new ConcurrentHashMap<>();
	private static final AtomicReference<String> mostRecentLockKey = new AtomicReference<>();

	public static void initFields() {
		lockResponseMap.clear();
		lockRequestMap.clear();
		unlockResponseMap.clear();
		unlockRequestMap.clear();
		mostRecentLockKey.set(null);
	}

	public static void putLockRequest(String lockKey, LockRequest request) {
		lockRequestMap.put(lockKey, request);
		mostRecentLockKey.set(lockKey);
	}

	public static LockRequest getLockRequest(String lockKey) {
		return lockRequestMap.get(lockKey);
	}

	public static void putLockResponse(String lockKey, LockResponse response) {
		lockResponseMap.put(lockKey, response);
		mostRecentLockKey.set(lockKey);
	}

	public static LockResponse getLockResponse(String lockKey) {
		return lockResponseMap.get(lockKey);
	}

	public static void putUnLockRequest(String lockKey, UnLockRequest request) {
		unlockRequestMap.put(lockKey, request);
		mostRecentLockKey.set(lockKey);
	}

	public static UnLockRequest getUnLockRequest(String lockKey) {
		return unlockRequestMap.get(lockKey);
	}

	public static void putUnLockResponse(String lockKey, UnLockResponse response) {
		unlockResponseMap.put(lockKey, response);
		mostRecentLockKey.set(lockKey);
	}

	public static UnLockResponse getUnLockResponse(String lockKey) {
		return unlockResponseMap.get(lockKey);
	}

	public static String getMostRecentLockKey() {
		return mostRecentLockKey.get();
	}

	public static LockResponse getMostRecentLockResponse() {
		String recentLockKey = mostRecentLockKey.get();
		return recentLockKey != null ? getLockResponse(recentLockKey) : null;
	}

	public static UnLockResponse getMostRecentUnLockResponse() {
		String recentLockKey = mostRecentLockKey.get();
		return recentLockKey != null ? getUnLockResponse(recentLockKey) : null;
	}
}

