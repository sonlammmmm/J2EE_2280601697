package com.example.KTGK.service;

import com.example.KTGK.model.Course;
import com.example.KTGK.model.Enrollment;
import com.example.KTGK.model.Student;
import com.example.KTGK.repository.CourseRepository;
import com.example.KTGK.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    public void enroll(Student student, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new RuntimeException("Already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDate.now());
        enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> findByStudent(Student student) {
        return enrollmentRepository.findByStudent(student);
    }

    public boolean isEnrolled(Student student, Course course) {
        return enrollmentRepository.existsByStudentAndCourse(student, course);
    }
}
