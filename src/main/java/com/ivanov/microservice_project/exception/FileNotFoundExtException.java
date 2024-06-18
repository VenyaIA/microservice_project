package com.ivanov.microservice_project.exception;

public class FileNotFoundExtException extends RuntimeException {

    public FileNotFoundExtException(String message, Throwable cause) {
        super(message, cause);
    }
}

