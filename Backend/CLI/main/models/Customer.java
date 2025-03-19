package main.models;

import main.dao.impl.SalesLogDAOImpl;
import main.dao.impl.SystemConfigDAOImpl;
import java.sql.SQLException;
import java.util.Random;

/**
 * Represents a customer who interacts with the ticket pool to purchase tickets.
 * Implements the Runnable interface to support concurrent operations.
 */
public class Customer implements Runnable {
    private final int id; // Unique identifier for the customer
    private final boolean vip; // Indicates if the customer is a VIP
    private final TicketPool ticketPool; // Reference to the shared ticket pool

    // Constructs a Customer object with a randomly generated ID and VIP status
    public Customer(TicketPool ticketPool) throws SQLException {
        this.ticketPool = ticketPool;
        Random random = new Random(); // Generate a random ID for the customer
        this.id = random.nextInt(100) + 1;
        this.vip = random.nextInt(2) + 1 == 1; // Randomly assign VIP status (50% chance)
    }

    // The run method executed by threads. Simulates the customer attempting to purchase a ticket.
    @Override
    public void run() {
        try {
            // Check if the system is active
            if (new SystemConfigDAOImpl().findConfigValue("system_status") == 1 ) {
                ticketPool.reloadSetMaxCapacity();
                ticketPool.reloadSetTotalTickets();
                // Attempt to remove a ticket from the pool
                if (TicketPool.getInstance().removeTicket()) {
                    // Log the ticket purchase event
                    new SalesLogDAOImpl().addLog(vip
                            ? "Buy 1 ticket from ticket pool [ID - " + id + "] VIP Customer"
                            : "Buy 1 ticket from ticket pool [ID - " + id + "] Customer");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage()); // Handle database errors
        }
    }
    // Retrieves the VIP status of the customer
    public boolean isVip() {
        return vip;
    }
}
