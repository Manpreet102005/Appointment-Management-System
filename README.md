[README.md](https://github.com/user-attachments/files/27696271/README.md)
# Appointment Management System

A command-line based Appointment Management System written in Java. This application allows users to manage doctors, patients, and their appointments. It offers the flexibility to use either an in-memory data store for quick testing or a persistent MySQL database for durable storage.

## Features

- **Dual Storage Modes**: Choose between transient in-memory storage or persistent MySQL database storage at runtime.
- **Doctor Management**: Add new doctors with their specializations and view a list of all doctors.
- **Patient Management**: Register new patients with their details (name, phone, gender, blood group) and view a list of all patients.
- **Appointment Scheduling**:
    - Book new appointments for a specific doctor and patient at a given time.
    - Cancel existing appointments.
    - Reschedule appointments to a new date and time.
- **Data Validation**: Includes input validation for names, phone numbers, appointment times, and more to ensure data integrity.
- **View Records**: Display lists of all doctors, patients, and currently booked appointments.

## Project Structure

The project is organized into several packages to separate concerns:

-   `src/`
    -   `Main.java`: The entry point of the application, handling the command-line interface and user interaction.
    -   `entities/`: Contains the model classes (`Appointment`, `Doctor`, `Patient`).
    -   `repositories/`: Defines interfaces for data access operations and includes two sets of implementations:
        -   `impl/DB*`: Implementations for MySQL database interaction using JDBC.
        -   `impl/InMemory*`: Implementations using in-memory data structures (`ConcurrentHashMap`, `TreeMap`).
    -   `service/`: Contains the core business logic, orchestrating calls between the UI and data repositories.
    -   `util/`: Utility classes, including `DatabaseConnection` for managing JDBC connections.
    -   `validations/`: Classes responsible for validating user input and business rules.
-   `database/`
    -   `schema.sql`: The SQL script to create the necessary database and tables for MySQL storage mode.

## Getting Started

### Prerequisites

-   Java Development Kit (JDK) 11 or higher.
-   MySQL Server (if you intend to use the database storage mode).
-   An IDE like IntelliJ IDEA or Eclipse.

### Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/manpreet102005/appointment-management-system.git
    cd appointment-management-system
    ```

2.  **Database Setup (for Database Mode):**
    If you wish to use persistent storage, you need to set up a MySQL database.

    a. **Execute the SQL Schema:**
    Connect to your MySQL server and run the script located at `database/schema.sql` to create the `appointment_db` database and the necessary tables (`patients`, `doctors`, `appointments`).

    b. **Configure Database Connection:**
    Create a file named `env.properties` in the root directory of the project and add your database connection details:
    ```properties
    url=jdbc:mysql://localhost:3306/appointment_db
    user=your_mysql_username
    password=your_mysql_password
    ```

### Running the Application

1.  Compile and run the `Main.java` file from your IDE or the command line.
2.  On startup, you will be prompted to select a storage mode:

    ```
    Select Storage Mode:
    1. Database
    2. In-Memory
    Enter choice:
    ```
    -   Enter `1` to use the MySQL database.
    -   Enter `2` to use the in-memory store (data will be lost when the application stops).

## Usage

Once the application is running, you will be presented with a menu of options. Enter the number corresponding to the action you want to perform.

```
===== HOSPITAL MANAGEMENT MENU =====
1. Add Doctor
2. Add Patient
3. Book Appointment
4. Cancel Appointment
5. Reschedule Appointment
6. View All Appointments
7. View All Patients
8. View All Doctors
9. Remove Doctor
0. Exit
Enter choice:
```

Follow the on-screen prompts to enter details for each operation. The system provides feedback for successful operations and clear error messages for invalid input or failed operations.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
