package com.allan.library_management.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.allan.library_management.model.IssuedBook;

@Repository
public class IssuedBookRepository {

    private final JdbcTemplate jdbcTemplate;

    public IssuedBookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<IssuedBook> issuedBookRowMapper =
            (rs, rowNum) -> {

        IssuedBook issuedBook = new IssuedBook();

        issuedBook.setId(rs.getInt("id"));
        issuedBook.setBookId(rs.getInt("book_id"));
        issuedBook.setStudentName(rs.getString("student_name"));
        issuedBook.setIssueDate(
                rs.getDate("issue_date").toLocalDate());

        if (rs.getDate("return_date") != null) {
            issuedBook.setReturnDate(
                    rs.getDate("return_date").toLocalDate());
        }

        issuedBook.setStatus(rs.getString("status"));

        return issuedBook;
    };

    // Issue a book
    public int issueBook(IssuedBook issuedBook) {

        String sql = """
                INSERT INTO issued_books
                (book_id, student_name, issue_date, status)
                VALUES (?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                issuedBook.getBookId(),
                issuedBook.getStudentName(),
                issuedBook.getIssueDate(),
                "ISSUED"
        );
    }

    // Get all issued book records
    public List<IssuedBook> findAll() {

        String sql = "SELECT * FROM issued_books";

        return jdbcTemplate.query(sql, issuedBookRowMapper);
    }

    // Find a particular issue record
    public IssuedBook findById(int id) {

        String sql = "SELECT * FROM issued_books WHERE id = ?";

        List<IssuedBook> records = jdbcTemplate.query(
                sql,
                issuedBookRowMapper,
                id
        );

        return records.isEmpty() ? null : records.get(0);
    }

    // Mark a book as returned
    public int returnBook(int id) {

        String sql = """
                UPDATE issued_books
                SET return_date = ?, status = 'RETURNED'
                WHERE id = ? AND status = 'ISSUED'
                """;

        return jdbcTemplate.update(
                sql,
                java.sql.Date.valueOf(java.time.LocalDate.now()),
                id
        );
    }

    // Find the latest issue record for a student and book
public IssuedBook findLatestIssuedBook(int bookId, String studentName) {

    String sql = """
            SELECT * FROM issued_books
            WHERE book_id = ? AND student_name = ?
            ORDER BY id DESC
            LIMIT 1
            """;

    List<IssuedBook> records = jdbcTemplate.query(
            sql,
            issuedBookRowMapper,
            bookId,
            studentName
    );

    return records.isEmpty() ? null : records.get(0);
}
}