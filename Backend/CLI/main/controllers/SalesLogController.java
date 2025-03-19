package main.controllers;

import main.dao.impl.SalesLogDAOImpl;
import main.models.SalesLog;

// Controller class for managing and displaying sales logs
public class SalesLogController {
    // Displays the sales logs in a structured format
    public void salesLog() {
        System.out.println("\n----  Configure System Parameters  ----");
        System.out.println("+-------------------------+-----------------------------------------------------------------+");
        System.out.println("| Log Time and Date       | Log                                                             |");
        System.out.println("+-------------------------+-----------------------------------------------------------------+");
        // Retrieve all sales logs
        for (SalesLog salesLog : new SalesLogDAOImpl().getAllLogs()) {
            System.out.printf(
                    "| %-23s | %-63s |%n", // Format for time, date, and log description
                    salesLog.getTimeAndDate(), // Log timestamp
                    salesLog.getLog() // Log message
            );
        }
        System.out.println("+-------------------------+-----------------------------------------------------------------+");
    }
}
