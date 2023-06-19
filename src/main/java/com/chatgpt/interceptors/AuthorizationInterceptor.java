package com.chatgpt.interceptors;

import com.chatgpt.services.AuthCheckerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Autowired
    AuthCheckerService authCheckerService;

    @Value("${auth.skip}")
    boolean skipAuth;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        if (skipAuth) {
            request.setAttribute("vkUserId", "0");
            return true;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            boolean isSignSuccess = authCheckerService.checkAuthorizationHeader(
                    authCheckerService.splitBearer(authorizationHeader)
            );

            if (isSignSuccess) {
                request.setAttribute(
                        "vkUserId",
                        authCheckerService.getVkUserId(
                                authCheckerService.splitBearer(authorizationHeader)
                        )
                );

                return true;
            }

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        return false;
    }
}
