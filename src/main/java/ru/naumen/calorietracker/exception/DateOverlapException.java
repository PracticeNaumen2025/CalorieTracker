package ru.naumen.calorietracker.exception;

/**
 * Исключение, выбрасываемое при пересечении дат.
 * Например, при создании новой цели пользователя с перекрытием по времени.
 */
public class DateOverlapException extends RuntimeException {
    public DateOverlapException(String message) {
        super(message);
    }
}