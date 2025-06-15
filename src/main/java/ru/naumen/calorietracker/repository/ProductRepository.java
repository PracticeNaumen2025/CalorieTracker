package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
