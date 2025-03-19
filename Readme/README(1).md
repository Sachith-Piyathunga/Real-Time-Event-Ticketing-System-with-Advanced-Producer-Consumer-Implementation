# Real-Time Ticketing System - (QUICK Tiks)

## 1. Overview of the System
The Real-Time Ticketing System is designed to facilitate the management of ticket sales in real-time, replicating the process of tickets being released by vendors (producers) and purchased by customers (consumers). The system features capabilities to handle ticket availability, provide real-time updates, and organize data efficiently to accommodate multiple vendors and diverse ticket types.  

## 2. Component Roles: What Each Part Does

### Command-Line Interface (CLI)
**Purpose**: The Command-Line Interface (CLI) is primarily utilized by system administrators to configure and manage the system. It supports parameter configuration, vendor management, and the monitoring of ticket status in real-time. 

**CLI Functions**:
- **System Configuration**: Allows administrators to set up initial parameters, such as the total ticket count and the ticket release rate.
- **Manage Vendors**: Enables the addition or removal of vendors responsible for ticket sales. 
- **Manage Ticket Types**: Facilitates the setup of ticket types, such as VIP or General Admission, each with unique pricing.
- **Real-Time Status Monitoring**: Provides real-time updates on ticket inventory by vendor and type. 
- **Sales Log Retrieval**: Displays historical sales data, including details of ticket purchases.

### Frontend (User Interface)
**Purpose**: The frontend serves as a graphical interface for both customers and administrators. Customers use it to purchase tickets, while administrators monitor ticket sales and system status.

**Frontend Functions**:
- **Ticket Availability Display**: Displays real-time information about ticket availability to customers. 
- **Administrative Control Panel**: Offers tools for starting or stopping ticket release operations and adjusting system settings.
- **Real-Time Notifications**: Alerts users to changes in ticket status, sales updates, or system errors (e.g., tickets unavailable). 
- **Ticket Purchasing Interface**: Allows customers to purchase tickets through a user-friendly interface.  

### API (Backend Logic and Database Interaction)
**Purpose**: The API, developed using Spring Boot, serves as the central controller of the system. It manages all operations related to ticket handling, acts as the intermediary between the front end/CLI and the database, and ensures real-time data consistency.  

**API Functions**:
- **Producer-Consumer Pattern Implementation**: Manages ticket addition by producers and purchase by consumers, ensuring real-time updates. 
- **Thread Safety**: Handles operations involving multiple vendors and customers simultaneously, ensuring data integrity through synchronization. 
- **Database Operations**: Facilitates adding, updating, and retrieving data related to tickets, vendors, sales, and customers.

### Database
**Purpose**: The database is responsible for storing essential information about vendors, tickets, and sales. It provides a foundation for both real-time operations and historical data analysis. 

**Database Tables**:
- **Vendors Table**: Stores vendor details, including names and ticket release rates. 
- **Tickets Table**: Tracks information about each ticket, such as type, availability, and vendor association.
- **Sales Log Table**: Records details of every ticket sale, including ticket ID, customer information, and timestamps. 

## 3. How Components Work Together
Here’s how each part interacts in a typical workflow:

### System Setup (CLI and API)
- Administrators use the CLI to configure vendors, ticket types, and general system parameters. The API processes these configurations and stores them in the database. For instance, administrators can add vendors, assign specific ticket types to each vendor, and define release rates.  

### Ticket Handling (API and Database)
- After setup, the API handles ticket operations in real-time:
  - Vendors (producers) periodically release tickets into the pool.
  - Customers (consumers) remove tickets from the pool when they make purchases.
  - Customers (consumers) purchase tickets, removing them from the pool.  errors (using synchronization).
  - The API employs multi-threading to simulate these simultaneous actions while maintaining synchronization to prevent errors.  

### Real-Time Updates (Frontend, API, and Database)
 -The front end retrieves real-time ticket availability data from the API, which continuously updates ticket statuses in the database. When customers attempt to purchase tickets, the front end sends a request to the API, which adjusts the ticket count in the database and reflects the changes in real-time.  

### Sales Logging (API and Database)
 -Each ticket purchase is recorded by the API in the Sales Log Table. Both the CLI and front end can access this log to display sales history and verify ticket transaction details. d.

## 4. Example Workflow

### Admin Setup (CLI)
-An administrator accesses the CLI to configure vendors and ticket types. Two vendors, for example, might be added: Vendor A selling VIP tickets and Vendor B selling General Admission tickets. This configuration is saved in the database through the API.

### Customer Purchase (Frontend)
-A customer navigates the frontend to view available tickets. After selecting a VIP ticket, the frontend sends a purchase request to the API, which processes the transaction, updates the Tickets Table, and logs the sale in the Sales Log Table.

### Real-Time Status Check (CLI/Frontend)
-Administrators can monitor ticket availability in real time through the CLI, while customers observe updated availability through the frontend immediately after purchases.

## 5. Key Relationships
- **Vendors and Tickets**:  vendor is associated with specific ticket types, linked through the vendor ID in the Tickets Table.
- **Tickets and Sales Log**: Ticket sales are tracked in the Sales Log, with each log entry referencing the sold ticket's ID.
- **API and Database**: The API bridges the gap between the frontend/CLI and the database, managing ticket availability, sales, and user interactions by reading from and writing to the database.

This system is structured to ensure seamless synchronization across all components, facilitating efficient ticket management, real-time updates, and robust logging.
