package main.models;

import main.dao.impl.SalesLogDAOImpl;
import main.dao.impl.SystemConfigDAOImpl;
import java.sql.SQLException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Represents a Vendor responsible for periodically adding tickets to the ticket pool.
 * Implements Runnable to facilitate scheduling periodic tasks.
 */
public class Vendor implements Runnable {
    private int id; // Unique ID of the vendor
    private String vendorName; // Name of the vendor
    private int ticketsPerRelease; // Number of tickets released per cycle
    private int releaseRateSec; // Time interval (in seconds) between ticket releases
    private TicketPool ticketPool; // Reference to the shared ticket pool
    private final SystemConfigDAOImpl configDAO = new SystemConfigDAOImpl();  // DAO for fetching system configurations

    /**
     * The task executed periodically by the ScheduledExecutorService.
     * Adds tickets to the ticket pool if the system is active.
     */
    @Override
    public void run() {
        try {
            if (configDAO.findConfigValue("system_status") == 1) {
                checkVendorDetails(); // Check vendor details and update settings

                ticketPool.reloadSetMaxCapacity();
                ticketPool.reloadSetTotalTickets();
                // Add tickets to the pool and log the action
                if (TicketPool.getInstance().addTickets(ticketsPerRelease)) {
                    new SalesLogDAOImpl().addLog("Add " + ticketsPerRelease +
                            " tickets into ticket pool [ID - " + id + "] Vendor " + vendorName);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Starts the vendor's ticket release process at fixed intervals
    public void start(ScheduledExecutorService executorService, TicketPool ticketPool) throws SQLException {
        checkVendorDetails(); // Ensure vendor details are properly set
        this.ticketPool = ticketPool;
        executorService.scheduleAtFixedRate(this, 0, releaseRateSec, TimeUnit.SECONDS);
    }

    /**
    * Validates and updates vendor details.
    * Ensures all vendor attributes are properly set and updates release rate if needed.
    */
    private void checkVendorDetails() throws SQLException {
        if (id == 0 || vendorName == null || ticketsPerRelease == 0 || releaseRateSec == 0) {
            throw new IllegalStateException("Before start or run vendor, setup vendor details");
        }
        setReleaseRateSec(Math.max(configDAO.findConfigValue("ticket_release_rate"), releaseRateSec));
    }

    // Nun argument constructor
    public Vendor() {
    }

    // Constructor with all fields
    public Vendor(int id, String vendorName, int ticketsPerRelease, int releaseRateSec) {
        this.id = id;
        this.vendorName = vendorName;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseRateSec = releaseRateSec;
    }

    // Constructor without ID (for new vendors)
    public Vendor(String vendorName, int ticketsPerRelease, int releaseRateSec) {
        this.vendorName = vendorName;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseRateSec = releaseRateSec;
    }

    // Getter and setter methods for vendor attribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public int getTicketsPerRelease() {
        return ticketsPerRelease;
    }

    public void setTicketsPerRelease(int ticketsPerRelease) {
        this.ticketsPerRelease = ticketsPerRelease;
    }

    public int getReleaseRateSec() {
        return releaseRateSec;
    }

    public void setReleaseRateSec(int releaseRateSec) {
        this.releaseRateSec = releaseRateSec;
    }
}
