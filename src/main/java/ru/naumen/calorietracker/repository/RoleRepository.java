package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.calorietracker.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(String user);
}
