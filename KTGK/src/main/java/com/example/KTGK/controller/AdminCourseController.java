package com.example.KTGK.controller;

import com.example.KTGK.model.Course;
import com.example.KTGK.service.CategoryService;
import com.example.KTGK.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "admin/course-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/course-form";
    }

    @PostMapping("/create")
    public String create(@RequestParam String name,
                         @RequestParam int credits,
                         @RequestParam(required = false) String lecturer,
                         @RequestParam(required = false) Long categoryId,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         RedirectAttributes redirectAttributes) {
        try {
            Course course = new Course();
            course.setName(name);
            course.setCredits(credits);
            course.setLecturer(lecturer);
            if (categoryId != null) {
                categoryService.findById(categoryId).ifPresent(course::setCategory);
            }
            String imageUrl = courseService.uploadImage(imageFile);
            if (imageUrl != null) {
                course.setImage(imageUrl);
            }
            courseService.save(course);
            redirectAttributes.addFlashAttribute("success", "Thêm học phần thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi tải ảnh: " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));
        model.addAttribute("course", course);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/course-form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam int credits,
                         @RequestParam(required = false) String lecturer,
                         @RequestParam(required = false) Long categoryId,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         RedirectAttributes redirectAttributes) {
        try {
            Course existing = courseService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));
            existing.setName(name);
            existing.setCredits(credits);
            existing.setLecturer(lecturer);
            if (categoryId != null) {
                categoryService.findById(categoryId).ifPresent(existing::setCategory);
            } else {
                existing.setCategory(null);
            }
            String imageUrl = courseService.uploadImage(imageFile);
            if (imageUrl != null) {
                existing.setImage(imageUrl);
            }
            courseService.save(existing);
            redirectAttributes.addFlashAttribute("success", "Cập nhật học phần thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi tải ảnh: " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        courseService.softDelete(id);
        redirectAttributes.addFlashAttribute("success", "Đã chuyển học phần vào thùng rác!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/trash")
    public String trash(Model model) {
        model.addAttribute("courses", courseService.findDeleted());
        return "admin/course-trash";
    }

    @PostMapping("/restore/{id}")
    public String restore(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        courseService.restore(id);
        redirectAttributes.addFlashAttribute("success", "Khôi phục học phần thành công!");
        return "redirect:/admin/courses/trash";
    }
}
