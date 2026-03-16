package com.example.KTGK.repository;

import com.example.KTGK.model.Enrollment;
import com.example.KTGK.model.Student;
import com.example.KTGK.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND e.course.deleted = false")
    List<Enrollment> findByStudent(@Param("student") Student student);
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
    boolean existsByStudentAndCourse(Student student, Course course);
}
