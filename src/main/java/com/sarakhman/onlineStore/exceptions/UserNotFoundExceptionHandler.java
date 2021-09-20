package com.sarakhman.onlineStore.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserNotFoundExceptionHandler{
    @ExceptionHandler(UsernameNotFoundException.class)
    public String userNotFound(UsernameNotFoundException e){
        return "userNotFound";
    }
}