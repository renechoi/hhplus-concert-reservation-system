package io.apiorchestrationservice.common.logtrace.filter;


import static io.apiorchestrationservice.util.YmlLoader.*;

import java.io.IOException;

import io.apiorchestrationservice.common.logtrace.TraceStatus;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/17
 */
@RequiredArgsConstructor
@Slf4j
public class LoggingFilter implements Filter {

    private final RequestResponseLogger logger;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (isLoggingDisabled()) {
            chain.doFilter(request, response);
            return;
        }

        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest((HttpServletRequest) request);
        CachedBodyHttpServletResponse cachedResponse = new CachedBodyHttpServletResponse((HttpServletResponse) response);

        TraceStatus status = null;
        try {
            status = logger.startTrace(cachedRequest);
            chain.doFilter(cachedRequest, cachedResponse);
            logger.endTrace(status, cachedResponse);
            logger.logResponseBody(cachedResponse);
            cachedResponse.copyBodyToResponse();
        } catch (Exception e) {
            if (status != null) {
                logger.logException(status, e);
            }
            log.error("Exception in LoggingFilter: ", e);
        }
    }

    private boolean isLoggingDisabled() {
        return !ymlLoader().isLoggingFilterEnabled();
    }
}