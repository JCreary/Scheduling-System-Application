package com.example.c195project.Controller;

import com.example.c195project.Model.DBAppointments;
import com.example.c195project.Model.DBCountries;
import com.example.c195project.Model.DBCustomers;
import com.example.c195project.Model.DBDivisions;
import com.example.c195project.Model.Appointments;
import com.example.c195project.Model.Countries;
import com.example.c195project.Model.Customers;
import com.example.c195project.Model.Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.util.Locale.filter;
/**
 * MainMenuController class allows for the user to manage Customer and Appointment.
 *
 * @author Jamal Creary
 */

public class MainMenuController implements Initializable {
    public TableView appointmentTableview;
    public TableColumn appointmentIDMainMenu;
    public TableColumn titleMainMenu;
    public TableColumn descriptionMainMenu;
    public TableColumn locationMainMenu;
    public TableColumn typeMainMenu;
    public TableColumn startMainMenu;
    public TableColumn endMainMenu;
    public TableView customerTableview;
    public TableColumn customerIDMainMenu;
    public TableColumn userIDMainMenu;
    public TableColumn contactIDMainMenu;
    public TableColumn customerIDCustomerRecords;
    public TableColumn customerNameCustomerRecords;
    public TableColumn addressCustomerRecords;
    public TableColumn postalCodeCustomerRecords;
    public TableColumn phoneNumberCustomerRecords;
    public TableColumn divisionIDCustomerRecords;
    public RadioButton currentMonthAppointments;
    public RadioButton allAppointmentsAppointments;
    public RadioButton currentWeekAppointments;
    public ToggleGroup appointmentViewTg;
    public TableColumn divisionCustomerRecords;
    public TableColumn countryCustomerRecords;

    /**
     * This method initializes the main menu controller and populates the customer and appointment table.
     *
     * @param url            The location used to resolve relative paths for the root object or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Retrieves data from DBAppointments.getAllAppointments()
            ObservableList<Appointments> allAppointmentsList = DBAppointments.getAllAppointments();

            // Populates data in Appointment table
            appointmentIDMainMenu.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleMainMenu.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionMainMenu.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationMainMenu.setCellValueFactory(new PropertyValueFactory<>("location"));
            typeMainMenu.setCellValueFactory(new PropertyValueFactory<>("type"));
            startMainMenu.setCellValueFactory(new PropertyValueFactory<>("start"));
            endMainMenu.setCellValueFactory(new PropertyValueFactory<>("end"));
            customerIDMainMenu.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            userIDMainMenu.setCellValueFactory(new PropertyValueFactory<>("userID"));
            contactIDMainMenu.setCellValueFactory(new PropertyValueFactory<>("contactID"));
            appointmentTableview.setItems(allAppointmentsList);

            // Selects the allAppointmentsAppointment radio button when the user logs in
            allAppointmentsAppointments.setSelected(true);

            // Call onSelectAllAppointments to ensure all appointments are initially displayed
            onSelectAllAppointments(null);

            // Retrieves data from DBCustomers.getAllCustomers()
            ObservableList<Customers> allCustomersList = DBCustomers.getAllCustomers();

            // Populate division and country for each customer
            for (Customers customer : allCustomersList) {
                for (Divisions division : DBDivisions.getAllDivisions()) {
                    if (division.getDivisionID() == customer.getDivisionID()) {
                        customer.setDivision(division.getDivision());
                    }
                }
                for (Countries countries : DBCountries.getAllCountries()) {
                    if (countries.getCountry().equals(customer.getCountry())) {
                        customer.setCountry(countries.getCountry());
                    }
                }
            }
            // Populates data in Customer table
            customerIDCustomerRecords.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerNameCustomerRecords.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            addressCustomerRecords.setCellValueFactory(new PropertyValueFactory<>("address"));
            postalCodeCustomerRecords.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phoneNumberCustomerRecords.setCellValueFactory(new PropertyValueFactory<>("phone"));
            divisionIDCustomerRecords.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
            divisionCustomerRecords.setCellValueFactory(new PropertyValueFactory<>("division"));
            countryCustomerRecords.setCellValueFactory(new PropertyValueFactory<>("country"));
            customerTableview.setItems(allCustomersList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * On click of button, directs user to add appointment menu
     *
     * @param actionEvent
     * @throws IOException from FXMLLoader
     */
    public void onClickAddAppointment(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/AddAppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * On click of button, directs user to update appointment menu
     *
     * @param actionEvent
     * @throws IOException from FXMLLoader
     * @throws SQLException if there is an error while accessing the SQL database
     */
    public void onClickUpdateAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        //Retrieves the selected appointment from the appointment table
        Appointments appointmentSelected = (Appointments) appointmentTableview.getSelectionModel().getSelectedItem();
        //If no appointment is selected, an error message is displayed.
        if (appointmentSelected == null) {
            Alert existingAppointment = new Alert(Alert.AlertType.ERROR);
            existingAppointment.setTitle("Error Message");
            existingAppointment.setContentText("No appointment was selected to update. Please select an appointment to update: ");
            existingAppointment.show();
        }
        //Loads update appointment menu controller
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/c195project/UpdateAppointmentMenu.fxml"));
        loader.load();
        //Loads the selected appointment to the update appointment menu controller
        UpdateAppointmentMenuController UpdateAppointmentMenu = loader.getController();
        UpdateAppointmentMenu.sendAppointment((Appointments) appointmentTableview.getSelectionModel().getSelectedItem());

        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Method deletes the appointment selected by the user in the table.
     *
     * @param actionEvent
     */
    public void onClickDeleteAppointment(ActionEvent actionEvent) {
        //Retrieves the selected appointment from the appointment table
         Appointments selectedAppointment = (Appointments) appointmentTableview.getSelectionModel().getSelectedItem();
        //If no appointment is selected, the error message is displayed
        if (selectedAppointment == null) {
            Alert existingAppointment = new Alert(Alert.AlertType.ERROR);
            existingAppointment.setTitle("Error Message");
            existingAppointment.setContentText("No appointment was selected to delete. Please select an appointment to delete: ");
            existingAppointment.show();
        } else {
            //Confirmation alert to confirm customer record deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Appointment Removal");
            alert.setHeaderText("Delete");
            //alert.setContentText("Do you want to delete this appointment?\n\nID: " + selectedAppointment.getId() + "\nType: " + selectedAppointment.getType());
            alert.setContentText("Do you want to delete this appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //Deletes appointment
                DBAppointments.deleteAppointment(selectedAppointment);
                //Updates the tableview
                appointmentTableview.getItems().remove(selectedAppointment);
                // Show message after successful deletion
                Alert deletionAlert = new Alert(Alert.AlertType.INFORMATION);
                deletionAlert.setTitle("Deletion Successful");
                deletionAlert.setHeaderText(null);
                deletionAlert.setContentText("The following appointment was successfully deleted:\n\nAppointment ID: " + selectedAppointment.getId() + "\nType: " + selectedAppointment.getType());
                deletionAlert.showAndWait();
            }
            }
        }

    /**
     * On click of button, directs the user to the add customer records menu
     *
     * @param actionEvent
     * @throws IOException from FXMLLoader
     */
    public void onClickAddCustomerRecord(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/AddCustomerRecordsMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * On click of button, directs the user to the update customer records menu
     *
     * @param actionEvent
     * @throws IOException from FXMLLoader
     */

    public void onClickUpdateCustomerRecord(ActionEvent actionEvent) throws IOException {
        // Retrieves the selected customer record from the customer record table
        Customers customerSelected = (Customers) customerTableview.getSelectionModel().getSelectedItem();
        //If no customer is selected, an error message is displayed.
        if (customerSelected == null) {
            Alert existingCustomer = new Alert(Alert.AlertType.ERROR);
            existingCustomer.setTitle("Error Message");
            existingCustomer.setContentText("No customer was selected to update. Please select a customer record to update: ");
            existingCustomer.show();
        }
        //Loads UpdateCustomerRecordsMenu
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/c195project/UpdateCustomerRecordsMenu.fxml"));
        loader.load();

        //Loads the selected customer to the update customer records menu controller
        UpdateCustomerRecordsMenuController UpdateCustomerRecordsMenu = loader.getController();
        UpdateCustomerRecordsMenu.sendCustomer((Customers) customerTableview.getSelectionModel().getSelectedItem());

        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Method deletes the selected customer in the table if the customer has no associated appointments.
     *
     * @param actionEvent
     */
    public void onClickDeleteCustomerRecord(ActionEvent actionEvent) {
        // Retrieves the selected customer from the customer records table
        Customers selectedCustomer = (Customers) customerTableview.getSelectionModel().getSelectedItem();
        //If no customer record is selected, the error message is displayed
        if (selectedCustomer == null) {
            Alert customerRecordExist = new Alert(Alert.AlertType.ERROR);
            customerRecordExist.setTitle("Error Message");
            customerRecordExist.setContentText("No customer record was selected to delete. Please select a customer record to delete: ");
            customerRecordExist.show();
            return;
        }
            try {
                //If the customer has associated appointments, the error message is displayed.
                if (DBAppointments.associatedAppointments(selectedCustomer.getCustomerID())) {
                    Alert associatedAppointment = new Alert(Alert.AlertType.ERROR);
                    associatedAppointment.setTitle("Error Message");
                    associatedAppointment.setContentText("Customer has appointment(s) scheduled. Remove associated appointment(s) before you attempt to delete the customer.");
                    associatedAppointment.showAndWait();
                    return;
                }
                //Confirmation alert to confirm customer record deletion
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Customer Record Removal");
                alert.setHeaderText("Delete");
                alert.setContentText("Do you want to delete this customer record?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    //Deletes customer records if "OK" is selected in the confirmation screen.
                    boolean customerDeletion = DBCustomers.deleteCustomer(selectedCustomer);
                    if (customerDeletion) {
                        // Remove the customer from the table view
                        customerTableview.getItems().remove(selectedCustomer);
                        System.out.println("Customer record deleted successfully.");
                        Alert deletionAlert = new Alert(Alert.AlertType.INFORMATION);
                        deletionAlert.setTitle("Deletion Successful");
                        deletionAlert.setHeaderText(null);
                        deletionAlert.setContentText("The following customer was successfully deleted:\n\nCustomer ID: " + selectedCustomer.getCustomerID() + "\nCustomer Name: " + selectedCustomer.getCustomerName());
                        deletionAlert.showAndWait();
                    } else {
                        System.out.println("Failed to delete customer record.");
                    }
                }
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
        }

    /**
     * On click of button, directs the user to the reports menu controller
     *
     * @param actionEvent
     * @throws IOException from FXMLLoader
     */
    public void onClickReports(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/c195project/ReportsMenu.fxml"));
        Parent scene = loader.load();
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * If the user selects the radio button, the system displays appointments
     * within the current week and updates the table accordingly.
     *
     * @param actionEvent
     * @return A list of Appointments filtered for the current week.
     */
    public ObservableList<Appointments> onSelectCurrentWeekAppointments(ActionEvent actionEvent) {
        try {
            // Gets all appointments from the SQL database
            ObservableList<Appointments> allAppointments = DBAppointments.getAllAppointments();
            // Creates a list for the week
            ObservableList<Appointments> week = FXCollections.observableArrayList();

            // Define the current date and find the start of the current week (Sunday)
            LocalDate currentDate = LocalDate.now();
            LocalDate weekBeginning = currentDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
            //Allows for the filter to include Sunday
            LocalDate weekStart = weekBeginning.minusDays(1);

            // Define the end of the week (Saturday)
            LocalDate weekEnd = weekStart.plusDays(6);

            // Filters appointments to obtain appointments within the week
            if (allAppointments != null) {
                for (Appointments appointment : allAppointments) {
                    LocalDateTime appointmentEnd = appointment.getEnd();
                    LocalDate appointmentEndDate = appointmentEnd.toLocalDate();
                    if (appointmentEndDate.isEqual(currentDate) || (appointmentEndDate.isAfter(weekStart) && appointmentEndDate.isBefore(weekEnd))) {
                        week.add(appointment);
                    }
                }
                // Sets the appointment table view to the week list
                appointmentTableview.setItems(week);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        }

    /**
     * If the user selects the radio button, the system displays appointments
     * within the current month and updates the table accordingly.
     *
     * @param actionEvent
     * @return A list of Appointments filtered for the current month.
     */
    public ObservableList<Appointments> onSelectCurrentMonthAppointments(ActionEvent actionEvent) {
        try {
            //Gets all appointments from the SQL database
            ObservableList<Appointments> allAppointments = DBAppointments.getAllAppointments();
            //Creates a list for the month
            ObservableList<Appointments> month = FXCollections.observableArrayList();

            // Defines current date and the start and end of current month
            LocalDate currentDate = LocalDate.now();
            LocalDate monthStart = LocalDate.from(currentDate.withDayOfMonth(1).atStartOfDay());
            LocalDate monthEnd = LocalDate.from(currentDate.withDayOfMonth(currentDate.lengthOfMonth()).plusDays(1).atStartOfDay());

            // Filters appointments to obtain those within the month
            if (allAppointments != null) {
                for (Appointments appointment : allAppointments) {
                    LocalDateTime appointmentEnd = appointment.getEnd();
                    if (appointmentEnd.isAfter(monthStart.atStartOfDay()) && appointmentEnd.isBefore(monthEnd.atStartOfDay())) {
                        month.add(appointment);
                    }
                }
                // Sets the appointment tableview to the month list
                appointmentTableview.setItems(month);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * If the user selects the radio button, the system displays all appointments
     *
     * @param actionEvent
     * @return A list of all Appointments.
     */
    public ObservableList<Appointments> onSelectAllAppointments(ActionEvent actionEvent) {
        try {
            //Gets all appointments from the SQL database
            ObservableList<Appointments> allAppointments = DBAppointments.getAllAppointments();

            //Sets the appointment tableview to display all appointments
            if (allAppointments != null) {
                    appointmentTableview.setItems(allAppointments);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * On click of button, logs the user out of the system and directs user to the login menu.
     *
     * @param actionEvent
     * @throws IOException from FXMLLoader
     */
    public void onClickLogout(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/LoginMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}



