package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.naumen.calorietracker.model.UserGoal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserGoalRepository extends JpaRepository<UserGoal, Integer> {
    @Query("""
    select g
    from UserGoal g
    where g.user.userId = :userId
      and (cast(:endDate as date)  is null or g.startDate <= cast(:endDate as date))
      and (g.endDate is null or g.endDate >= :startDate)
""")
    List<UserGoal> findOverlappingGoals(
            @Param("userId") Integer userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate")   LocalDate endDate
    );
    List<UserGoal> findByUserUserId(Integer ownerUserId);

    @Query("""
        select g
        from UserGoal g
        where g.user.userId = :userId
          and g.startDate <= :date
          and (g.endDate is null or g.endDate >= :date)
    """)
    Optional<UserGoal> findActiveGoalByDate(
            @Param("userId") Integer userId,
            @Param("date")   LocalDate date
    );
}
