package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;
    private boolean isBorrowed;

    public Book(String title, String author, String isbn, int publicationYear) {
        validateBookInput(title, author, isbn, publicationYear);
    
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.isBorrowed = false;
    }    

    public Book(ResultSet rs) throws SQLException {
        this.title = rs.getString("title");
        this.author = rs.getString("author");
        this.isbn = rs.getString("isbn");
        this.publicationYear = rs.getInt("publicationYear");
        this.isBorrowed = rs.getBoolean("isBorrowed");
    }

    private void validateBookInput(String title, String author, String isbn, int publicationYear) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty.");
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }
        if (publicationYear <= 0) {
            throw new IllegalArgumentException("Publication year must be a positive number.");
        }
        if (publicationYear < 1000 || publicationYear > 2023) {
            throw new IllegalArgumentException("Publication year must be between 1000 and 2023.");
        }
        if (!isbn.matches("[0-9\\-]+")) {
            throw new IllegalArgumentException("ISBN must contain only numbers and hyphens.");
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean isBorrowed) {
        this.isBorrowed = isBorrowed;
    }

    public String toString() {
        return "Book{" +
               "title='" + title + '\'' +
               ", author='" + author + '\'' +
               ", isbn='" + isbn + '\'' +
               ", publicationYear=" + publicationYear +
               ", isBorrowed=" + isBorrowed +
               '}';
    }
}