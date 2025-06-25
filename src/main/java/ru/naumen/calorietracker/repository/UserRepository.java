package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.naumen.calorietracker.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.isDeleted = false")
    List<User> findAllActiveUsers();

    long countByIsDeletedFalse();
    long countByIsDeletedTrue();
    @Query("SELECT FUNCTION('DATE', u.createdAt), COUNT(u) FROM User u WHERE u.createdAt BETWEEN :from AND :to GROUP BY FUNCTION('DATE', u.createdAt) ORDER BY FUNCTION('DATE', u.createdAt)")
    List<Object[]> countRegistrationsByDateRange(LocalDateTime from, LocalDateTime to);
    List<User> findByIsDeletedFalse();
}
