package com.allan.library_management.model;

import java.time.LocalDate;

public class IssuedBook {

    private int id;
    private int bookId;
    private String studentName;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private String status;

    public IssuedBook() {
    }

    public IssuedBook(int id, int bookId, String studentName,
                      LocalDate issueDate, LocalDate returnDate,
                      String status) {
        this.id = id;
        this.bookId = bookId;
        this.studentName = studentName;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}