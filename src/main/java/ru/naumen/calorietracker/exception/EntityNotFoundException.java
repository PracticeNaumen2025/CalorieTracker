package ru.naumen.calorietracker.exception;

/**
 * Исключение, выбрасываемое при отсутствии искомой сущности в базе данных.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}