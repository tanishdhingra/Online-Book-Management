package manager;

import model.Book;
import model.User;
import util.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibraryManager {

    public LibraryManager() {
    }

    public void addBook(Book book) {
        String sql = "INSERT INTO books (isbn, title, author, publicationYear, isBorrowed) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setInt(4, book.getPublicationYear());
            pstmt.setBoolean(5, book.isBorrowed());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public void addUser(User user) {
        String sql = "INSERT INTO users (id, name, maxBorrowedBooks) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setInt(3, user.getMaxBorrowedBooks());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public void borrowBook(String userId, String isbn) {
        User user = findUserById(userId);
        Book book = findBookByIsbn(isbn);
    
        if (user == null || book == null || book.isBorrowed()) {
            System.out.println("Cannot borrow book. User or book not found, or book is already borrowed.");
            return;
        }
    
        String sqlCount = "SELECT COUNT(*) FROM books WHERE isBorrowed = true AND borrowerId = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmtCount = conn.prepareStatement(sqlCount)) {
            pstmtCount.setString(1, userId);
            try (ResultSet rs = pstmtCount.executeQuery()) {
                if (rs.next() && rs.getInt(1) >= user.getMaxBorrowedBooks()) {
                    System.out.println("User has already borrowed the maximum number of books.");
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return;
        }
    
        String sqlUpdate = "UPDATE books SET isBorrowed = true, borrowerId = ? WHERE isbn = ?";
    try (Connection conn = DatabaseUtils.getConnection();
         PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
        pstmtUpdate.setString(1, userId);
        pstmtUpdate.setString(2, isbn);
        pstmtUpdate.executeUpdate();
        System.out.println("Book borrowed successfully.");
    } catch (SQLException e) {
        System.out.println("SQL Error: " + e.getMessage());
    }
    }
    

    public void listAllBooks() {
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book(rs);
                System.out.println(book);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public void listAllUsers() {
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                User user = new User(rs);
                System.out.println(user);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public void returnBook(String userId, String isbn) {
        if (findBookByIsbn(isbn) == null || !isBookBorrowed(isbn)) {
            System.out.println("Cannot return book.");
            return;
        }
    
        String sql = "UPDATE books SET isBorrowed = false WHERE isbn = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            pstmt.executeUpdate();
            System.out.println("Book returned successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    

    public Book findBookByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return null;
    }

    public User findUserById(String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return null;
    }

    public void removeBook(String isbn) {
        String sql = "DELETE FROM books WHERE isbn = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("No book found with ISBN: " + isbn);
            } else {
                System.out.println("Book removed successfully.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    

    private boolean isBookBorrowed(String isbn) {
        String sql = "SELECT isBorrowed FROM books WHERE isbn = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("isBorrowed");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return false;
    }

    public void findBooksByName(String bookName) {
        String sql = "SELECT * FROM books WHERE title LIKE ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + bookName + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    if (!found) {
                        System.out.println("Books found:");
                        found = true;
                    }
                    Book book = new Book(rs);
                    System.out.println(book);
                }

                if (!found) {
                    System.out.println("No book found");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public void findBooksByAuthor(String authorName) {
        String sql = "SELECT * FROM books WHERE author LIKE ?";
    
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + authorName + "%");
    
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    if (!found) {
                        System.out.println("Books found by author '" + authorName + "':");
                        found = true;
                    }
                    Book book = new Book(rs);
                    System.out.println(book);
                }
    
                if (!found) {
                    System.out.println("No books found by author '" + authorName + "'.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    
    public void removeUser(String userId) {
        String sql = "DELETE FROM users WHERE id = ?";
    
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            int affectedRows = pstmt.executeUpdate();
    
            if (affectedRows > 0) {
                System.out.println("User removed successfully.");
            } else {
                System.out.println("No user found with ID: " + userId);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    
}