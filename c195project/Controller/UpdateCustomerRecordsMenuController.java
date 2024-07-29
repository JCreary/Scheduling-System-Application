package com.example.c195project.Controller;

import com.example.c195project.Model.DBCountries;
import com.example.c195project.Model.DBCustomers;
import com.example.c195project.Model.DBDivisions;
import com.example.c195project.Model.Countries;
import com.example.c195project.Model.Customers;
import com.example.c195project.Model.Divisions;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * UpdateCustomerRecordsMenuController class allows for the user to Update a Customer Record.
 *
 * @author Jamal Creary
 */
public class UpdateCustomerRecordsMenuController implements Initializable {

    public TextField updateCustomerIDCustomerRecords;
    public TextField updateCustomerNameCustomerRecords;
    public TextField updateAddressCustomerRecords;
    public TextField updatePostalCodeCustomerRecords;
    public TextField updatePhoneNumberCustomerRecords;
    public ComboBox<Countries> updateCountryCustomerRecords;
    public ComboBox<Divisions> updateDivisionCustomerRecords;
    public TextField updateDivisionIDCustomerRecords;

    /**
     * Initializes the Update Customer Records menu and populates combo boxes with items/objects including
     *
     * @param url            The location used to resolve relative paths for the root object or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Ensures the country combo box is not null
        if (updateCountryCustomerRecords != null) {
            ObservableList<Countries> countries = DBCountries.getAllCountries();
            // Sets countries in combo box
            updateCountryCustomerRecords.setItems(countries);
            // Sets visible rows
            updateCountryCustomerRecords.setVisibleRowCount(5);
            // Sets visible prompts
            updateDivisionCustomerRecords.setPromptText("Please select a division:");

            // Initialize country
            Countries initialCountry = updateCountryCustomerRecords.getItems().get(0);
            if (initialCountry != null) {
                int countryId = initialCountry.getCountryID();
                ObservableList<Divisions> divisions = DBDivisions.getAllDivisionsUsingCountryID(countryId);
                updateDivisionCustomerRecords.setItems(divisions);
                updateDivisionCustomerRecords.setVisibleRowCount(5);
                updateDivisionCustomerRecords.setPromptText("Please select a division:");
            }

            // Listener to load divisions when a country is selected
            updateCountryCustomerRecords.valueProperty().addListener((observable, oldValue, newValue) -> {
                Countries selectedCountry = newValue;
                updateDivisionCustomerRecords.setValue(null);
                updateDivisionCustomerRecords.setPromptText("Please select a division:");
                if (selectedCountry != null) {
                    // Clear division combo box before loading divisions for the new country
                    updateDivisionCustomerRecords.getItems().clear();
                    updateDivisionCustomerRecords.setValue(null);
                    updateDivisionCustomerRecords.setPromptText("Please select a division:");

                    int countryId = selectedCountry.getCountryID();
                    ObservableList<Divisions> divisions = DBDivisions.getAllDivisionsUsingCountryID(countryId);
                    updateDivisionCustomerRecords.setItems(divisions);
                    updateDivisionCustomerRecords.setVisibleRowCount(5);
                } else {
                    // If no country is selected, clear the division combo box and set prompt text
                    updateDivisionCustomerRecords.getItems().clear();
                    updateDivisionCustomerRecords.setValue(null);
                    updateDivisionCustomerRecords.setPromptText("Please select a Country first");
                }
            });
        }
    }

    /**
     * Handles the save action for updating customer records.
     *
     * @param actionEvent
     */
    public void onClickSaveUpdateCustomerRecords(ActionEvent actionEvent) {
        // Validates text fields
        if (updateCustomerNameCustomerRecords.getText().trim().isEmpty() || updateAddressCustomerRecords.getText().trim().isEmpty()
                || updatePostalCodeCustomerRecords.getText().trim().isEmpty() || updatePhoneNumberCustomerRecords.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please make sure that you provide a customer name, address, postal code, and phone number before you continue.");
            alert.showAndWait();
            return;
        }

        // Validates combo boxes
        if (updateCountryCustomerRecords.getValue() == null || updateDivisionCustomerRecords.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please make sure that the appropriate country is selected then select a division before you continue.");
            alert.showAndWait();
            return;
        }


        try {
            // Retrieves input fields
            int id = Integer.parseInt(updateCustomerIDCustomerRecords.getText());
            String name = updateCustomerNameCustomerRecords.getText();
            String address = updateAddressCustomerRecords.getText();
            String postalCode = updatePostalCodeCustomerRecords.getText();
            String phone = updatePhoneNumberCustomerRecords.getText();
            int divisionID = Integer.parseInt(updateDivisionIDCustomerRecords.getText());
            Countries country = updateCountryCustomerRecords.getValue();
            Divisions division = updateDivisionCustomerRecords.getValue();

            // Creates a new Customers object with the updated data
            Customers updatedCustomer = new Customers(id, name, address, postalCode, phone, division.getDivisionID(), country.getCountry(), division.getDivision());
            //Update customer records then directs user to the main menu
            DBCustomers.updateCustomer(updatedCustomer);
            System.out.println("Customer records updated successfully.");
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * On click, customer is directed to main menu.
     *
     * @param actionEvent
     * @throws IOException from FXMLLoader
     */
    public void onClickCancelUpdateCustomerRecords(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * Obtains customer information from the main menu screen and updates fields
     *
     * @param updatedCustomerRecord
     */
    public void sendCustomer(Customers updatedCustomerRecord) {
        // Populates update fields with customer details
        updateCustomerIDCustomerRecords.setText(String.valueOf(updatedCustomerRecord.getCustomerID()));
        updateCustomerNameCustomerRecords.setText(updatedCustomerRecord.getCustomerName());
        updateAddressCustomerRecords.setText(updatedCustomerRecord.getAddress());
        updatePostalCodeCustomerRecords.setText(updatedCustomerRecord.getPostalCode());
        updatePhoneNumberCustomerRecords.setText(updatedCustomerRecord.getPhone());
        updateDivisionIDCustomerRecords.setText(String.valueOf(updatedCustomerRecord.getDivisionID()));

        // Set selected country
        ObservableList<Countries> countries = DBCountries.getAllCountries();
        String selectedCountry = updatedCustomerRecord.getCountry();
        for (Countries country : countries) {
            if (country.getCountry().equals(selectedCountry)) {
                updateCountryCustomerRecords.setValue(country);
            }
        }
        // Set selected division based on country selected by the user
        if (selectedCountry != null) {
            ObservableList<Divisions> divisions = DBDivisions.getAllDivisions();
            for (Divisions division : divisions) {
                if (division.getDivision().equals(updatedCustomerRecord.getDivision())) {
                    updateDivisionCustomerRecords.setValue(division);
                }
            }
        }
    }
}
