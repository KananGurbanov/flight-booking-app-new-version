package resources.application.properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDriver {
    public Connection getConnection(){
        try {
            return DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
