package com.allan.library_management.service;

import com.allan.library_management.model.Book;
import com.allan.library_management.model.IssuedBook;
import com.allan.library_management.repository.BookRepository;
import com.allan.library_management.repository.IssuedBookRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final IssuedBookRepository issuedBookRepository;

    public LibraryService(BookRepository bookRepository,
                          IssuedBookRepository issuedBookRepository) {
        this.bookRepository = bookRepository;
        this.issuedBookRepository = issuedBookRepository;
    }

    // Issue a book to a student
    @Transactional
    public IssuedBook issueBook(int bookId, String studentName) {

        if (studentName == null || studentName.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Student name is required"
            );
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Book not found"
                ));

        if (bookRepository.decreaseAvailableCopies(bookId) == 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No copies of this book are available"
            );
        }

        IssuedBook issuedBook = new IssuedBook();

        issuedBook.setBookId(bookId);
        issuedBook.setStudentName(studentName.trim());
        issuedBook.setIssueDate(LocalDate.now());
        issuedBook.setStatus("ISSUED");

        issuedBookRepository.issueBook(issuedBook);

        return issuedBookRepository.findLatestIssuedBook(bookId, studentName.trim());
    }

    // View all borrowing records
    public List<IssuedBook> getAllIssuedBooks() {
        return issuedBookRepository.findAll();
    }

    // Return a borrowed book
    @Transactional
    public IssuedBook returnBook(int issueId) {

        IssuedBook issuedBook = issuedBookRepository.findById(issueId);

        if (issuedBook == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Issue record not found"
            );
        }

        if (!"ISSUED".equals(issuedBook.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This book has already been returned"
            );
        }

        int updated = issuedBookRepository.returnBook(issueId);

        if (updated == 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Unable to return this book"
            );
        }

        bookRepository.increaseAvailableCopies(issuedBook.getBookId());

        return issuedBookRepository.findById(issueId);
    }
}