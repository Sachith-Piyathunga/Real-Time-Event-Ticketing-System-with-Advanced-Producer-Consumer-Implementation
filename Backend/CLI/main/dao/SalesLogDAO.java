package main.dao;


// Importing the SalesLog model and List class for handling sales log data.
import main.models.SalesLog;
import java.util.List;


public interface SalesLogDAO  // Defining an interface SalesLogDAO that provides a blueprint for logging sales activities.
{
    void addLog(String log);  // Method to add a new log entry.
    List<SalesLog> getAllLogs();
}
