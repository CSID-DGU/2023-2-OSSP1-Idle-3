package com.almostThere.domain.user.ouath;

import com.almostThere.global.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

//    private static final Logger logger = Logger.getLogger(JwtAuthenticationEntryPoint.class);

    @Autowired
    public JwtAuthenticationEntryPoint(
        @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException ex) throws IOException {
        log.error("User is unauthorised. Routing from the entry point{} {}", response.getStatus(), ex);
        //검색 필요 !!
        if (request.getAttribute("javax.servlet.error.exception") != null) {
            Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
            resolver.resolveException(request, response, null, (Exception) throwable);
        }
        /**
         * 유저 인증 중 Error = 401
         */
        if (!response.isCommitted()) {
            response.sendError(ErrorCode.NOT_AUTHENTICATION.getCode(),
                ErrorCode.NOT_AUTHORIZATION.getMessage());
        }
    }
}
