package com.codewitharjun.fullstackbackend.exception;
/* Created by Arjun Gautam */

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("Could not find the user with id "+ id);
    }
}
