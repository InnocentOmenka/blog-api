package com.innocode.blogapi.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) { super(message);}
}
