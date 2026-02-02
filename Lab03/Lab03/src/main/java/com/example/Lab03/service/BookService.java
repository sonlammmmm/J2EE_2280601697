package com.example.Lab03.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Lab03.model.Book;

@Service
public class BookService {

    private List<Book> books = new ArrayList<>();
    private int nextId = 1;

    public BookService() {
        books.add(new Book(nextId++, "Java cơ bản", "Nguyễn Văn A", 50000));
        books.add(new Book(nextId++, "Spring Boot", "Trần Văn B", 75000));
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public void addBook(Book book) {
        book.setId(nextId++);
        books.add(book);
    }

    public Optional<Book> getBookById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst();
    }

    public void updateBook(Book updatedBook) {
        books.stream()
                .filter(book -> book.getId() == updatedBook.getId())
                .findFirst()
                .ifPresent(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                    book.setPrice(updatedBook.getPrice());
                });
    }

    public void deleteBook(int id) {
        books.removeIf(book -> book.getId() == id);
    }
}