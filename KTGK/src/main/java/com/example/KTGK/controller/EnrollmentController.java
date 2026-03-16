package com.example.KTGK.controller;

import com.example.KTGK.model.Enrollment;
import com.example.KTGK.model.Student;
import com.example.KTGK.service.EnrollmentService;
import com.example.KTGK.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;

    @PostMapping("/enroll/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public String enroll(@PathVariable Long courseId, Principal principal, RedirectAttributes redirectAttributes) {
        Student student = studentService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        try {
            enrollmentService.enroll(student, courseId);
            redirectAttributes.addFlashAttribute("success", "Đăng ký học phần thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/courses";
    }

    @GetMapping("/enroll/my-courses")
    @PreAuthorize("hasRole('STUDENT')")
    public String myCourses(Principal principal, Model model) {
        Student student = studentService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Enrollment> enrollments = enrollmentService.findByStudent(student);
        model.addAttribute("enrollments", enrollments);
        return "my-courses";
    }
}
