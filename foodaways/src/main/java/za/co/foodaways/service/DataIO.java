package za.co.foodaways.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DataIO {
    public Connection connection;

    @PostConstruct
    public void createConnection() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/foodawaysdb",
                "root", "@ZXCp0001");
    }

    public Statement getStatement(){
        try {
            return this.connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
