import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/agricultural_input_distribution";
        String user = "root";
        String password = "nzioki";
        return DriverManager.getConnection(url, user, password);
    }
}
