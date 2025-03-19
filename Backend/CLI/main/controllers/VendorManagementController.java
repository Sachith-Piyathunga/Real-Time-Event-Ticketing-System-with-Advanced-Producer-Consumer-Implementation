package main.controllers;

import main.dao.VendorDAO;
import main.dao.impl.VendorDAOImpl;
import main.models.Vendor;
import main.util.UserInputGetCollection;
import main.util.validation.VendorValidation;

import java.util.List;

// Controller class for managing vendors
public class VendorManagementController {
    // Utility for user input collection
    private final UserInputGetCollection uic = new UserInputGetCollection();

    // Data Access Object for vendor operations
    private final static VendorDAO configDAO = new VendorDAOImpl();

    // Validation utility for vendor data
    private final static VendorValidation validation = new VendorValidation();

    // Displays the vendor management menu
    private int manageVendorsMenu()
    {
        System.out.println("""
        --------------------------------------------
        -----         Manage Vendors           -----
        --------------------------------------------
             1. Show All Vendors
             2. Add Vendor
             3. Update Vendor
             4. Remove Vendor
             5. Back to Main Menu
        
        --------------------------------------------
        """);
        return uic.getUserInputInt("Please select an option between number (1-5):> ");
    }

    // Main method for vendor management
    public void vendorsManagement() {
        boolean exit = true;

        while (exit) {
            System.out.println();
            switch (manageVendorsMenu()) {
                case -1:    // For invalid input, skip the iteration
                    break;

                case 1:     // Show all vendors
                    showAllVendors();
                    break;

                case 2:     // Add a new vendor
                    addVendor();
                    break;

                case 3:     // Update an existing vendor
                    updateVendor();
                    break;

                case 4:     // Remove an existing vendor
                    removeVendor();
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

    // Adds a new vendor to the system
    private void addVendor() {
        System.out.println("\n-----     Add Vendor    -----");
        System.out.println(configDAO.addVendor(new Vendor(getVendorName(),
                getTicketsPerRelease(),
                getReleaseRateSec())) ? "Successfully added the vendor.\n" : "Failed to added the vendor.");
    }

    // Removes a vendor from the system
    private void removeVendor() {
        System.out.println("\n-----     Remove Vendor     -----");
        System.out.println(configDAO.deleteVendor(getVendorId())
                ? "Successfully deleted the vendor.\n"
                : "Failed to delete the vendor.");
    }

    // Displays all vendors in a formatted table
    private void showAllVendors() {
        System.out.println("\n-----     Show All Vendors     -----");
        System.out.println("+-----+-------------------------+---------------------+--------------------+");
        System.out.println("| ID  | Vendor Name             | Tickets Per Release | Release Rate (sec) |");
        System.out.println("+-----+-------------------------+---------------------+--------------------+");
        // Iterate through the list of vendors and print their details
        for (Vendor vendor : configDAO.getAllVendors()) {
            System.out.printf(
                    "| %-3d | %-23s | %-19d | %-18d |%n",
                    vendor.getId(),
                    vendor.getVendorName(),
                    vendor.getTicketsPerRelease(),
                    vendor.getReleaseRateSec()
            );
        }
        System.out.println("+-----+-------------------------+---------------------+--------------------+");
    }

    // Updates an existing vendor
    private void updateVendor() {
        System.out.println("\n-----     Update Vendor     -----");
        System.out.println(configDAO.updateVendor(new Vendor(
                getVendorId(),
                getVendorName(),
                getTicketsPerRelease(),
                getReleaseRateSec())) ? "Successfully update vendor.\n": "Failed to update vendor.\n");
    }

    private String getVendorName() {
        while (true) {
            String vendorName = uic.getUserInputString("Please enter a vendor name:> ");
            if (validation.validateVendorName(vendorName)) {
                return vendorName;
            }
        }
    }

    private int getTicketsPerRelease() {
        while (true) {
            int ticketsPerRelease = uic.getUserInputInt("Please enter a tickets per release:> ");
            if (validation.validateVendorTicketsPerRelease(ticketsPerRelease)) {
                return ticketsPerRelease;
            }
        }
    }

    private int getReleaseRateSec() {
        while (true) {
            int releaseRateSec = uic.getUserInputInt("Please enter a release rate (sec) :> ");
            if (validation.validateVendorReleaseRateSec(releaseRateSec)) {
                return releaseRateSec;
            }
        }
    }

    private int getVendorId() {
        while (true) {
            int vendorId = uic.getUserInputInt("Please enter a vendor id:> ");
            if (validation.existsVendor(vendorId)) {
                return vendorId;
            }
        }
    }

}
