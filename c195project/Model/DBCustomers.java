package com.example.c195project.Model;

import com.example.c195project.Controller.ReportsMenuController;
import com.example.c195project.Helper.DBConnection;
import com.example.c195project.Model.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * DAO class created for handling database operations related to customer.
 *
 * @author Jamal Creary
 */
public abstract class DBCustomers {
    private static ReportsMenuController reportsMenuController;

    /**
     * Retrieves all customer records from the database.
     *
     * @return an array list of Customers containing all customers in the database
     * @throws SQLException if an SQL exception occurs
     */
    public static ObservableList<Customers> getAllCustomers() throws SQLException {
        //Initialize an empty ObservableList to store the retrieved customers
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        //SQL query to select all appointments from the database
        String sql = "SELECT * FROM CLIENT_SCHEDULE.CUSTOMERS";
        //Create a PreparedStatement to execute the SQL query
        PreparedStatement ps2 = DBConnection.connection.prepareStatement(sql);
        //Execute the query and retrieve the result set
        ResultSet rs2 = ps2.executeQuery();
        try {
            //Loops through the result set using the rs next method
            while (rs2.next()) {
                int customerID = rs2.getInt("Customer_ID");
                String customerName = rs2.getString("Customer_Name");
                String address = rs2.getString("Address");
                String postalCode = rs2.getString("Postal_Code");
                String phone = rs2.getString("Phone");
                int divisionID = rs2.getInt("Division_ID");
                String division = getDivisionName(divisionID);
                String country = getCountryName(divisionID);
                // Creates a customer object
                Customers customer = new Customers(customerID, customerName, address, postalCode, phone, divisionID, country, division);
                // Adds the customer to the customers list created above
                customers.add(customer);
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        //Returns a list of customers
        return customers;
    }
    /**
     * Retrieves country name from the database using the division ID.
     *
     * @param divisionID
     * @return the country name
     * @throws SQLException if an SQL exception occurs
     */
    private static String getCountryName(int divisionID) throws SQLException {
        //Empty country string
        String country = "";
        //SQL query to select a country from the database using the division ID and Division table
        String sql = "SELECT Countries.Country FROM CLIENT_SCHEDULE.FIRST_LEVEL_DIVISIONS Divisions " +
                "JOIN CLIENT_SCHEDULE.COUNTRIES Countries ON Divisions.Country_ID = Countries.Country_ID " +
                "WHERE Divisions.Division_ID = ?";
        //PreparedStatement to execute the SQL query
        PreparedStatement ps2 = DBConnection.connection.prepareStatement(sql);
        try {
            //Sets parameters
            ps2.setInt(1, divisionID);
            //Execute the query and retrieve the result set
            try (ResultSet rs = ps2.executeQuery()) {
                if (rs.next()) {
                    country = rs.getString("Country");
                }
            }
        } catch (Exception eee) {
            eee.printStackTrace();
        }
        //Returns country name
        return country;
    }

    // Method to retrieve division name based on division ID
    /**
     * Retrieves division name from the database using the division ID.
     *
     * @param divisionID
     * @return the division name
     * @throws SQLException if an SQL exception occurs
     */
    private static String getDivisionName(int divisionID) throws SQLException {
        //Empty country string
        String divisions = "";
        //SQL query to select a division from the database using the division ID and Division table
        String sql = "SELECT Division FROM CLIENT_SCHEDULE.FIRST_LEVEL_DIVISIONS WHERE Division_ID = ?";
        //PreparedStatement to execute the SQL query
        PreparedStatement ps2 = DBConnection.connection.prepareStatement(sql);
        try {
            //Sets parameters
            ps2.setInt(1, divisionID);
            //Execute the query and retrieve the result set
            try (ResultSet rs = ps2.executeQuery()) {
                if (rs.next()) {
                    divisions = rs.getString("Division");
                }
            }
        } catch (Exception eee) {
            eee.printStackTrace();
        }
        //Returns division name
        return divisions;
    }

    /**
     * Inserts customer record into customer database.
     *
     * @param customer
     * @throws SQLException if an SQL exception occurs
     */
    public static void insertCustomer(Customers customer) throws SQLException {
        //Create connection to database
        Connection connection = DBConnection.getConnection();
        try {
            //SQL query to insert customer into database
            String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                    "VALUES (?, ?, ?, ?, ?)";
            //PreparedStatement to execute the SQL query
            PreparedStatement ps = connection.prepareStatement(sql);
            try {
                // Set parameters for the PreparedStatement
                ps.setString(1, customer.getCustomerName());
                ps.setString(2, customer.getAddress());
                ps.setString(3, customer.getPostalCode());
                ps.setString(4, customer.getPhone());
                ps.setInt(5, customer.getDivisionID());
                // Execute the insert operation
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    /**
     * Updates customer record into customer database.
     *
     * @param customer
     * @throws SQLException if an SQL exception occurs
     */
    public static void updateCustomer(Customers customer) throws SQLException {
        //Create connection to database
        Connection connection = DBConnection.getConnection();
        try {
            //SQL query to update customer in database
            String sql = "UPDATE client_schedule.customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
            //PreparedStatement to execute the SQL query
            PreparedStatement ps = connection.prepareStatement(sql);
            try {
                // Set parameters for the PreparedStatement
                ps.setString(1, customer.getCustomerName());
                ps.setString(2, customer.getAddress());
                ps.setString(3, customer.getPostalCode());
                ps.setString(4, customer.getPhone());
                ps.setInt(5, customer.getDivisionID());
                ps.setInt(6, customer.getCustomerID());
                // Execute the update operation
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    /**
     * Deletes the selected Customer Records.
     *
     * @param selectedCustomer
     * @return true if the customer is successfully deleted
     */
    public static boolean deleteCustomer(Customers selectedCustomer) {
        //Create connection to database
        Connection connection = DBConnection.getConnection();
        try {
            //if connection is closed to database, open connection before execute code
            if (connection.isClosed()) {
                DBConnection.openConnection();
            }
            // Checks to see if there is any associated appointments
            if (selectedCustomer.getAllAssociatedAppointments().size() > 0) {
                //If appointment exist, alert message is displayed and no action is taken
                Alert associatedAppointment = new Alert(Alert.AlertType.ERROR);
                associatedAppointment.setTitle("Error Message");
                associatedAppointment.setContentText("There are associated appointments attached to the selected customer record. Please remove the associated appointments before deleting the customer.");
                associatedAppointment.show();
                return false;
            } else {
                // Prepares the SQL statement for deleting the customer
                String sql = "DELETE FROM customers WHERE Customer_ID = ?";
                //PreparedStatement to execute the SQL query
                try (PreparedStatement ps4 = connection.prepareStatement(sql)) {
                    // Set the customer ID as the parameter for the PreparedStatement
                    ps4.setInt(1, selectedCustomer.getCustomerID());
                    // Execute the SQL statement to delete the customer
                    int rowsAffected = ps4.executeUpdate();
                    if (rowsAffected > 0) {
                        // Add the deleted customer to the list of deleted customers
                        addDeletedCustomer(selectedCustomer);

                        // Call the addDeletedCustomer method in ReportsMenuController
                        if (reportsMenuController != null) {
                            reportsMenuController.addDeletedCustomer(selectedCustomer);
                        }
                        return true;
                    } else {
                        //Alert if failed to delete customer
                        Alert associatedAppointment = new Alert(Alert.AlertType.ERROR);
                        associatedAppointment.setTitle("Error Message");
                        associatedAppointment.setContentText("Failed to delete customer record.");
                        associatedAppointment.show();
                    }
                }
                // Removes the customer from the list of all customers
                return Customers.getAllCustomers().remove(selectedCustomer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // List to store deleted customers
    private static ObservableList<Customers> deletedCustomers = FXCollections.observableArrayList();


    /**
     * Retrieves the list of deleted customers.
     *
     * @return The list of deleted customers.
     */
    public static ObservableList<Customers> getDeletedCustomers() {
        return deletedCustomers;
    }

    /**
     * Adds a deleted customer to the list of deleted customers.
     *
     * @param customer The deleted customer to be added.
     */
    public static void addDeletedCustomer(Customers customer) {
        deletedCustomers.add(customer);
    }
}
