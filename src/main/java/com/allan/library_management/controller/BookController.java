package com.allan.library_management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allan.library_management.model.Book;
import com.allan.library_management.repository.BookRepository;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // 1. Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // 2. Get a book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id) {

        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Add a new book
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {

        if (book.getTitle() == null || book.getTitle().isBlank()
                || book.getAuthor() == null || book.getAuthor().isBlank()
                || book.getTotalCopies() <= 0) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid book details"));
        }

        book.setAvailableCopies(book.getTotalCopies());

        try {
            int generatedId = bookRepository.addBook(book);
            book.setId(generatedId);

            return ResponseEntity.status(HttpStatus.CREATED).body(book);

        } catch (org.springframework.dao.DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "ISBN already exists"));
        }
    }

    // 4. Update a book
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable int id,
            @RequestBody Book book) {

        if (book.getTitle() == null || book.getTitle().isBlank()
                || book.getAuthor() == null || book.getAuthor().isBlank()
                || book.getTotalCopies() <= 0) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid book details"));
        }

        var existingBook = bookRepository.findById(id);

        if (existingBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        int issuedCopies = existingBook.get().getTotalCopies()
                - existingBook.get().getAvailableCopies();

        if (book.getTotalCopies() < issuedCopies) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message",
                            "Total copies cannot be less than issued copies"));
        }

        book.setAvailableCopies(book.getTotalCopies() - issuedCopies);

        int updated = bookRepository.updateBook(id, book);

        return ResponseEntity.ok(book);
    }

    // 5. Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable int id) {

        if (bookRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            bookRepository.deleteBook(id);
            return ResponseEntity.ok(
                    Map.of("message", "Book deleted successfully"));

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message",
                            "Cannot delete a book that has borrowing records"));
        }
    }
}