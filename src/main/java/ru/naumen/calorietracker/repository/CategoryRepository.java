package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
