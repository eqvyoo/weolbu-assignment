package com.weolbu.assignment.exception;

public class PhoneAlreadyExistsException extends RuntimeException{
    public PhoneAlreadyExistsException(String message){
        super(message);
    }
}
