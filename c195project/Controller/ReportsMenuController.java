package com.example.c195project.Controller;

import com.example.c195project.Model.DBAppointments;
import com.example.c195project.Model.DBContacts;
import com.example.c195project.Model.Appointments;
import com.example.c195project.Model.Customers;

import com.example.c195project.Model.DBCustomers;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * ReportsMenuController class allows for the user to view/manage reports.
 *
 * @author Jamal Creary
 */

public class ReportsMenuController implements Initializable {

    public TableColumn contactIDReport1Menu;
    public TableColumn contactNameReport1Menu;
    public TableColumn appointmentIDReport1Menu;
    public TableColumn typeReport1Menu;
    public TableColumn descriptionReport1Menu;
    public TableColumn startDateAndTimeReport1Menu;
    public TableColumn endDateAndTimeReport1Menu;
    public TableColumn customerIDReport1Menu;
    @FXML
    private TableView<Customers> report3Tableview;
    @FXML
    private ComboBox<Integer> selectContactIDReportMenu;
    @FXML
    private TableView<Appointments> report1Tableview;
    @FXML
    private TextField searchTypeMonthReportMenu;
    @FXML
    private TextField totalCustomerAppointmentText;
    @FXML
    public TableColumn<Integer, Customers> customerIDReport3Menu;
    @FXML
    public TableColumn<String, Customers> customerNameReport3Menu;
    //Created list to store deleted customers
    private List<Customers> deletedCustomers = new ArrayList<>();

    /**
     * This method initializes the reports menu controller.
     *
     * @param url            The location used to resolve relative paths for the root object or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize TableView with correct type
        customerIDReport3Menu.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameReport3Menu.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        // Get the list of deleted customers from DBCustomers class
        ObservableList<Customers> deletedCustomers = DBCustomers.getDeletedCustomers();

        // Set the items of report3TableView to the list of deleted customers
        report3Tableview.setItems(deletedCustomers);

        try {
            // Populate the ComboBox with contact IDs
            ObservableList<Integer> contactIDs = DBContacts.getAllContactID();
            selectContactIDReportMenu.setItems(contactIDs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add event listener for the searchTypeMonthReportMenu TextField
        searchTypeMonthReportMenu.setOnAction(this::SearchTypeMonth);
    }

    /**
     * Method to add deleted customers to the list.
     *
     * @param customer The list of deleted customers which is added to the Reports table.
     */
    public void addDeletedCustomer(Customers customer) {
        deletedCustomers.add(customer);
        // Refresh the table view to display the updated list of deleted customers
        DeletedCustomersTableView();
        // Get the list of deleted customers from DBCustomers class
        ObservableList<Customers> deletedCustomers = DBCustomers.getDeletedCustomers();
        // Set the items of report3TableView to the list of deleted customers
        report3Tableview.setItems(deletedCustomers);
    }

    /**
     * Method to refresh the table view with deleted customers
     */
    private void DeletedCustomersTableView() {
        if (report3Tableview != null) {
            // Clear existing items in the table view
            report3Tableview.getItems().clear();
            // Populates the table view with the list of deleted customers
            report3Tableview.getItems().addAll(deletedCustomers);
        }
    }
    /**
     * Upon click, the customer is directed to the Main Menu Controller
     *
     * @param actionEvent
     * @throws IOException from FXMLLoader
     */
    public void onClickCancelReport(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Handles the search based on the text entered by the user and
     * counts/displays the number of appointments related to the entered type or month.
     *
     * @param actionEvent
     */
    private void SearchTypeMonth(ActionEvent actionEvent) {
        // Get the value entered in the searchTypeMonthReportMenu TextField
        String searchText = searchTypeMonthReportMenu.getText();

        // Count the number of appointments related to the entered type or month
        int appointmentCount = countAppointments(searchText);

        // Display the count in the totalCustomerAppointmentText TextField
        totalCustomerAppointmentText.setText(String.valueOf(appointmentCount));
    }

    /**
     * Counts the appointments based on the given search text.
     * If the search text represents a month, counts appointments by month.
     * Otherwise, assumes it's a type and counts appointments by type.
     *
     * @param search
     * @return the number of appointments based on the search text
     */
    private int countAppointments(String search) {
        try {
            // Check if the searchText is a month
            Month month = getMonthFromString(search);
            // If it's a valid month, count appointments by month
            if (month != null) {
                return DBAppointments.countAppointmentsByMonth(month);
            } else {
                return DBAppointments.countAppointmentsByType(search);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Returns 0 if no data found
            return 0;
        }
    }

    /**
     * Converts string representation of a month
     *
     * @param searchText
     * @return the Month value if the string represents a valid month, otherwise returns null
     */
    private Month getMonthFromString(String searchText) {
        try {
            return Month.valueOf(searchText.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Handler for the searchTypeMonthReportMenu TextField.
     *
     * @param actionEvent
     */
    public void onActionSearchTypeMonthReportMenu(ActionEvent actionEvent) {
    }
    /**
     * Retrieves appointment details for the selected contact ID and populates the table.
     *
     * @param actionEvent
     */
    @FXML
    public void onContactIDSelected(ActionEvent actionEvent) {
        // Retrieve the selected contact ID
        Integer selectedContactID = selectContactIDReportMenu.getValue();

        if (selectedContactID != null) {
            try {
                // Obtains appointment details for the selected contact ID
                ObservableList<Appointments> appointments = DBAppointments.getAppointmentsByContactID(selectedContactID);

                // Populate the report1Tableview with the fetched appointment details
                report1Tableview.setItems(appointments);

                // Set cell value factories for the table columns
                contactIDReport1Menu.setCellValueFactory(new PropertyValueFactory<>("contactID"));
                contactNameReport1Menu.setCellValueFactory(new PropertyValueFactory<>("contactName"));
                appointmentIDReport1Menu.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                typeReport1Menu.setCellValueFactory(new PropertyValueFactory<>("type"));
                descriptionReport1Menu.setCellValueFactory(new PropertyValueFactory<>("description"));
                startDateAndTimeReport1Menu.setCellValueFactory(new PropertyValueFactory<>("start"));
                endDateAndTimeReport1Menu.setCellValueFactory(new PropertyValueFactory<>("end"));
                customerIDReport1Menu.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
