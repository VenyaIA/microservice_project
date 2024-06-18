package com.ivanov.microservice_project.exception;

import java.io.IOException;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String string, IOException e) {
    }
}
