package com.example.KTGK.controller;

import com.example.KTGK.model.Student;
import com.example.KTGK.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final StudentService studentService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        if (studentService.existsByUsername(student.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username đã tồn tại!");
            return "redirect:/register";
        }
        if (studentService.existsByEmail(student.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
            return "redirect:/register";
        }

        studentService.register(student);
        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/login";
    }
}
