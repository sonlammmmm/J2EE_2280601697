package com.example.Lab03.controller;

import com.example.Lab03.model.Book;
import com.example.Lab03.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Hiển thị danh sách sách
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    // Hiển thị form thêm sách
    @GetMapping("/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    // Thêm sách mới
    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book) {
        bookService.addBook(book);
        return "redirect:/books";
    }

    // Hiển thị form sửa sách - Đã sửa Long thành int
    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable("id") int id, Model model) {
        bookService.getBookById(id).ifPresent(book -> model.addAttribute("book", book));
        return "edit-book";
    }

    // Cập nhật sách
    @PostMapping("/edit")
    public String updateBook(@ModelAttribute("book") Book book) {
        bookService.updateBook(book);
        return "redirect:/books";
    }

    // Xóa sách - Đã sửa Long thành int
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}