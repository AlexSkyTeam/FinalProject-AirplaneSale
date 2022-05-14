package org.example.exception;

public class UnsupportedImageTypeException extends RuntimeException {
    public UnsupportedImageTypeException(String message) {
        super(message);
    }
}
