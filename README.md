# Scheduling-System-Application
Overview

The C195 Project is a Java-based scheduling application that allows users to manage customer records and appointments efficiently. The application leverages JavaFX for the user interface and connects to a SQL database to retrieve and manage data.

Features

Customer Management:
Add new customer records.
Update existing customer records.
Delete customer records (if no associated appointments).
Appointment Management:
Add new appointments.
Update existing appointments.
Delete appointments.
View appointments by current week, current month, or all appointments.
Reports:
Generate various reports based on customer and appointment data.
Prerequisites

Java Development Kit (JDK) 8 or higher.
JavaFX SDK.
SQL database with the required schema and data.
File Structure

src/com/example/c195project/Controller/MainMenuController.java: Main controller for handling the main menu operations.
src/com/example/c195project/Model/DBAppointments.java: Database operations related to appointments.
src/com/example/c195project/Model/DBCustomers.java: Database operations related to customers.
src/com/example/c195project/Model/DBCountries.java: Database operations related to countries.
src/com/example/c195project/Model/DBDivisions.java: Database operations related to divisions.
src/com/example/c195project/Model/Appointments.java: Model class for appointments.
src/com/example/c195project/Model/Customers.java: Model class for customers.
src/com/example/c195project/Model/Countries.java: Model class for countries.
src/com/example/c195project/Model/Divisions.java: Model class for divisions.
src/com/example/c195project/view/: FXML files for the user interface.
Database Schema

The application connects to a SQL database with the following tables:

appointments: Stores appointment details.
customers: Stores customer details.
countries: Stores country details.
divisions: Stores division details.
Running the Application

Set Up Database:
Ensure your SQL database is set up with the required tables and data.
Update the database connection settings in the relevant model classes (DBAppointments.java, DBCustomers.java, etc.).
Compile and Run:
Use an IDE like IntelliJ IDEA or Eclipse to import the project.
Ensure the JavaFX SDK is properly configured in your IDE.
Compile and run the Main class to start the application.
Main Menu Operations

Initialize Method
The initialize method in MainMenuController is responsible for setting up the initial state of the application, including populating the customer and appointment tables with data from the database.

Add, Update, Delete Operations
The application provides methods to add, update, and delete customer records and appointments:

onClickAddAppointment(ActionEvent actionEvent): Opens the add appointment menu.
onClickUpdateAppointment(ActionEvent actionEvent): Opens the update appointment menu.
onClickDeleteAppointment(ActionEvent actionEvent): Deletes the selected appointment.
onClickAddCustomerRecord(ActionEvent actionEvent): Opens the add customer records menu.
onClickUpdateCustomerRecord(ActionEvent actionEvent): Opens the update customer records menu.
onClickDeleteCustomerRecord(ActionEvent actionEvent): Deletes the selected customer record if no associated appointments.
View Filters
The application allows users to filter appointments by current week, current month, or view all appointments:

onSelectCurrentWeekAppointments(ActionEvent actionEvent): Filters appointments for the current week.
onSelectCurrentMonthAppointments(ActionEvent actionEvent): Filters appointments for the current month.
onSelectAllAppointments(ActionEvent actionEvent): Displays all appointments.
Logout

The onClickLogout(ActionEvent actionEvent) method logs the user out of the system and redirects to the login menu.

Conclusion

This scheduling application provides a robust solution for managing customer records and appointments, complete with filtering options and detailed CRUD operations. The use of JavaFX for the user interface and SQL for the backend ensures a seamless and efficient user experience.
