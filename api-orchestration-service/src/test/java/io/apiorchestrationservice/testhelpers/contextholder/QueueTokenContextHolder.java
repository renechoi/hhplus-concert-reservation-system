package io.apiorchestrationservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public class QueueTokenContextHolder implements TestDtoContextHolder{


	private static final ConcurrentHashMap<Long, String> tokenMap = new ConcurrentHashMap<>();
	private static final AtomicReference<String> mostRecentToken = new AtomicReference<>();

	public static void initFields() {
		tokenMap.clear();
		mostRecentToken.set(null);
	}

	public static String getToken(Long userId) {
		return tokenMap.get(userId);
	}

	public static void putToken(Long userId, String token) {
		tokenMap.put(userId, token);
		mostRecentToken.set(token);
	}

	public static String getMostRecentToken() {
		return mostRecentToken.get();
	}
}
