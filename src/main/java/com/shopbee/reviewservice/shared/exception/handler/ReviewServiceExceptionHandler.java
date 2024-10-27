package com.shopbee.reviewservice.shared.exception.handler;

import com.shopbee.reviewservice.shared.exception.ErrorResponse;
import com.shopbee.reviewservice.shared.exception.ReviewServiceException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ReviewServiceExceptionHandler implements ExceptionMapper<ReviewServiceException> {

    @Override
    public Response toResponse(ReviewServiceException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return Response.status(e.getResponse().getStatus()).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
    }
}
