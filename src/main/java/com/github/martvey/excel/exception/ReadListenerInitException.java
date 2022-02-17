package com.github.martvey.excel.exception;

public class ReadListenerInitException extends RuntimeException{
    public ReadListenerInitException(String message) {
        super(message);
    }

    public ReadListenerInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
