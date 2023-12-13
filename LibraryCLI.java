import manager.LibraryManager;
import model.Book;
import model.User;

import java.util.Scanner;
import java.util.InputMismatchException;

public class LibraryCLI {
    private final LibraryManager libraryManager;
    private final Scanner scanner;

    public static void main(String[] args) {
        LibraryCLI cli = new LibraryCLI();
        cli.start();
    }

    public LibraryCLI() {
        this.libraryManager = new LibraryManager();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            int choice = getSafeIntInput();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    handleAddBook();
                    break;
                case 2:
                    handleAddUser();
                    break;
                case 3:
                    handleBorrowBook();
                    break;
                case 4:
                    handleReturnBook();
                    break;
                case 5:
                    libraryManager.listAllBooks();
                    break;
                case 6:
                    libraryManager.listAllUsers();
                    break;
                case 7:
                    handleRemoveBook();
                    break;
                case 8:
                    searchBooksByName();
                    break;
                case 9:
                    listBooksByAuthor();
                    break;
                case 10:
                    handleRemoveUser();
                    break;                
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║        Library Management System       ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("1. Add Book");
        System.out.println("2. Add User");
        System.out.println("3. Borrow Book");
        System.out.println("4. Return Book");
        System.out.println("5. List All Books");
        System.out.println("6. List All Users");
        System.out.println("7. Remove Book");
        System.out.println("8. Search Books By Name");
        System.out.println("9. List Books by Author");
        System.out.println("10. Remove User");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");

    }

    private int getSafeIntInput() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void handleAddBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author name: ");
        String author = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter publication year: ");
        int year = getSafeIntInput();
        scanner.nextLine();

        if (libraryManager.findBookByIsbn(isbn) != null) {
            System.out.println("A book with this ISBN already exists.");
            return;
        }
    
        try {
            Book book = new Book(title, author, isbn, year);
            libraryManager.addBook(book);
            System.out.println("Book added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void handleAddUser() {
        System.out.print("Enter user ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the maximum number of books to borrow: ");
        int maxBooks = getSafeIntInput();
        scanner.nextLine();

        if (libraryManager.findUserById(id) != null) {
            System.out.println("A user with this ID already exists.");
            return;
        }
    
        try {
            User user = new User(id, name, maxBooks);
            libraryManager.addUser(user);
            System.out.println("User added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void handleBorrowBook() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter ISBN of the book to borrow: ");
        String isbn = scanner.nextLine();
    
        try {
            libraryManager.borrowBook(userId, isbn);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    
    private void handleReturnBook() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter ISBN of the book to return: ");
        String isbn = scanner.nextLine();

        if (libraryManager.findUserById(userId) == null) {
            System.out.println("No user found with ID: " + userId);
            return;
        }
    
        try {
            libraryManager.returnBook(userId, isbn);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void handleRemoveBook() {
        System.out.print("Enter ISBN of the book to remove: ");
        String isbn = scanner.nextLine();
    
        try {
            libraryManager.removeBook(isbn);
            System.out.println("Book removed successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void searchBooksByName() {
        System.out.print("Enter book name to search: ");
        String bookName = scanner.nextLine();
        libraryManager.findBooksByName(bookName);
    }

    private void listBooksByAuthor() {
        System.out.print("Enter author name to list books: ");
        String authorName = scanner.nextLine();
        libraryManager.findBooksByAuthor(authorName);
    }

    private void handleRemoveUser() {
        System.out.print("Enter user ID to remove: ");
        String userId = scanner.nextLine();
        libraryManager.removeUser(userId);
    }
    
}