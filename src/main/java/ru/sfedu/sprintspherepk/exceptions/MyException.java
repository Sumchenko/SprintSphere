package ru.sfedu.sprintspherepk.exceptions;

public class MyException extends RuntimeException {
    public MyException(String message, Throwable cause) {
        super(message, cause);
    }
}
