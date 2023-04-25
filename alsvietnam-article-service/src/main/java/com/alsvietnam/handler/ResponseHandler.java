package com.alsvietnam.handler;

import com.alsvietnam.models.dtos.payment.IpnVnPayResponse;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.models.wrapper.StringResponseWrapper;
import com.alsvietnam.models.wrapper.ValidationErrorsWrapper;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 9:14 PM
 */

@ControllerAdvice
public class ResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object data, @NonNull MethodParameter methodParameter, @NonNull MediaType mediaType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> aClass, @NonNull ServerHttpRequest serverHttpRequest,
                                  @NonNull ServerHttpResponse serverHttpResponse) {
        if (data instanceof Resource) return data;
        if (data instanceof StringResponseWrapper) return data;
        if (data instanceof ObjectResponseWrapper) return data;
        if (data instanceof ValidationErrorsWrapper) return data;
        if (data instanceof String && ((String) data).contains("openapi")) return data;
        if (data instanceof TreeMap) return data;
        if (data instanceof LinkedHashMap) return data;
        if (data instanceof IpnVnPayResponse) return data;
        return ObjectResponseWrapper.builder().status(1).data(data).build();
    }

}
