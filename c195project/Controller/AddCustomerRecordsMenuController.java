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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * AddCustomerRecordsMenuController class allows for the user to Add a Customer.
 *
 * @author Jamal Creary
 */
public class AddCustomerRecordsMenuController implements Initializable {
    public TextField addCustomerIDCustomerRecords;
    public TextField addCustomerNameCustomerRecords;
    public TextField addAddressCustomerRecords;
    public TextField addPostalCodeCustomerRecords;
    public TextField addPhoneNumberCustomerRecords;
    public ComboBox addCountryCustomerRecords;
    public ComboBox addDivisionCustomerRecords;

    /**
     * Initializes the Add Customer menu and populates combo boxes with items/objects.
     *
     * @param url            The location used to resolve relative paths for the root object or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Countries> countries = DBCountries.getAllCountries();
        //Sets countries in combo box
        addCountryCustomerRecords.setItems(countries);
        //Sets visible rows
        addCountryCustomerRecords.setVisibleRowCount(5);
        //Sets visible prompts
        addCountryCustomerRecords.setPromptText("Select a Country then a Division: ");

        addCountryCustomerRecords.setOnAction(event -> {
            // Retrieves the selectedcountry from the ComboBox
            Countries selectedCountry = (Countries) addCountryCustomerRecords.getValue();
            //Checks to see if a country is selected
            if (selectedCountry != null) {
                // Gets the countryID
                int countryId = selectedCountry.getCountryID();
                // Retrieve divisions based on the selected country ID
                ObservableList<Divisions> divisions = DBDivisions.getAllDivisionsUsingCountryID(countryId);
                // Set items for the addDivisionCustomerRecords ComboBox
                addDivisionCustomerRecords.setItems(divisions);
                addDivisionCustomerRecords.setVisibleRowCount(5);
                addDivisionCustomerRecords.setPromptText("Please select a division: ");
            }
        });
    }

    /**
     * Handles the save action function for when a user adds a new customer and
     * inserts the new customer into the database.
     *
     * @param actionEvent
     */
    public void onClickSaveAddCustomerRecords(ActionEvent actionEvent) {

        // Validates text fields
        if (addCustomerNameCustomerRecords.getText().trim().isEmpty() || addAddressCustomerRecords.getText().trim().isEmpty()
        || addPostalCodeCustomerRecords.getText().trim().isEmpty() || addPhoneNumberCustomerRecords.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please make sure that you provide a customer name, address, postal code, and phone number before you continue.");
            alert.showAndWait();
            return;
        }

        // Validates combo boxes
        if (addCountryCustomerRecords.getValue() == null || addDivisionCustomerRecords.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please make sure that you select a country then division before you continue.");
            alert.showAndWait();
            return;
        }

        //Creates new Customers object and set its properties
        Customers customer = new Customers();
        customer.setCustomerName(addCustomerNameCustomerRecords.getText());
        customer.setAddress(addAddressCustomerRecords.getText());
        customer.setPostalCode(addPostalCodeCustomerRecords.getText());
        customer.setPhone(addPhoneNumberCustomerRecords.getText());

        // Gets the selected division from the ComboBox
        Divisions selectedDivision = (Divisions) addDivisionCustomerRecords.getValue();
        if (selectedDivision != null) {
            customer.setDivisionID(selectedDivision.getDivisionID());
        }
        try {
            //Adds new customer to Database then directs the user to the Main Menu Controller.
            DBCustomers.insertCustomer(customer);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
            //Displays error if there is an issue when adding a new customer.
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("An error occurred during the process of adding the customer!");
            alert.showAndWait();
        }
    }

    /**
     * Cancels the process of adding a new customer and directs the user
     * back to the Main Menu controller.
     *
     * @param actionEvent
     */
    public void onClickCancelAddCustomerRecords(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}

