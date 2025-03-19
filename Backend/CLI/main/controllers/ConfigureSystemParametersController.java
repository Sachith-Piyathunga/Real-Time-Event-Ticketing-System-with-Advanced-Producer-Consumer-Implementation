package main.controllers;

import main.dao.impl.SystemConfigDAOImpl;
import main.models.TicketPool;
import main.util.UserInputGetCollection;
import java.sql.SQLException;


public class ConfigureSystemParametersController
{

    // Utility class for capturing user input
    private final UserInputGetCollection uic = new UserInputGetCollection();

    // Data access object for system configuration
    private final static SystemConfigDAOImpl configDAO = new SystemConfigDAOImpl();

    // Displays the menu for configuring system parameters
    private int configureSystemParametersMenu() {
        System.out.println(""" 
        --------------------------------------------
        ---     Configure System Parameters      ---
        --------------------------------------------
        
              1. Show Status
              2. Set Total Tickets
              3. Set Ticket Release Rate
              4. Set Customer Retrieval Rate
              5. Set Max Ticket Capacity
              6. Back to Main Menu
         
        --------------------------------------------
        """);
        return uic.getUserInputInt("Please select an option between number (1-6):> ");
    }

    // Displays the current system configuration status
    private void showStatus() {
        try {
            System.out.println(
                    "\n---- Show Status ----\n" +
                            "Tickets Total: " +
                            configDAO.findConfigValue("total_tickets") + "\n" +
                            "Tickets Released (per seconds): " +
                            configDAO.findConfigValue("ticket_release_rate") + "\n" +
                            "Customer Retrieval Rate (per seconds): " +
                            configDAO.findConfigValue("customer_retrieval_rate") + "\n" +
                            "Max Ticket Capacity: " +
                            configDAO.findConfigValue("max_ticket_capacity") + "\n" +
                            "--------------------------------------------"
            );

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Allows the user to set the total number of tickets
    private void setTotalTickets() {
        try {
            System.out.println(
                    "\n---- Total Ticket Update ---- \n" +
                            "Tickets Total Right Now: " +
                            configDAO.findConfigValue("total_tickets")
            );
            int maxCapacity = configDAO.findConfigValue("max_ticket_capacity");
            int newTickets = uic.getUserInputInt("Set total tickets:> ");

            if (newTickets >= 0 && newTickets <= maxCapacity) {
                configDAO.updateConfigValue("total_tickets", newTickets);
                TicketPool.getInstance().reloadSetTotalTickets();
                System.out.println("Configuration saved successfully!");

            } else {
                System.out.println("Invalid configuration!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Update the ticket release rate
    private void setTicketReleaseRate() {
        try {
            System.out.println(
                    "\n----  Tickets Released Update ---- \n" +
                            "Tickets Released Right Now (Per seconds): " +
                            configDAO.findConfigValue("ticket_release_rate")
            );

            int newTickets = uic.getUserInputInt("Set tickets released (per seconds):> ");

            if (newTickets >= 0) {
                configDAO.updateConfigValue("ticket_release_rate", newTickets);
                new TicketManagementController().restartSystemForVendors();
                System.out.println("Configuration saved successfully!");

            } else {
                System.out.println("Invalid configuration!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Updates the customer retrieval rate
    private void setCustomerRetrievalRate() {
        try {
            System.out.println(
                    "\n----  Customer Retrieval Rate ---- \n" +
                            "Customer Retrieval Rate Right Now (Per seconds): " +
                            configDAO.findConfigValue("customer_retrieval_rate")
            );

            int newTickets = uic.getUserInputInt("Set customer retrieval rate (Per seconds):> ");

            if (newTickets >= 0) {
                configDAO.updateConfigValue("customer_retrieval_rate", newTickets);
                new TicketManagementController().restartSystemForCustomers();
                System.out.println("Configuration saved successfully!");

            } else {
                System.out.println("Invalid configuration!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Updates the maximum ticket capacity
    private void setMaxTicketCapacity() {
        try {
            System.out.println(
                    "\n----  Max Ticket Capacity ---- \n" +
                            "Max Ticket Capacity Right Now: " +
                            configDAO.findConfigValue("max_ticket_capacity")
            );

            int newTickets = uic.getUserInputInt("Set max ticket capacity:> ");

            if (newTickets >= 0) {
                configDAO.updateConfigValue("max_ticket_capacity", newTickets);
                TicketPool.getInstance().reloadSetMaxCapacity();
                System.out.println("Configuration saved successfully!");

            } else {
                System.out.println("Invalid configuration!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Main entry point for managing system parameters
    public void configureSystemParameters() {
        boolean exit = true;

        while (exit) {
            System.out.println();
            switch (configureSystemParametersMenu()) {
                case -1:    // Skip for invalid input
                    break;

                case 1:     // Show Status
                    showStatus();
                    break;

                case 2:     // Set Total Tickets
                    setTotalTickets();
                    break;

                case 3:     // Set Ticket Release Rate
                    setTicketReleaseRate();
                    break;

                case 4:     // Set Customer Retrieval Rate
                    setCustomerRetrievalRate();
                    break;

                case 5:     // Set Max Ticket Capacity
                    setMaxTicketCapacity();
                    break;

                case 6:     // Back to Main Menu
                    exit = false;
                    break;

                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }
}
