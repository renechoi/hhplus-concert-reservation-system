package io.reservationservice.common.logtrace.filter;




import static io.reservationservice.util.YmlLoader.*;

import java.io.IOException;

import io.reservationservice.common.logtrace.TraceStatus;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


/**
 * @author : Rene Choi
 * @since : 2024/07/17
 */

@RequiredArgsConstructor
public class LoggingFilter implements Filter {

    private final RequestResponseLogger logger;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isLoggingDisabled() || isExcludedRequest((HttpServletRequest) request)) {
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
            chain.doFilter(request, response);
        }
    }

    private boolean isLoggingDisabled() {
        return !ymlLoader().isLoggingFilterEnabled();
    }

    private boolean isExcludedRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/reservation/actuator") || uri.startsWith("/reservation/prometheus");
    }
}