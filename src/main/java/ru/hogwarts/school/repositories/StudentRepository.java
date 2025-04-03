package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int from, int to);

    Student findById(long id);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer getStudentCount();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Double getAverageStudentAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudent();
}
