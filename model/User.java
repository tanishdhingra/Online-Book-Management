package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String id;
    private String name;
    private int maxBorrowedBooks;

    public User(ResultSet rs) throws SQLException {
        this.id = rs.getString("id");
        this.name = rs.getString("name");
        this.maxBorrowedBooks = rs.getInt("maxBorrowedBooks");
    }

    public User(String id, String name, int maxBorrowedBooks) {
        validateUserInput(id, name, maxBorrowedBooks);
        
        boolean isNumeric = true;

for (int i = 0; i < id.length(); i++) {
    char c = id.charAt(i);
    if (c < '0' || c > '9') {
        isNumeric = false;
        break;
    }
}

if (!isNumeric) {
    throw new IllegalArgumentException("User ID must contain only integers.");
}


        this.id = id;
        this.name = name;
        this.maxBorrowedBooks = maxBorrowedBooks;
    }

    private void validateUserInput(String id, String name, int maxBorrowedBooks) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (maxBorrowedBooks <= 0) {
            throw new IllegalArgumentException("Maximum number of borrowed books must be positive.");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxBorrowedBooks() {
        return maxBorrowedBooks;
    }

    public void setMaxBorrowedBooks(int maxBorrowedBooks) {
        this.maxBorrowedBooks = maxBorrowedBooks;
    }

    public String toString() {
        return "User{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", maxBorrowedBooks=" + maxBorrowedBooks +
               '}';
    }
}