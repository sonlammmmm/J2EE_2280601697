package com.example.KTGK.service;

import com.example.KTGK.model.Course;
import com.example.KTGK.repository.CourseRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private static final String UPLOAD_DIR = "uploads/courses/";

    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findByDeletedFalse(pageable);
    }

    public List<Course> findAll() {
        return courseRepository.findByDeletedFalse();
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findByIdAndDeletedFalse(id);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public void softDelete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));
        course.setDeleted(true);
        courseRepository.save(course);
    }

    public void restore(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));
        course.setDeleted(false);
        courseRepository.save(course);
    }

    public List<Course> findDeleted() {
        return courseRepository.findByDeletedTrue();
    }

    public List<Course> searchByName(String keyword) {
        return courseRepository.findByNameContainingIgnoreCaseAndDeletedFalse(keyword);
    }

    public Page<Course> searchByName(String keyword, Pageable pageable) {
        return courseRepository.findByNameContainingIgnoreCaseAndDeletedFalse(keyword, pageable);
    }

    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID() + extension;
        Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/courses/" + filename;
    }
}
