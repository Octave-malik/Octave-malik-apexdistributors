import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class test {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/agricultural_input_distribution"; // Replace with your database URL
        String user = "root"; // Replace with your username
        String password = "nzioki"; // Replace with your password

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC Driver Loaded Successfully!");

            // Attempt to connect to the database
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                if (connection != null) {
                    System.out.println("Database connected successfully!");
                } else {
                    System.out.println("Failed to connect to the database.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found. Ensure the driver is in the classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed. Check the URL, username, and password.");
            e.printStackTrace();
        }
    }
}
