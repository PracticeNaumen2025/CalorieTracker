package ru.naumen.calorietracker.config;

import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация OpenAPI.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Кастомизатор операций OpenAPI, удаляющий параметр "sort" из запросов.
     */
    @Bean
    public OperationCustomizer removeSortParam() {
        return (operation, handlerMethod) -> {
            List<Parameter> params = operation.getParameters();
            if (params != null) {
                params.removeIf(p -> "sort".equals(p.getName()));
            }
            return operation;
        };
    }
}
