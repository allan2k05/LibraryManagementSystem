package com.allan.library_management.model;

public class IssueBookRequest {

    private Integer bookId;
    private String studentName;

    public IssueBookRequest() {
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}