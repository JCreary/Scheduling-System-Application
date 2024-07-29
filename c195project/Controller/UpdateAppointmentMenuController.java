package com.example.c195project.Controller;

import com.example.c195project.Model.DBAppointments;
import com.example.c195project.Model.DBContacts;
import com.example.c195project.Model.DBCustomers;
import com.example.c195project.Model.DBUsers;
import com.example.c195project.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;

import static com.example.c195project.Model.DBAppointments.updateAppointment;

/**
 * UpdateAppointmentMenuController class allows for the user to Update an Appointment.
 *
 * @author Jamal Creary
 */
public class UpdateAppointmentMenuController {
    public TextField updateAppointmentIDAppointment;
    public TextField updateTitleAppointment;
    public TextField updateDescriptionAppointment;
    public TextField updateLocationAppointment;
    public DatePicker updateStartDateAppointment;
    public DatePicker updateEndDateAppointment;
    public ComboBox updateTypeAppointment;
    public ComboBox updateUserIDAppointment;
    public ComboBox updateCustomerIDAppointment;
    public ComboBox updateStartTimeAppointment;
    public ComboBox updateEndTimeAppointment;
    public ComboBox updateContactIDAppointment;


    /**
     * Initializes the Update Appointment menu and populates combo boxes with items/objects including
     * start and end time ComboBoxes with intervals of 15 minutes.
     * LambdaExpression#2: included within method to help handle the updating the end time combo box
     * based on the selected start time. The lambda expression offers a more concise way to
     * handle events.
     *
     * @throws SQLException if SQL exception occurs
     */

    public void initialize() throws SQLException {
        // Populate start time combo box with 15-minute intervals from 00:00 to 23:45
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                updateStartTimeAppointment.getItems().add(LocalTime.of(hour, minute));
            }
        }

        // Set prompt text and visibility for start time combo box
        updateStartTimeAppointment.setPromptText("Please select a start time before an end time: ");
        updateStartTimeAppointment.setVisibleRowCount(5);

        // Listener to update end time options based on selected start time
        updateStartTimeAppointment.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            // Lambda expression#2 starts at the arrow with 3 parameters: observableValue, oldValue, newValue
            if (newValue != null) {
                // Clear previous end times
                updateEndTimeAppointment.getItems().clear();

                // Get the selected start time
                LocalTime selectedStartTime = (LocalTime) newValue;

                // Loop iterates over each hour starting from the selected start time until 23
                for (int hour = selectedStartTime.getHour(); hour < 24; hour++) {
                    int startMinute = (hour == selectedStartTime.getHour()) ? selectedStartTime.getMinute() + 15 : 0;
                    // Iterate over each minute from startMinute to 59 in 15-minute intervals
                    for (int minute = startMinute; minute < 60; minute += 15) {
                        // Add the time to the end time combo box
                        updateEndTimeAppointment.getItems().add(LocalTime.of(hour, minute));
                    }
                }
                // Add 00:00 (midnight)
                updateEndTimeAppointment.getItems().add(LocalTime.MIDNIGHT);

                // Check if midnight is selected as the end time
                if (LocalTime.MIDNIGHT.equals(updateEndTimeAppointment.getValue())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("Selected end time is out of business hours.");
                    alert.showAndWait();
                }
            } else {
                // If start time is null, clear end time options and set prompt text
                updateEndTimeAppointment.getItems().clear();
                updateEndTimeAppointment.setPromptText("Please select a start time first");
            }

        });

        // Appointment1 observable list gets all appointments
        ObservableList<Appointments> appointments1 = DBAppointments.getAllAppointments();
        // Iterate through appointments and add types to the ComboBox
        for (Appointments appointment : appointments1) {
            String type = appointment.getType();
            if (!updateTypeAppointment.getItems().contains(type)) {
                updateTypeAppointment.getItems().add(type);
            }
        }
    }

    /**
     * Handles the action event when the "Save" button is clicked to update the appointment.
     *
     * @param actionEvent
     */

    public void onClickSaveUpdateAppointment(ActionEvent actionEvent) {
        // Validates text fields
        if (updateTitleAppointment.getText().trim().isEmpty() ||
                updateDescriptionAppointment.getText().trim().isEmpty() ||
                updateLocationAppointment.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please make sure that you provide a title, description, and location before you continue.");
            alert.showAndWait();
            return;
        }

        // Validates combo boxes
        if (updateTypeAppointment.getValue() == null ||
                updateCustomerIDAppointment.getValue() == null ||
                updateUserIDAppointment.getValue() == null ||
                updateContactIDAppointment.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please make sure that you select a type, customer ID, user ID, and contact ID before you continue.");
            alert.showAndWait();
            return;
        }

        // Validates selected start and end times
        if (updateStartTimeAppointment.getValue() == null || updateEndTimeAppointment.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please make sure that you select both a start and end time before you continue.");
            alert.showAndWait();
            return;
        }

        // New start and end times
        LocalTime newStartTime = (LocalTime) updateStartTimeAppointment.getValue();
        LocalTime newEndTime = (LocalTime) updateEndTimeAppointment.getValue();

        // Validates selected date
        if (updateStartDateAppointment.getValue() == null || updateEndDateAppointment.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a start and end date");
            alert.showAndWait();
            return;
        }
        // Check if end time is midnight
        if (newEndTime.equals(LocalTime.MIDNIGHT)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Selected appointment times must be within the business hours of 8:00 - 22:00");
            alert.showAndWait();
            return;
        }

        // Define LocalTime representing 00:00
        LocalTime midnight = LocalTime.of(0, 0);

        // Sets business hours
        LocalTime startBusinessHours = LocalTime.of(8, 0);
        LocalTime endBusinessHours = LocalTime.of(22, 0);

        // Validates if the selected end time is equal to midnight
        if (endBusinessHours.equals(midnight) || endBusinessHours.equals(LocalTime.MIDNIGHT)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Selected appointment times must be within the business hours of 8:00 - 22:00");
            alert.showAndWait();
            return;
        }

        // Validates if start and end time is outside of business hours
        if (newStartTime.isBefore(startBusinessHours) || newEndTime.isAfter(endBusinessHours)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Selected appointment times must be within the business hours of 8:00 - 22:00");
            alert.showAndWait();
            return;
        }

        // Validates selected time range
        LocalTime startTime1 = (LocalTime) updateStartTimeAppointment.getValue();
        LocalTime endTime1 = (LocalTime) updateEndTimeAppointment.getValue();

        // Set business hours in Eastern Time (ET)
        ZoneId etTimeZone = ZoneId.of("America/New_York");
        ZonedDateTime startET = ZonedDateTime.of(LocalDate.now(), startTime1, etTimeZone);
        ZonedDateTime endET = ZonedDateTime.of(LocalDate.now(), endTime1, etTimeZone);

        ZonedDateTime startBusinessHoursET = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8, 0), etTimeZone);
        ZonedDateTime endBusinessHoursET = ZonedDateTime.of(LocalDate.now(), LocalTime.of(22, 0), etTimeZone);

        // Check if the selected time range is within business hours or if the end time is midnight
        if (startET.isBefore(startBusinessHoursET) || endET.isAfter(endBusinessHoursET) || endET.equals(midnight)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Selected appointment times must be within the business hours of 8:00 - 22:00");
            alert.showAndWait();
            return;
        }

        // Retrieves all existing appointments
        ObservableList<Appointments> allAppointments;
        try {
            allAppointments = DBAppointments.getAllAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Retrieves the original/initial appointment that the user selected to update
        int id = Integer.parseInt(updateAppointmentIDAppointment.getText());
        Appointments initialAppointment = null;
        for (Appointments appointment : allAppointments) {
            if (appointment.getId() == id) {
                initialAppointment = appointment;
                break;
            }
        }

        if (initialAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Unable to find the original/initial appointment to update");
            alert.showAndWait();
            return;
        }

        // Creates new appointment object
        Appointments newAppointment = new Appointments();
        newAppointment.setCustomerID(Integer.parseInt(updateCustomerIDAppointment.getValue().toString()));
        newAppointment.setUserID(Integer.parseInt(updateUserIDAppointment.getValue().toString()));
        newAppointment.setContactID(Integer.parseInt(updateContactIDAppointment.getValue().toString()));
        newAppointment.setTitle(updateTitleAppointment.getText());
        newAppointment.setDescription(updateDescriptionAppointment.getText());
        newAppointment.setLocation(updateLocationAppointment.getText());
        newAppointment.setType(updateTypeAppointment.getValue().toString());
        newAppointment.setStart(LocalDateTime.of(updateStartDateAppointment.getValue(), newStartTime));
        newAppointment.setEnd(LocalDateTime.of(updateEndDateAppointment.getValue(), newEndTime));

        // Checks for overlap with existing appointments
        boolean existingOverlap = false;
        // Only check for overlap if start or end times have changed
        if (!newAppointment.getStart().isEqual(initialAppointment.getStart()) || !newAppointment.getEnd().isEqual(initialAppointment.getEnd())) {
            for (Appointments appointment : allAppointments) {
                if (appointment.getId() != id && checksForOverlap(newAppointment, appointment)) {
                    existingOverlap = true;
                    break;
                }
            }
        }

        // Displays an error message if overlap is exists
        if (existingOverlap) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Appointment overlaps with an existing appointment. Please select a different appointment time.");
            alert.showAndWait();
            return;
        }

        // Updates the appointment
        try {
            // Retrieves updated appointment details
            String title = updateTitleAppointment.getText();
            String description = updateDescriptionAppointment.getText();
            String location = updateLocationAppointment.getText();
            String type = updateTypeAppointment.getValue().toString();
            LocalDate startDate = updateStartDateAppointment.getValue();
            LocalTime startTime = (LocalTime) updateStartTimeAppointment.getValue();
            LocalDateTime start = LocalDateTime.of(startDate, startTime);

            LocalDate endDate = updateEndDateAppointment.getValue();
            LocalTime endTime = (LocalTime) updateEndTimeAppointment.getValue();
            LocalDateTime end = LocalDateTime.of(endDate, endTime);

            int customerID = Integer.parseInt(updateCustomerIDAppointment.getValue().toString());
            int userID = Integer.parseInt(updateUserIDAppointment.getValue().toString());
            int contactID = Integer.parseInt(updateContactIDAppointment.getValue().toString());

            // Creates a new appointment object with the updated data
            Appointments updatedAppointment = new Appointments(id, title, description, location, type, start, end, customerID, userID, contactID);
            updateAppointment(updatedAppointment);
            System.out.println("Appointment updated successfully.");

            // Navigate back to the main screen
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NumberFormatException e) {
            // Handle parsing errors
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }
    }

    // Method created to check for overlap between appointments
    private boolean checksForOverlap(Appointments newAppointment, Appointments existingAppointment) {
        LocalDateTime newStart = newAppointment.getStart();
        LocalDateTime newEnd = newAppointment.getEnd();
        LocalDateTime existingStart = existingAppointment.getStart();
        LocalDateTime existingEnd = existingAppointment.getEnd();
        return (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)) ||
                newStart.isEqual(existingStart) || newEnd.isEqual(existingEnd);
    }


    /**
     * Cancels the process of updating an appointment and directs the user
     * back to the Main Menu controller.
     *
     * @param actionEvent
     */
    public void onClickCancelUpdateAppointment(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Obtains appointment information from the main menu screen.
     *
     * @param updatedAppointment
     * @throws SQLException if SQL exception occurs
     */
    public void sendAppointment(Appointments updatedAppointment) throws SQLException {
        //Set appointment details
        updateAppointmentIDAppointment.setText(String.valueOf(updatedAppointment.getAppointmentID()));
        updateTitleAppointment.setText(updatedAppointment.getTitle());
        updateDescriptionAppointment.setText(String.valueOf(updatedAppointment.getDescription()));
        updateLocationAppointment.setText(String.valueOf(updatedAppointment.getLocation()));
        updateStartDateAppointment.setValue(updatedAppointment.getStart().toLocalDate());
        updateEndDateAppointment.setValue(updatedAppointment.getEnd().toLocalDate());
        updateStartTimeAppointment.setValue(updatedAppointment.getStart().toLocalTime());
        updateEndTimeAppointment.setValue(updatedAppointment.getEnd().toLocalTime());
        updateTypeAppointment.setItems(DBAppointments.getAllAppointments());
        updateTypeAppointment.getSelectionModel().select(String.valueOf(updatedAppointment.getType()));

        ObservableList<Appointments> allAppointments = DBAppointments.getAllAppointments();
        ObservableList<String> type = FXCollections.observableArrayList();
        // Load type into the combo box
        for (Appointments appointments : allAppointments) {
            String type1 = appointments.getType();
            // Checks if the type is not present in the combo box's items
            if (!type.contains(type1)) {
                // If not present, it's added to the list
                type.add(type1);
            }
        }
        // Set the items of the combo box and select the updated appointment's type
        updateTypeAppointment.setItems(type);
        updateTypeAppointment.getSelectionModel().select(updatedAppointment.getType());
        updateCustomerIDAppointment.setItems(DBCustomers.getAllCustomers());
        updateCustomerIDAppointment.getSelectionModel().select(String.valueOf(updatedAppointment.getCustomerID()));
            // Load customer IDs into the combo box
            ObservableList<Customers> allCustomers = DBCustomers.getAllCustomers();
            ObservableList<Integer> customerIDs = FXCollections.observableArrayList();
            for (Customers customers : allCustomers) {
                customerIDs.add(customers.getCustomerID());
            }
            updateCustomerIDAppointment.setItems(customerIDs);
            updateCustomerIDAppointment.getSelectionModel().select(updatedAppointment.getCustomerID());
            updateUserIDAppointment.setItems(DBUsers.getAllUsers());
            updateUserIDAppointment.getSelectionModel().select(String.valueOf(updatedAppointment.getUserID()));
            // Load user IDs into the combo box
            ObservableList<Users> allUsers = DBUsers.getAllUsers();
            ObservableList<Integer> userIDs = FXCollections.observableArrayList();
            for (Users users : allUsers) {
                userIDs.add(users.getUserID());
            }
            updateUserIDAppointment.setItems(userIDs);
            updateUserIDAppointment.getSelectionModel().select(updatedAppointment.getUserID());
            updateContactIDAppointment.setItems(DBContacts.getAllContacts());
            updateContactIDAppointment.getSelectionModel().select(String.valueOf(updatedAppointment.getContactID()));
            // Load contact IDs into the combo box
            ObservableList<Contacts> allContacts = DBContacts.getAllContacts();
            ObservableList<Integer> contactIDs = FXCollections.observableArrayList();
            for (Contacts contacts : allContacts) {
                contactIDs.add(contacts.getContactID());
            }
            updateContactIDAppointment.setItems(contactIDs);
            updateContactIDAppointment.getSelectionModel().select(updatedAppointment.getContactID());
        }
}
