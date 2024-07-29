package com.example.c195project.Model;

import com.example.c195project.Helper.DBConnection;
import com.example.c195project.Model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.*;

/**
 * DAO class created for handling database operations related to appointments.
 *
 * @author Jamal Creary
 */
public class DBAppointments {

    /**
     * Retrieves all appointments from the database.
     *
     * @return an array list of Appointments containing all appointments in the database
     * @throws SQLException if SQL exception occurs
     */
    public static ObservableList<Appointments> getAllAppointments() throws SQLException {
        //Initialize an empty ObservableList to store the retrieved appointments
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        //SQL query to select all appointments from the database
        String sql = "SELECT * FROM CLIENT_SCHEDULE.APPOINTMENTS";
        //Create a PreparedStatement to execute the SQL query
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        //Execute the query and retrieve the result set
        ResultSet rs = ps.executeQuery();
        //Loops through the result set using the rs next method

        try {
            // Loop through the result set using the rs next method
            while (rs.next()) {
                // Extract appointment details from the result set
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp startTimestamp = rs.getTimestamp("Start");
                Timestamp endTimestamp = rs.getTimestamp("End");
                LocalDateTime startLocal = startTimestamp.toLocalDateTime();
                LocalDateTime endLocal = endTimestamp.toLocalDateTime();
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                // Creates an Appointments object
                Appointments appointment = new Appointments(appointmentID, title, description, location, type,
                        startLocal, endLocal, customerID, userID, contactID);
                // Adds the appointment to the appointments list created above
                appointments.add(appointment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Returns a list of all appointments

        }
            return appointments;
        }


    /**
     * Retrieves appointments from the database based on the contact ID.
     *
     * @return an array list of Appointments containing all appointments retrieved from the database
     * @throws SQLException if SQL exception occurs
     * @param contactID
     */
    public static ObservableList<Appointments> getAppointmentsByContactID(int contactID) throws SQLException {
        //Initialize an empty ObservableList to store the retrieved appointments
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        //SQL query to select all appointments from the database based on contact ID
        String sql = "SELECT A.*, C.Contact_Name " +
                "FROM CLIENT_SCHEDULE.APPOINTMENTS A " +
                "JOIN CLIENT_SCHEDULE.CONTACTS C ON A.Contact_ID = C.Contact_ID " +
                "WHERE A.Contact_ID = ?";
        //Create a PreparedStatement to execute the SQL query
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        try {
            // Set the contact ID parameter for the PreparedStatement
            ps.setInt(1, contactID);
            //Execute the query and retrieve the result set
            try (ResultSet rs = ps.executeQuery()) {
                //Loops through the result set using the rs next method
                while (rs.next()) {
                    // Extract appointment details from the result set
                    int nContactID = rs.getInt("Contact_ID");
                    String contactName = rs.getString("Contact_Name");
                    int appointmentID = rs.getInt("Appointment_ID");
                    String type = rs.getString("Type");
                    String description = rs.getString("Description");
                    LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                    int customerID = rs.getInt("Customer_ID");
                    // Creates an Appointments object
                    Appointments appointment = new Appointments(contactID, contactName, appointmentID, type, description, start, end, customerID);
                    // Adds the appointment to the ObservableList
                    appointments.add(appointment);
                }
            }
        } catch (Exception ee){
            ee.printStackTrace();
        }
        // Returns ObservableList containing all appointments
        return appointments;
    }

    /**
     * Inserts appointments into the database.
     *
     * @throws SQLException if SQL exception occurs
     * @param appointment
     */
    public static void insertAppointment(Appointments appointment) throws SQLException {
        // Obtain a connection to the database
        Connection connection = DBConnection.getConnection();
        try {
            // Gets start and end times
            LocalDateTime start = appointment.getStart();
            LocalDateTime end = appointment.getEnd();

            //SQL query to insert appointments into the database
            String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            //Create a PreparedStatement to execute the SQL query
            PreparedStatement ps4 = connection.prepareStatement(sql);
            try {
                // Set the parameters for the PreparedStatement
                ps4.setString(1, appointment.getTitle());
                ps4.setString(2, appointment.getDescription());
                ps4.setString(3, appointment.getLocation());
                ps4.setString(4, appointment.getType());
                ps4.setTimestamp(5, Timestamp.valueOf(start));
                ps4.setTimestamp(6, Timestamp.valueOf(end));
                ps4.setInt(7, appointment.getCustomerID());
                ps4.setInt(8, appointment.getUserID());
                ps4.setInt(9, appointment.getContactID());

                // Executes the SQL statement
                ps4.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    /**
     * Deletes the selected appointment from the database.
     *
     * @param selectedAppointment
     * @return true if the appointment was deleted
     */
    public static boolean deleteAppointment(Appointments selectedAppointment) {

        // Obtain a connection to the database
        Connection connection = DBConnection.getConnection();
        try {
            // If connection is closed, it will be opened.
            if (connection.isClosed()) {
                DBConnection.openConnection();
            }
            // Prepare the SQL statement for deleting the appointment
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
            //Create a PreparedStatement to execute the SQL query
            try (PreparedStatement ps4 = connection.prepareStatement(sql)) {
                // Set the appointment ID as the parameter for the PreparedStatement
                ps4.setInt(1, selectedAppointment.getAppointmentID());
                // Execute the SQL statement to delete the appointment
                int rowsAffected = ps4.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Appointment deleted successfully.");
                } else {
                    System.out.println("Failed to delete appointment.");
                }
            }
            // Remove the appointment from the list of all appointments
            return Appointments.getAllAppointments().remove(selectedAppointment);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exceptions that occur during the deletion process
        }
        return false;
    }

    /**
     * Updates the selected appointment in the database.
     *
     * @param appointment
     */
    public static void updateAppointment(Appointments appointment) {
        // Obtain a connection to the database
        Connection connection = DBConnection.getConnection();
        try {
            // Gets the start and end times
            LocalDateTime start = appointment.getStart();
            LocalDateTime end = appointment.getEnd();

            //SQL query to insert appointments into the database
            String sql = "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            //Create a PreparedStatement to execute the SQL query
            PreparedStatement ps = connection.prepareStatement(sql);
            try {
                // Set the parameters for the PreparedStatement
                ps.setString(1, appointment.getTitle());
                ps.setString(2, appointment.getDescription());
                ps.setString(3, appointment.getLocation());
                ps.setString(4, appointment.getType());
                ps.setTimestamp(5, Timestamp.valueOf(start));
                ps.setTimestamp(6, Timestamp.valueOf(end));
                ps.setInt(7, appointment.getCustomerID());
                ps.setInt(8, appointment.getUserID());
                ps.setInt(9, appointment.getContactID());
                ps.setInt(10, appointment.getId());
                // Executes the SQL statement
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    /**
     * Checks if there are any appointments associated with a specific customer ID.
     *
     * @param customerID
     * @return true if there are associated appointments
     * @throws SQLException if an SQL exception occurs
     */
    public static boolean associatedAppointments(int customerID) throws SQLException {
        boolean associatedAppointment = false;
        // Obtain a connection to the database
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM appointments WHERE customer_ID = ?");
        try {
            // Set the customer ID parameter for the PreparedStatement
            statement.setInt(1, customerID);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int appointmentCount = rs.getInt(1);
                    associatedAppointment = appointmentCount > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return associatedAppointment;
    }

    /**
     * Checks for upcoming appointments within the next 15 minutes and displays an alert if found.
     *
     * @return true if there are existing upcoming appointments
     * @throws SQLException if an SQL exception occurs
     */
    public static boolean checkUpcomingAppointments() throws SQLException {
        // Get the current time
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime fifteenMinutesLater = currentTime.plusMinutes(15);

        // Query the database to check for upcoming appointments within the next 15 minutes
        // Obtain a connection to the database
        Connection connection = DBConnection.getConnection();
        //SQL query to obtain appointment ID from the database
        String sql = "SELECT Appointment_ID, Start FROM appointments WHERE Start BETWEEN ? AND ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        try {
            ps.setTimestamp(1, Timestamp.valueOf(currentTime));
            ps.setTimestamp(2, Timestamp.valueOf(fifteenMinutesLater));
            ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    // If there are upcoming appointments, build the message
                    StringBuilder message = new StringBuilder();
                    message.append("You have the following upcoming appointments within the next 15 minutes:\n\n");
                    do {
                        int appointmentId = rs.getInt("Appointment_ID");
                        LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
                        message.append("Appointment ID: ").append(appointmentId).append("\n");
                        message.append("Date: ").append(appointmentStart.toLocalDate()).append("\n");
                        message.append("Time: ").append(appointmentStart.toLocalTime()).append("\n\n");
                    } while (rs.next());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Upcoming Appointments");
                    alert.setHeaderText("Appointments:");
                    alert.setContentText(message.toString());
                    alert.showAndWait();
                } else {
                    // Displays a message indicating that there are no upcoming appointments within the next 15 minutes
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Upcoming Appointments");
                    alert.setHeaderText("Appointments:");
                    alert.setContentText("There are no appointments scheduled within the next 15 minutes.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Counts the number of appointments in a specific month.
     *
     * @param month
     * @return the number of appointments in the selected month
     * @throws SQLException if an SQL exception occurs
     */
    public static int countAppointmentsByMonth(Month month) throws SQLException {
        // SQL query to count appointments in the specified month
        String sql = "SELECT COUNT(*) FROM appointments WHERE MONTH(start) = ?";
        // Obtain a connection to the database
        Connection conn = DBConnection.getConnection();
        //Create a PreparedStatement to execute the SQL query
        PreparedStatement statement = conn.prepareStatement(sql);
        try {
            statement.setInt(1, month.getValue());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (Exception ee){
            ee.printStackTrace();
        }
        return 0; // Return 0 if no appointments found
    }


    /**
     * Counts the number of appointments in a specific type.
     *
     * @param type
     * @return the number of appointments by type
     * @throws SQLException if an SQL exception occurs
     */
    public static int countAppointmentsByType(String type) throws SQLException {
        // SQL query to count appointments in the specified month
        String sql = "SELECT COUNT(*) FROM appointments WHERE type = ?";
        // Obtain a connection to the database
        Connection conn = DBConnection.getConnection();
        //Create a PreparedStatement to execute the SQL query
        PreparedStatement statement = conn.prepareStatement(sql);
        try {
            statement.setString(1, type);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (Exception ee){
            ee.printStackTrace();
        }
        // Return 0 if no appointments found
        return 0;
    }
}

