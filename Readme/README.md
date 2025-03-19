# Real-Time Event Ticketing System

## Project Overview
The Real-Time Event Ticketing System is a multi-component application designed to efficiently manage tickets through a command-line interface (CLI), a backend API implemented in Spring Boot, and a front-end application developed in Angular. This system facilitates real-time interactions between vendors and customers, leveraging multi-threading and concurrency for optimal performance.

Key features include:
* Simulates multi-threaded vendor and customer operations.
* Prioritizes VIP customers for higher access privileges.
* Integrates SQLite database for persistent storage.
* Enables dynamic configuration of system parameters via the CLI.
* Provides an API backend for real-time data management.
* Includes an Angular front-end for visualization and user management.

<hr>

## System Components
### 1. Command-Line Interface (CLI)
The CLI serves as a direct interaction point for system management.

Main Menu
```java
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
```

Submenus
1. Configure System Parameters
   *This submenu allows dynamic monitoring and configuration of parameters such as total tickets, ticket release rate, and maximum ticket capacity.
```java
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
```
2. Vendors Management
   *Manage vendors dynamically with options to view, add, update, or remove vendors.
```java
--------------------------------------------
-----         Manage Vendors           -----
--------------------------------------------
        1. Show All Vendors
        2. Add Vendor
        3. Update Vendor
        4. Remove Vendor
        5. Back to Main Menu
        
--------------------------------------------
```
3. Ticket Management
   *Monitor and control the ticketing system operations, including starting, stopping, and restarting the system.
```java
--------------------------------------------
-----       Ticket Management          -----
--------------------------------------------
        
        1. Show Status
        2. Start System
        3. Stop System
        4. Restart System
        5. Back to Main Menu
                  
--------------------------------------------
```
4. Sales Log
   *Displays real-time logs of ticket sales.

### 2. API (Spring Boot)
The Spring Boot API acts as the backend layer, offering:
* Integration with SQLite for storing vendors, customers, and tickets.
* RESTful endpoints for system management and real-time data retrieval.
* Synchronization between CLI and front-end interactions.

### 3. Front-End (Angular)
The Angular-based front-end provides:
* A user-friendly interface to visualize ticket availability, sales logs, and system statistics.
* Real-time updates from the backend.
* Tools for vendor and customer management.

<hr>

## Key Features
#### Multi-threading and Concurrency
- Operates vendors and customers in separate threads.
- Implements VIP customer prioritization through a custom queuing mechanism.
- Ensures thread-safe operations using synchronized methods and thread-safe collections.

#### SQLite Database Integration
- Stores vendor, customer, and ticket data persistently.
- Reflects runtime updates dynamically, such as adding or removing vendors.

#### Dynamic Configuration
- Updates parameters like ticket release rate and maximum capacity during runtime via the CLI.

<hr>

## Installation and Setup
<b>>Prerequisites<b>
* Java 21+
* Node.js 18+
* Angular CLI
* SQLite

<hr>

## Usage
### CLI
* Access the main menu to configure settings, manage vendors, control ticketing operations, and view logs.
* Start or stop the system and monitor real-time updates.

### API
* Utilize RESTful endpoints for integration or management tasks.
* Refer to the API Documentation for detailed endpoint descriptions.

### Front-End
- Access the Angular application at `http://localhost:4200`.
- Manage vendors, visualize ticket availability, and view logs.

<hr>

## Technologies Used
* Java: Core application logic and CLI.
* Spring Boot: Backend API development.
* Angular: Front-end user interface.
* SQLite: Persistent database storage.
* Multi-threading: Simulation of vendors and customers.
