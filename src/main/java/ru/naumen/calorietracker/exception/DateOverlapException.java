package ru.naumen.calorietracker.exception;

public class DateOverlapException extends RuntimeException {
    public DateOverlapException(String message) {
        super(message);
    }
}