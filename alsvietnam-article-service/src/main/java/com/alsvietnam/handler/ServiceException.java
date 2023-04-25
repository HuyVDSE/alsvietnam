package com.alsvietnam.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 8:32 PM
 */

public class ServiceException extends ResponseStatusException {

    @Getter
    private Object data;

    public ServiceException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public ServiceException(String message, Object data, HttpStatus httpStatus) {
        super(httpStatus, message);
        this.data = data;
    }

    public ServiceException(String message, HttpStatus httpStatus) {
        super(httpStatus, message);
    }

}
