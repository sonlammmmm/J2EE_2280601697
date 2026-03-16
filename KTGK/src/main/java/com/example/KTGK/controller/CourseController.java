package com.example.KTGK.controller;

import com.example.KTGK.model.Course;
import com.example.KTGK.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping({"/", "/home", "/courses"})
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Course> coursePage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            coursePage = courseService.searchByName(keyword.trim(), pageable);
            model.addAttribute("keyword", keyword);
        } else {
            coursePage = courseService.findAll(pageable);
        }

        model.addAttribute("coursePage", coursePage);
        return "home";
    }

    @GetMapping("/courses/search")
    public String search(@RequestParam String keyword,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Course> coursePage = courseService.searchByName(keyword, pageable);
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("keyword", keyword);
        return "home";
    }
}
