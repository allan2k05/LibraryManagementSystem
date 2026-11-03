package com.allan.library_management.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.allan.library_management.model.Book;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Book> bookRowMapper = (rs, rowNum) -> {
        Book book = new Book();

        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        book.setTotalCopies(rs.getInt("total_copies"));
        book.setAvailableCopies(rs.getInt("available_copies"));

        return book;
    };

    // Get all books
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";

        return jdbcTemplate.query(sql, bookRowMapper);
    }

    // Get a book by ID
    public Optional<Book> findById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";

        List<Book> books = jdbcTemplate.query(
            sql,
            bookRowMapper,
            id
        );

        return books.stream().findFirst();
    }

    // Add a new book
    public int addBook(Book book) {

        String sql = """
            INSERT INTO books
            (title, author, isbn, total_copies, available_copies)
            VALUES (?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getTotalCopies());
            ps.setInt(5, book.getAvailableCopies());

            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // Update an existing book
    public int updateBook(int id, Book book) {

        String sql = """
            UPDATE books
            SET title = ?, author = ?, isbn = ?,
                total_copies = ?, available_copies = ?
            WHERE id = ?
            """;

        return jdbcTemplate.update(
            sql,
            book.getTitle(),
            book.getAuthor(),
            book.getIsbn(),
            book.getTotalCopies(),
            book.getAvailableCopies(),
            id
        );
    }

    // Delete a book
    public int deleteBook(int id) {

        String sql = "DELETE FROM books WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }

    // Decrease available copies when a book is issued
public int decreaseAvailableCopies(int bookId) {

    String sql = """
            UPDATE books
            SET available_copies = available_copies - 1
            WHERE id = ? AND available_copies > 0
            """;

    return jdbcTemplate.update(sql, bookId);
}

// Increase available copies when a book is returned
public int increaseAvailableCopies(int bookId) {

    String sql = """
            UPDATE books
            SET available_copies = available_copies + 1
            WHERE id = ?
            """;

    return jdbcTemplate.update(sql, bookId);
}
}