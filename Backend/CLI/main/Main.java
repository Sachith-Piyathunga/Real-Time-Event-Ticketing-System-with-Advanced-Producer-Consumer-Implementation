package main;

import main.controllers.ConfigureSystemParametersController;
import main.controllers.SalesLogController;
import main.controllers.TicketManagementController;
import main.controllers.VendorManagementController;
import main.dao.impl.SystemConfigDAOImpl;
import main.db.SQLiteConnection;
import main.util.SystemConfig;
import main.util.UserInputGetCollection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Entry point for the Real-Time Event Ticketing System application.
 * Manages the main menu, periodic system updates, and user interaction.
 */
public class Main {

    // Utility for capturing user input
    private final UserInputGetCollection userInputGetCollection = new UserInputGetCollection();

    // Database connection instance
    private final Connection connection = SQLiteConnection.getInstance().getConnection();

    // Scheduled executor service for periodic tasks
    private final ScheduledExecutorService executorServiceSystemConfig = Executors.newScheduledThreadPool(1);

    // DAO for system configuration
    private final SystemConfigDAOImpl systemConfigDAO = new SystemConfigDAOImpl();

    // Controllers for different system functionalities
    private final ConfigureSystemParametersController configureSystemParametersController = new ConfigureSystemParametersController(); // Configure System Parameters

    private final VendorManagementController vendorManagementController = new VendorManagementController(); // Manage Vendors

    private final TicketManagementController ticketManagementController = new TicketManagementController(); // Manage Tickets

    private final SalesLogController salesLogController = new SalesLogController(); // Sales Log

    // Displays the main menu
    private int mainMenu() {
        System.out.println("""
        --------------------------------------------
        ---   Real-Time Event Ticketing System   ---
        --------------------------------------------
        ---       WELCOME TO QUICK Ticks         ---
        --------------------------------------------
        
            1. Configure System Parameters
            2. Vendors Management
            3. Ticket Management
            4. View Sales Log
            0. Exit
        
        --------------------------------------------
        """);
        return userInputGetCollection.getUserInputInt("Please select an option between number (0-4):> ");
    }
    // Main method to initialize and run the application
    public static void main(String[] args) {
        Main main = new Main();
        boolean exit;
        // Initialize database connection and check status
        try {
            boolean dbConnection = main.connection != null && !main.connection.isClosed();
            System.out.println(dbConnection ? "Database connected successfully." : "Failed to connect to Database");
            exit = dbConnection;
            main.systemConfigDAO.updateConfigValue("cli_status", 1); // Update CLI status in the system configuration

        } catch (SQLException e) {
            exit = false;
            System.out.println(e.getMessage());
        }
        // Schedule periodic updates for system configuration
        main.executorServiceSystemConfig.scheduleAtFixedRate(
                new SystemConfig(main), 0, 5, TimeUnit.SECONDS);

        // Main loop to handle user interaction
        while (exit) {
            System.out.println();
            switch (main.mainMenu()) {
                case -1:    // Skip invalid input
                    break;

                case 0: // Exit the system
                    exit = !main.userInputGetCollection.getUserInputString(
                            "If you want to exit? (y/n):> ")
                            .equalsIgnoreCase("y");
                    try {
                        if (!exit) {
                            // Update CLI status and stop ticket system
                            main.systemConfigDAO.updateConfigValue("cli_status", 0);
                            main.ticketManagementController.stopSystem();
                            // Add shutdown hook to gracefully terminate resources
                            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                                main.executorServiceSystemConfig.shutdown();
                                try {
                                    if (!main.executorServiceSystemConfig.awaitTermination(5, TimeUnit.SECONDS)) {
                                        main.executorServiceSystemConfig.shutdownNow();
                                    }

                                } catch (InterruptedException e) {
                                    main.executorServiceSystemConfig.shutdownNow();
                                }
                            }));
                            // Close the database connection and terminate the program
                            main.connection.close();
                            System.exit(0);
                        }

                    } catch (SQLException e) {
                        System.out.println("Database failed to connect.\n" + e.getMessage());
                    }
                    break;

                case 1:     // Configure System Parameters
                    main.configureSystemParametersController.configureSystemParameters();
                    break;

                case 2:     // Manage Vendors
                    main.vendorManagementController.vendorsManagement();
                    break;

                case 3:     // Manage Tickets
                    main.ticketManagementController.ticketManagement();
                    break;

                case 4:     // Sales Log
                    main.salesLogController.salesLog();
                    break;

                default:
                    System.out.println("Invalid input. Please enter number between (0-4)");
                    break;
            }
        }
    }

    public TicketManagementController getTicketManagementController() {
        return ticketManagementController;
    }

}
