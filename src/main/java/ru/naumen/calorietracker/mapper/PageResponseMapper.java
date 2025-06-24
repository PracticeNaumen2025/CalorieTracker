package ru.naumen.calorietracker.mapper;

import org.springframework.data.domain.Page;
import ru.naumen.calorietracker.dto.PageResponse;

public class PageResponseMapper {

    public static <T> PageResponse<T> toPageResponse(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast(),
                page.getNumberOfElements()
        );
    }
}
