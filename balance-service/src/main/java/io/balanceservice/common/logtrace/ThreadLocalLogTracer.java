package io.balanceservice.common.logtrace;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Component
@Slf4j
public class ThreadLocalLogTracer implements LogTracer {

	private static final String START_PREFIX = "-->";
	private static final String COMPLETE_PREFIX = "<--";
	private static final String EXCEPTION_PREFIX = "<X-";

	private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();
	private ThreadLocal<Boolean> isExceptionLogged = ThreadLocal.withInitial(() -> false);

	@Override
	public TraceStatus begin(String message) {
		isExceptionLogged.set(false);
		syncTraceId();
		TraceId traceId = getCurrentTraceId();
		Long startTimeMs = System.currentTimeMillis();
		log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

		return new TraceStatus(traceId, startTimeMs, message);
	}

	@Override
	public void end(Object result, TraceStatus status) {
		complete(result, status, null);
	}

	@Override
	public void exception(Object result, TraceStatus status, Exception exception) {
		if (!isExceptionLogged.get()) {
			log.info("[{}] {}{} time={}ms ex={}", status.getTraceId().getId(), addSpace(EXCEPTION_PREFIX, status.getTraceId().getLevel()),
				limitMessage(status.getMessage()), System.currentTimeMillis() - status.getStartTimeMs(), exception.toString());
			isExceptionLogged.set(true);
		}
		releaseTraceId();
		//		complete(result, status, exception);
	}

	private void complete(Object result, TraceStatus status, Exception exception) {
		long resultTimeMs = System.currentTimeMillis() - status.getStartTimeMs();
		TraceId traceId = status.getTraceId();
		if (exception == null) {
			log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()),
				limitMessage(status.getMessage()), resultTimeMs);
			//			log.info("[{}] Process Result {}", traceId.getId(), result);
		} else {
			log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EXCEPTION_PREFIX, traceId.getLevel()),
				limitMessage(status.getMessage()), resultTimeMs, exception.toString());
		}

		releaseTraceId();
	}

	private static String limitMessage(String message) {
		if (message == null) {
			return null;
		}
		return message.substring(0, Math.min(message.length(), 2000));
	}

	private void syncTraceId() {
		TraceId traceId = getCurrentTraceId();
		if (traceId == null) {
			traceIdHolder.set(new TraceId());
		} else {
			traceIdHolder.set(traceId.createNextId());
		}
	}

	private void releaseTraceId() {
		TraceId currentTraceId = getCurrentTraceId();
		if (isFirstLevelTrace(currentTraceId)) {
			removeTraceId();
			return;
		}
		setPreviousTraceId(currentTraceId);
	}

	private TraceId getCurrentTraceId() {
		return traceIdHolder.get();
	}

	private boolean isFirstLevelTrace(TraceId traceId) {
		return traceId.isFirstLevel();
	}

	private void removeTraceId() {
		traceIdHolder.remove();
	}

	private void setPreviousTraceId(TraceId currentTraceId) {
		TraceId previousTraceId = currentTraceId.createPreviousId();
		traceIdHolder.set(previousTraceId);
	}

	private static String addSpace(String prefix, int level) {
		return IntStream.range(0, level + 1)
			.mapToObj(i -> (i == level) ? "|" + prefix : "|   ")
			.collect(Collectors.joining());
	}
}
