package com.example.KTGK.repository;

import com.example.KTGK.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByDeletedFalse(Pageable pageable);
    List<Course> findByDeletedFalse();
    List<Course> findByDeletedTrue();
    List<Course> findByNameContainingIgnoreCaseAndDeletedFalse(String keyword);
    Page<Course> findByNameContainingIgnoreCaseAndDeletedFalse(String keyword, Pageable pageable);
    Optional<Course> findByIdAndDeletedFalse(Long id);
}
