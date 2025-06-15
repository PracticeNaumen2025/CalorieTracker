package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
