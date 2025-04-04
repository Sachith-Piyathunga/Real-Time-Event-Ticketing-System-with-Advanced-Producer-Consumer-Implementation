package main.dao.impl;

import main.dao.SalesLogDAO;
import main.db.SQLiteConnection;
import main.models.SalesLog;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

 // Implements the SalesLogDAO interface
public class SalesLogDAOImpl implements SalesLogDAO {
    private static final Connection connection = SQLiteConnection.getInstance().getConnection();

    public SalesLogDAOImpl() {
        try {
            Statement stmt = connection.createStatement();

            // Create the "sales_log" table with fields for sale ID, date/time, and log details
            stmt.execute("CREATE TABLE IF NOT EXISTS sales_log (\n" +
                    "    sale_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    date_time TEXT NOT NULL,\n" +
                    "    log TEXT NOT NULL\n" +
                    ");");

        } catch (SQLException e) { // Log any SQL exceptions
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addLog(String log) {
        String query = "INSERT INTO sales_log(date_time, log) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Create a date formatter for the log timestamp
            SimpleDateFormat formatter = new SimpleDateFormat("ss:mm:HH - dd/MM/yyyy");

            // Get the current date and time
            String formattedDateTime = formatter.format(new Date());

            preparedStatement.setString(1, formattedDateTime);
            preparedStatement.setString(2, log);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<SalesLog> getAllLogs() {
        List<SalesLog> salesLog = new ArrayList<>();
        String query = "SELECT * FROM sales_log;";

        try {   // Statement to execute the query
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                salesLog.add(new SalesLog(resultSet.getInt("sale_id"),
                        resultSet.getString("date_time"),
                        resultSet.getString("log")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return salesLog;
    }
}
