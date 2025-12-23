package com.example.authService.Exception;

public class AuthException extends RuntimeException {
    private String errorCode;
    private int statusCode;

    public AuthException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public AuthException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = 400;
    }

    public AuthException(String message, String errorCode, int statusCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
