package util;

import java.sql.*;

public class DatabaseUtils {
    private static final String URL = "jdbc:mysql://localhost:3306/LibrarySystem";
    private static final String USER = "root";
    private static final String PASSWORD = "Xender@0000";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}