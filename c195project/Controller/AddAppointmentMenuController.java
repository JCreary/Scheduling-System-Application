package com.example.c195project.Controller;

import com.example.c195project.Model.*;
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
import java.time.format.DateTimeFormatter;

/**
 * AddAppointmentMenuController class allows for the user to Add an Appointment.
 *
 * @author Jamal Creary
 */

public class AddAppointmentMenuController {

    public TextField addAppointmentIDAppointment;
    public TextField addTitleAppointment;
    public TextField addDescriptionAppointment;
    public TextField addLocationAppointment;
    public DatePicker addStartDateAppointment;
    public DatePicker addEndDateAppointment;
    public ComboBox addTypeAppointment;
    public ComboBox addCustomerIDAppointment;
    public ComboBox addUserIDAppointment;
    public ComboBox addStartTimeAppointment;
    public ComboBox addEndTimeAppointment;
    public ComboBox addContactIDAppointment;

    /**
     * Initializes the Add Appointment menu and populates combo boxes with items/objects.
     *
     * @throws SQLException if SQL exception occurs
     */
    public void initialize() throws SQLException {
        // Populate start time combo box with 15-minute intervals from 00:00 to 23:45
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                addStartTimeAppointment.getItems().add(LocalTime.of(hour, minute));
            }
        }

        // Set prompt text and visibility for start time combo box
        addStartTimeAppointment.setPromptText("Please select a start time before an end time: ");
        addStartTimeAppointment.setVisibleRowCount(5);

        // Set prompt text and visibility for end time combo box
        addEndTimeAppointment.setPromptText("Please select an end time: ");
        addEndTimeAppointment.setVisibleRowCount(5);

        // Sets listener for changes in start time selection
        addStartTimeAppointment.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            // Lambda expression starts at the arrow with 3 parameters: observableValue, oldValue, newValue

            if (newValue != null) {
                // Clears previous end times
                addEndTimeAppointment.getItems().clear();
                LocalTime selectedStartTime = (LocalTime) newValue;
                // End time should include 24:00 or 00:00 which are the same times
                // Loop iterates over each hour starting the selected start time until 23
                for (int hour = selectedStartTime.getHour(); hour < 24; hour++) {
                    int startMinute = (hour == selectedStartTime.getHour()) ? selectedStartTime.getMinute() + 15 : 0;
                    // Iterate over each minute from startMinute to 59 in 15-minute intervals
                    for (int minute = startMinute; minute < 60; minute += 15) {
                        // Add the time to end time combo box
                        addEndTimeAppointment.getItems().add(LocalTime.of(hour, minute));
                    }
                }
                // Add 00:00 (midnight)
                addEndTimeAppointment.getItems().add(LocalTime.MIDNIGHT);
            }
        });

        ObservableList<Appointments> appointments1 = DBAppointments.getAllAppointments();

        // Iterate through appointments and add unique types to the ComboBox
        for (Appointments appointment : appointments1) {
            String type = appointment.getType();
            if (!addTypeAppointment.getItems().contains(type)) {
                addTypeAppointment.getItems().add(type);
            }
        }
        //Sets visible rows
        addTypeAppointment.setVisibleRowCount(5);
        //Sets visible prompts
        addTypeAppointment.setPromptText("Please select a Type: ");

        ObservableList<Customers> customers = DBCustomers.getAllCustomers();
        //Sets Customer ID in combo box
        addCustomerIDAppointment.setItems(customers);
        //Sets visible rows
        addCustomerIDAppointment.setVisibleRowCount(5);
        //Sets visible prompts
        addCustomerIDAppointment.setPromptText("Please select a Customer ID: ");


        ObservableList<Users> users = DBUsers.getAllUsers();
        //Sets User ID in combo box
        addUserIDAppointment.setItems(users);
        //Sets visible rows
        addUserIDAppointment.setVisibleRowCount(5);
        //Sets visible prompts
        addUserIDAppointment.setPromptText("Please select a User ID: ");

        ObservableList<Contacts> contacts = DBContacts.getAllContacts();
        //Sets Contact ID in combo box
        addContactIDAppointment.setItems(contacts);
        //Sets visible rows
        addContactIDAppointment.setVisibleRowCount(5);
        //Sets visible prompts
        addContactIDAppointment.setPromptText("Please select a Contact ID: ");
            }

    /**
     * Handles the save action function for when a user adds a new appointment.
     * Also, validates the selected start and end times, checks for overlaps with existing appointments,
     * and inserts the new appointment into the database.
     *
     * @param actionEvent
     */
    public void onClickSaveAddAppointment(ActionEvent actionEvent) {
        //Validates text fields
        if (addTitleAppointment.getText().trim().isEmpty() || addDescriptionAppointment.getText().trim().isEmpty() || addLocationAppointment.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please make sure that you provide a title, description, and location before you continue.");
            alert.showAndWait();
            return;
        }
        //Validates combo boxes
        if (addTypeAppointment.getValue() == null || addCustomerIDAppointment.getValue() == null || addUserIDAppointment.getValue() == null || addContactIDAppointment.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please make sure that you select a type, customer ID, user ID, and contact ID before you continue.");
            alert.showAndWait();
            return;
        }
        // Define business hours in EST
        ZoneId estZone = ZoneId.of("America/New_York");
        LocalTime startBusinessHours = LocalTime.of(8, 0);
        LocalTime endBusinessHours = LocalTime.of(22, 0);

        // Convert user-selected times to EST
        ZonedDateTime startDateTime1 = addStartDateAppointment.getValue().atTime((LocalTime) addStartTimeAppointment.getValue()).atZone(estZone);
        ZonedDateTime endDateTime1 = addEndDateAppointment.getValue().atTime((LocalTime) addEndTimeAppointment.getValue()).atZone(estZone);

        //New start and end times
        LocalTime newStartTime = startDateTime1.toLocalTime();
        LocalTime newEndTime = endDateTime1.toLocalTime();

        // Checks if start and end times are selected
        if (newStartTime == null || newEndTime == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a start and end time.");
            alert.showAndWait();
            return;
        }

        //Checks if start and end time is in between business hours
        if (newStartTime.isBefore(startBusinessHours) || newEndTime.isAfter(endBusinessHours)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Selected appointment times must be within the business hours of 8:00 - 22:00 EST");
            alert.showAndWait();
            return;
        }

        // Check if end time is midnight
        if (newEndTime.equals(LocalTime.MIDNIGHT)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Selected appointment times must be within the business hours of 8:00 - 22:00 EST");
            alert.showAndWait();
            return;

        }
        //Obtains all appointments from DAO
        ObservableList<Appointments> allAppointments;
        try {
            allAppointments = DBAppointments.getAllAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Check if date pickers have valid values
        LocalDate startDate = addStartDateAppointment.getValue();
        LocalDate endDate = addEndDateAppointment.getValue();
        //Validates start and end date in the case the field becomes editable
        if (addStartDateAppointment.getValue() == null || addEndDateAppointment.getValue() == null) {
            // Show an error message if either start or end date is not selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select valid start and end dates.");
            alert.showAndWait();
            return;
        }
        if (endDate.isBefore(startDate)) {
            // Show an error message if the end date is before the start date
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("End date cannot be before start date.");
            alert.showAndWait();
            return;
        }

        // Create the new appointment object
        Appointments newAppointment = new Appointments();
        newAppointment.setStart(LocalDateTime.of(addStartDateAppointment.getValue(), newStartTime));
        newAppointment.setEnd(LocalDateTime.of(addEndDateAppointment.getValue(), newEndTime));

        // Check for overlap with existing appointments
        for (Appointments appointment : allAppointments) {
            LocalDateTime existingStart = appointment.getStart();
            LocalDateTime existingEnd = appointment.getEnd();

            //Error alert if appointments overlap
            if ((newAppointment.getStart().isBefore(existingEnd) && newAppointment.getEnd().isAfter(existingStart))
                    || newAppointment.getStart().isEqual(existingStart) || newAppointment.getEnd().isEqual(existingEnd)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Appointment overlaps with an existing appointment. Please select different appointment times.");
                alert.showAndWait();
                return;
            }
        }
        try {
            // Creates newappointment object and set its properties
            Appointments appointment = new Appointments();
            appointment.setTitle(addTitleAppointment.getText());
            appointment.setDescription(addDescriptionAppointment.getText());
            appointment.setLocation(addLocationAppointment.getText());

            // Creates a formatter for date and time
            DateTimeFormatter dTformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");

            // Sets a start date and start time
            startDate = addStartDateAppointment.getValue();
            LocalTime startTime = (LocalTime) addStartTimeAppointment.getValue();
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

            // Formats the start date and start time using the formatter
            String formattedStartDT= startDateTime.format(dTformatter);

            // Parses the formatted string in LocalDateTime
            LocalDateTime parsedStartDT = LocalDateTime.parse(formattedStartDT, dTformatter);

            // Sets the end date and time
            endDate = addEndDateAppointment.getValue();
            LocalTime endTime = (LocalTime) addEndTimeAppointment.getValue();
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

            // Formats the end date and end time using the formatter
            String formattedEndDT = endDateTime.format(dTformatter);

            // Parses the formatted string in LocalDateTime
            LocalDateTime parsedEndDT = LocalDateTime.parse(formattedEndDT, dTformatter);

            // Sets the start and end date and time to the appointment object
            appointment.setStart(parsedStartDT);
            appointment.setEnd(parsedEndDT);

            //Sets Type
            String selectedType = (String) addTypeAppointment.getValue();
            if (selectedType != null) {
                appointment.setType(selectedType);
            }
            // Sets customerID
            Customers selectedCustomer = (Customers) addCustomerIDAppointment.getValue();
            if (selectedCustomer != null) {
                appointment.setCustomerID(selectedCustomer.getCustomerID());
            }
            //Sets UserID
            Users selectedUser = (Users) addUserIDAppointment.getValue();
            if (selectedUser != null) {
                appointment.setUserID(selectedUser.getUserID());
            }
            //Sets ContactID
            Contacts selectedContact = (Contacts) addContactIDAppointment.getValue();
            if (selectedContact != null) {
                appointment.setContactID(selectedContact.getContactID());
            }
            try {
                // Calls the method in DBAppointments to insert the appointment
                DBAppointments.insertAppointment(appointment);
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("An error occurred during the process of adding the appointment!");
                alert.showAndWait();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("An error occurred during the process of adding the appointment!");
            alert.showAndWait();
        }
    }

    /**
     * Cancels the process of adding an appointment and directs the user
     * back to the Main Menu controller.
     *
     * @param actionEvent
     */
    public void onClickCancelAddAppointment(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
