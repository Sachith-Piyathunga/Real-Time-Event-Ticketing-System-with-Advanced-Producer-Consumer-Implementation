package main.models;

import main.dao.impl.SystemConfigDAOImpl;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A Singleton class that represents the shared pool of tickets.
 * Ensures thread-safe operations for adding and removing tickets.
 */
public class TicketPool {
    private static int totalTickets; // Current number of tickets in the pool
    private static int maxCapacity; // Maximum ticket capacity for the pool
    private final ReentrantLock lock = new ReentrantLock(); // Lock for thread-safe operations
    private static volatile TicketPool ticketPool; // Singleton instance

    // Data Access Object for interacting with system configurations
    private static final SystemConfigDAOImpl configDAO = new SystemConfigDAOImpl();

    // Private constructor to enforce Singleton pattern
    private TicketPool() {}

    /**
    * Provides the Singleton instance of the TicketPool.
    * Uses double-checked locking for thread-safe and efficient initialization.
    */
    public static TicketPool getInstance() {
        if (ticketPool == null) {
            synchronized (TicketPool.class) {
                if (ticketPool == null) {
                    ticketPool = new TicketPool();
                    try {
                        // Initialize total tickets and maximum capacity from the database
                        ticketPool.setMaxCapacity(configDAO.findConfigValue("max_ticket_capacity"));
                        ticketPool.setTotalTickets(configDAO.findConfigValue("total_tickets"));

                    } catch (SQLException e) {
                        throw new RuntimeException("Failed to initialize ticket pool from database.", e);
                    }
                }
            }
        }
        return ticketPool;
    }

    // Adds tickets to the pool if the total count does not exceed the maximum capacity
    public boolean addTickets(int count) {
        lock.lock(); // Acquire lock for thread safety
        try {
            if (totalTickets + count <= maxCapacity) {
                configDAO.updateConfigValue("total_tickets", totalTickets + count); // Update database
                totalTickets += count;
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating total tickets in database.", e);

        } finally {
            lock.unlock(); // Release lock
        }
        return false;
    }

    // Removes ticket from the pool if there are tickets available
    public boolean removeTicket() {
        lock.lock();
        try {
            if (totalTickets > 0) {
                configDAO.updateConfigValue("total_tickets", totalTickets -= 1);
                totalTickets -= 1;
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating ticket count in database.", e);

        } finally {
            lock.unlock();
        }
        return false;
    }
    // Reloads and updates the maximum capacity from the database
    public void reloadSetMaxCapacity() {
        try {
            ticketPool.setMaxCapacity(configDAO.findConfigValue("max_ticket_capacity"));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to reload max capacity in ticket pool.", e);
        }
    }
    // Reloads and updates the total tickets count from the database
    public void reloadSetTotalTickets() {
        try {
            ticketPool.setTotalTickets(configDAO.findConfigValue("total_tickets"));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to reload total ticket in ticket pool.", e);
        }
    }
    // Sets the maximum capacity of the ticket pool
    private void setMaxCapacity(int maxCapacity) {
        lock.lock();
        try {
            TicketPool.maxCapacity = maxCapacity;
        } finally {
            lock.unlock();
        }
    }
    // Sets the total tickets in the pool
    private void setTotalTickets(int totalTickets) {
        lock.lock();
        try {
            TicketPool.totalTickets = totalTickets;
        } finally {
            lock.unlock();
        }
    }
}
