package ru.naumen.calorietracker.mapper;

import org.springframework.data.domain.Page;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.PageImpl;
import ru.naumen.calorietracker.dto.CategoryResponse;
import ru.naumen.calorietracker.model.Category;
import ru.naumen.calorietracker.model.elastic.CategorySearchDocument;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    List<CategoryResponse> toResponseList(List<CategorySearchDocument> category);
    Category toEntity(CategoryResponse categoryResponse);

    default Page<CategoryResponse> toResponsePage(Page<CategorySearchDocument> page){
        List<CategorySearchDocument> categorySearchDocuments = page.getContent();
        List<CategoryResponse> categories = toResponseList(categorySearchDocuments);
        return new PageImpl<>(categories, page.getPageable(), page.getTotalElements());
    }
}
