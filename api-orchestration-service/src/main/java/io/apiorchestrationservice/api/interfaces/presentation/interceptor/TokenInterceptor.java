package io.apiorchestrationservice.api.interfaces.presentation.interceptor;

import static org.apache.commons.lang.StringUtils.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import io.apiorchestrationservice.api.application.facade.TokenValidationFacade;
import io.apiorchestrationservice.common.annotation.ValidatedToken;
import io.apiorchestrationservice.common.exception.definitions.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */

@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

	private final TokenValidationFacade tokenValidationFacade;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			ValidatedToken validatedToken = method.getMethodAnnotation(ValidatedToken.class);

			if (validatedToken != null) {
				String token = request.getHeader("X-Queue-Token");

				if (isEmpty(token)) {
					throw new InvalidTokenException();
				}

				if (!tokenValidationFacade.validateQueueToken(token)) {
					throw new InvalidTokenException();
				}
			}
		}
		return true;
	}
}