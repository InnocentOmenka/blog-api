package com.innocode.blogapi.exception;

import com.innocode.blogapi.model.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleUserNotFoundException(UserNotFoundException exception) {
        ApiResponse<?> errorResponse = new ApiResponse<>();
        errorResponse.setStatus("FAILED");
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setData(null);
        return errorResponse;
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleUserAlreadyExistException(UserAlreadyExistException exception) {
        ApiResponse<?> errorResponse = new ApiResponse<>();
        errorResponse.setStatus("FAILED");
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setData(null);
        return errorResponse;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleBadCredentialsException(BadCredentialsException exception) {
        ApiResponse<?> errorResponse = new ApiResponse<>();
        errorResponse.setStatus("FAILED");
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setData(null);
        return errorResponse;
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleCommentNotFoundException(CommentNotFoundException exception) {
        ApiResponse<?> errorResponse = new ApiResponse<>();
        errorResponse.setStatus("FAILED");
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setData(null);
        return errorResponse;
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handlePostNotFoundException(PostNotFoundException exception) {
        ApiResponse<?> errorResponse = new ApiResponse<>();
        errorResponse.setStatus("FAILED");
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setData(null);
        return errorResponse;
    }
}
