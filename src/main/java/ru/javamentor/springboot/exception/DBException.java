package ru.javamentor.springboot.exception;

public class DBException extends Exception {
    public DBException(Throwable throwable) {
        super(throwable);
    }
}
