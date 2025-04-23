package com.example.toanyone.global.error;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {

    public abstract HttpStatus getStatus();

    public abstract String getMessage();

}
