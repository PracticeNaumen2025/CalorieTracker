package ru.naumen.calorietracker.exception;

/**
 * Исключение, выбрасываемое при попытке создать пользователя с уже существующим именем.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String formatted) {
    }
}
