package com.example.c195project.Model;

import com.example.c195project.Controller.ReportsMenuController;
import com.example.c195project.Helper.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * This class represents a customer in the scheduling system.
 * This class contains information such as customerID, customerName, address, postalCode, phone, divisionID,
 * country, and division.
 *
 * @author Jamal Creary
 */
public class Customers {

    /**
     * The list of associated appointments.
     */
    private ObservableList<Appointments> associatedAppointments = FXCollections.observableArrayList();
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionID;
    private String country;
    private String division;

    /**
     * @return the country.
     */

    public String getCountry() {return country;}

    /**
     * @param country the country to set.
     */

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the division.
     */

    public String getDivision() {
        return division;
    }

    /**
     * @param division the division to set.
     */

    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Constructors with all parameters
     */

    public Customers(int customerID, String customerName, String address, String postalCode, String phone, int divisionID, String country, String division) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
        this.country = country;
        this.division = division;
    }

    /**
     * Constructs an empty Customers
     */
    public Customers() {
    }

    /**
     * @return the customerID.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID the customerID to set.
     */

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return the customerName.
     */

    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the address.
     */

    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set.
     */

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the postalCode.
     */

    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the phone.
     */

    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the divisionID.
     */

    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @param divisionID the divisionID to set.
     */

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * The list of all customers.
     */
    private static ObservableList<Customers> allCustomers = FXCollections.observableArrayList();

    /**
     * Observable List to get all customers.
     *
     * @return Returns all customers.
     */

    public static ObservableList<Customers> getAllCustomers() {
        return allCustomers;
    }

    /**
     *
     * Observable List to get all associated appointments
     *
     * @return A list of associated appointments.
     */

    public ObservableList<Appointments> getAllAssociatedAppointments() {
        return associatedAppointments;
    }

    /**
     * Returns data in the combo box
     *
     * @return the customerID.
     */

    public String toString() {
        return (Integer.toString(customerID));
    }

    /**
     * @return the countryID.
     */
    public int getCountryID() {
        return getCountryID();
    }
}