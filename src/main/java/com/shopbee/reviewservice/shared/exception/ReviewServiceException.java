package com.shopbee.reviewservice.shared.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class ReviewServiceException extends WebApplicationException {

    public ReviewServiceException(String message, int status) {
        super(message, status);
    }

    public ReviewServiceException(String message, Response.Status status) {
        super(message, status);
    }
}
