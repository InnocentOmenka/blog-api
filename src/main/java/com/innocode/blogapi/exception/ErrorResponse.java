package com.innocode.blogapi.exception;

import lombok.Data;

@Data
public class ErrorResponse<T> {
    private String status;
    private String statusCode;
    private T message;
}
