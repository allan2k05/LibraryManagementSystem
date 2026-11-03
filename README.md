# Library Management System REST API

## Project Overview

The Library Management System REST API is a Java-based application developed using Spring Boot and MySQL. It helps manage library books and their issue and return operations through REST APIs.

The application uses JDBC to communicate with the MySQL database.

## Technologies Used

- Java 17
- Spring Boot
- Spring Web
- JDBC (JdbcTemplate)
- MySQL
- Maven
- Postman
- VS Code

## Features

### Book Management
- Add new books.
- View all books.
- View a book using its ID.
- Update book details.
- Delete books.

### Book Issue and Return
- Issue books to students.
- View all issued books.
- Return issued books.
- Automatically update available copies.
- Prevent issuing books when no copies are available.
- Prevent returning the same book twice.

## Database

Database name: `library_management`

### Table: books

| Column | Description |
|---|---|
| id | Unique book ID |
| title | Book title |
| author | Author name |
| isbn | ISBN number |
| total_copies | Total copies |
| available_copies | Available copies |

### Table: issued_books

| Column | Description |
|---|---|
| id | Issue record ID |
| book_id | Referenced book ID |
| student_name | Student name |
| issue_date | Date of issue |
| return_date | Date of return |
| status | ISSUED or RETURNED |

## REST API Endpoints

### Book APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/books` | View all books |
| GET | `/api/books/{id}` | View book by ID |
| POST | `/api/books` | Add a book |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |

### Issued Book APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/issued-books` | Issue a book |
| GET | `/api/issued-books` | View issued books |
| PUT | `/api/issued-books/{id}/return` | Return a book |

## How to Run the Project

1. Install Java 17 and MySQL.
2. Create the `library_management` database.
3. Create the `books` and `issued_books` tables.
4. Configure the database URL, username and password in `application.properties`.
5. Open the project in VS Code.
6. Run the application using:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs at:

`http://localhost:8081`

## API Testing

All REST API endpoints were tested using Postman.

The application was tested for successful book operations, issuing and returning books, unavailable book handling, and duplicate return prevention.

## Author

Developed as part of a Java Internship project.
