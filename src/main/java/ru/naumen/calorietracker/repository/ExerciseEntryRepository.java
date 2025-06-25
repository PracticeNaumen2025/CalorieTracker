package ru.naumen.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.naumen.calorietracker.model.ExerciseEntry;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ExerciseEntryRepository extends JpaRepository<ExerciseEntry, Integer> {
    @Query("SELECT DISTINCT e.user.userId FROM ExerciseEntry e WHERE e.dateTime >= :since")
    List<Integer> findActiveUserIdsSince(@Param("since") LocalDateTime since);

    long countByDateTimeBetween(LocalDateTime from, LocalDateTime to);

    @Query("""
        select e.exercise.exerciseId, e.exercise.name, count(e)
        from ExerciseEntry e
        where e.dateTime between :from and :to
        group by e.exercise.exerciseId, e.exercise.name
        order by count(e) desc
    """)
    List<Object[]> findPopularExercises(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("""
        select sum(e.caloriesBurned)
        from ExerciseEntry e
        where e.dateTime between :from and :to
    """)
    BigDecimal sumCaloriesBurnedBetweenDates(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("""
        select e.user.userId, e.user.username, count(e), sum(e.caloriesBurned)
        from ExerciseEntry e
        where e.dateTime between :from and :to
        group by e.user.userId, e.user.username
        order by sum(e.caloriesBurned) desc
    """)
    List<Object[]> findTopUsersByCaloriesBurned(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("""
    select 
        case 
            when function('date_part', 'hour', e.dateTime) between 5 and 11 then 'Morning'
            when function('date_part', 'hour', e.dateTime) between 12 and 17 then 'Afternoon'
            when function('date_part', 'hour', e.dateTime) between 18 and 22 then 'Evening'
            else 'Night'
        end as timePeriod,
        count(e)
    from ExerciseEntry e
    where e.dateTime between :from and :to
    group by timePeriod
""")
    List<Object[]> countExercisesGroupedByTimeOfDay(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

}
