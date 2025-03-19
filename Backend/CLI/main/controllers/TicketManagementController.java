package main.controllers;

import main.dao.SystemConfigDAO;
import main.dao.impl.SystemConfigDAOImpl;
import main.dao.impl.VendorDAOImpl;
import main.models.Customer;
import main.models.TicketPool;
import main.models.Vendor;
import main.util.UserInputGetCollection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TicketManagementController
{

    // User input utility for fetching user inputs
    private final UserInputGetCollection uic = new UserInputGetCollection();

    // Data access object for system configuration
    private final SystemConfigDAO configDAO = new SystemConfigDAOImpl();

    // Singleton instance of TicketPool for ticket management
    private static final TicketPool ticketPool = TicketPool.getInstance();

    // ScheduledExecutorService for handling vendor-related tasks
    private final ScheduledExecutorService executorServiceVendor = Executors.newScheduledThreadPool(3);

    // ScheduledExecutorService for handling customer-related tasks
    private final ScheduledExecutorService executorServiceCustomer = Executors.newScheduledThreadPool(1);
    // Lists to manage VIP and regular customers
    private static final ArrayList<Customer> vipCustomers = new ArrayList<>();
    private static final ArrayList<Customer> customers = new ArrayList<>();

    // Constructor to initialize the system and start it if configured
    public TicketManagementController()
    {
        try {
            if (configDAO.findConfigValue("system_status") == 1)  // Check system configuration to determine if the system should start
            {
                startSystem();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Displays the system parameter configuration menu
    private int configureSystemParametersMenu()
    {
        System.out.println("""
        --------------------------------------------
        -----       Ticket Management          -----
        --------------------------------------------
        
                  1. Show Status
                  2. Start System
                  3. Stop System
                  4. Restart System
                  5. Back to Main Menu
                  
        --------------------------------------------
        """);
        return uic.getUserInputInt("Please select an option between number (1-5):> ");
    }

    // Manages the ticket system showing menu options
    public void ticketManagement()
    {
        boolean exit = true;

        while (exit)
        {
            System.out.println();
            switch (configureSystemParametersMenu())
            {
                case -1:    // For invalid input skip
                    break;

                case 1:     // Show system status
                    showStatus();
                    break;

                case 2:     // Start the ticket management system
                    setStartSystem();
                    break;

                case 3:     // Stop the ticket management system
                    setStopSystem();
                    break;

                case 4:     // Restart the ticket management system
                    setRestartSystem();
                    break;

                case 5:     // Exit to the main menu
                    exit = false;
                    break;

                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }

    // Starts the ticket management system
    public void startSystem() throws SQLException
    {
        startSystemForVendors();
        startSystemForCustomers();
    }

    // Starts vendor-related tasks
    public void startSystemForVendors() throws SQLException
    {
        for (Vendor v : new VendorDAOImpl().getAllVendors())
        {
            v.start(executorServiceVendor, ticketPool);
        }
    }

    // Starts customer-related tasks
    public void startSystemForCustomers() throws SQLException
    {
        // Schedule a new customer task at a fixed rate
        executorServiceCustomer.scheduleAtFixedRate(() ->
                {
            try {
                Customer customer1 = new Customer(ticketPool);
                Customer customer2 = new Customer(ticketPool);

                if (customer1.isVip()) {
                    vipCustomers.add(customer1);
                } else {
                    customers.add(customer1);
                }

                if (customer2.isVip()) {
                    vipCustomers.add(customer2);
                } else {
                    customers.add(customer2);
                }

                if (!vipCustomers.isEmpty()) {
                    new Thread(vipCustomers.getFirst()).start();
                    vipCustomers.removeFirst();

                } else {
                    new Thread(customers.getFirst()).start();
                    customers.removeFirst();
                }

            } catch (SQLException e) {
                System.err.println("Failed creating a new customer: " + e.getMessage());
            }
        }, 0,
                configDAO.findConfigValue("customer_retrieval_rate"),
                TimeUnit.SECONDS);

    }

    // Stops the ticket management system
    public void stopSystem()
    {
        try {
            stopSystemForVendors();
            stopSystemForCustomers();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Stops vendor-related processes
    public void stopSystemForVendors() throws SQLException
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorServiceVendor.shutdown();
            try {
                if (!executorServiceVendor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorServiceVendor.shutdownNow();
                }

            } catch (InterruptedException e) {
                executorServiceVendor.shutdownNow();
            }
        }));
    }

    // Stops customer-related processes
    public void stopSystemForCustomers() throws SQLException
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorServiceCustomer.shutdown();
            try {
                if (!executorServiceCustomer.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorServiceCustomer.shutdownNow();
                }

            } catch (InterruptedException e) {
                executorServiceCustomer.shutdownNow();
            }
        }));
    }

    // Restart system
    public void restartSystem() throws SQLException {
        restartSystemForVendors();
        restartSystemForCustomers();
    }

    // Restarts vendor-related processes
    public void restartSystemForVendors() throws SQLException {
        stopSystemForVendors();
        startSystemForVendors();
    }

    // Restarts customer-related processes
    public void restartSystemForCustomers() throws SQLException {
        stopSystemForCustomers();
        startSystemForCustomers();
    }

    // Displays the current status of the system
    private void showStatus() {
        try {
            System.out.println(
                "\n-----   Show Status   -----\n" +
                (configDAO.findConfigValue("system_status") == 1
                ? "System is running\n"
                : "System is stop\n") +
                "--------------------------------------------"
            );

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Starts the system by updating the configuration and initiating processes
    private void setStartSystem() {
        try {
            configDAO.updateConfigValue("system_status", 1);
            startSystem();
            System.out.println("System is running.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Stops the system by updating the configuration and terminating processes
    private void setStopSystem() {
        try {
            configDAO.updateConfigValue("system_status", 0);
            stopSystem();
            System.out.println("System is stop.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Restart System
    private void setRestartSystem() {
        try {
            configDAO.updateConfigValue("system_status", 1);
            restartSystem();
            System.out.println("System is restart.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
