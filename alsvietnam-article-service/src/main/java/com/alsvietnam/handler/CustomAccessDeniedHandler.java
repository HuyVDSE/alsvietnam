package com.alsvietnam.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Duc_Huy
 * Date: 9/4/2022
 * Time: 6:58 PM
 */

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private HandlerExceptionResolver resolver;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    public void setResolver(HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) {
        // delegate handle exception for @ExceptionHandler
        resolver.resolveException(request, response, null, exception);
    }
}
