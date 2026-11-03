package com.allan.library_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allan.library_management.model.IssueBookRequest;
import com.allan.library_management.model.IssuedBook;
import com.allan.library_management.service.LibraryService;

@RestController
@RequestMapping("/api/issued-books")
public class IssuedBookController {

    private final LibraryService libraryService;

    public IssuedBookController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // Issue a book
    @PostMapping
    public ResponseEntity<IssuedBook> issueBook(
            @RequestBody IssueBookRequest request) {

        if (request.getBookId() == null || request.getBookId() <= 0) {
            throw new IllegalArgumentException("Valid book ID is required");
        }

        IssuedBook issuedBook = libraryService.issueBook(
                request.getBookId(),
                request.getStudentName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(issuedBook);
    }

    // View all issued books
    @GetMapping
    public List<IssuedBook> getAllIssuedBooks() {
        return libraryService.getAllIssuedBooks();
    }

    // Return a book
    @PutMapping("/{id}/return")
    public ResponseEntity<IssuedBook> returnBook(@PathVariable int id) {

        IssuedBook returnedBook = libraryService.returnBook(id);

        return ResponseEntity.ok(returnedBook);
    }
}