package com.example.c195project.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.*;
import java.util.*;

/**
 * This class represents an appointment in the scheduling system.
 * This class contains information such as appointmentID, title, description, location,
 * type, start, end, customerID, userID, contactID, and contactName.
 *
 * @author Jamal Creary
 */

public class Appointments {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int userID;
    private int contactID;
    private String contactName;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public static void setAllAppointments(ObservableList<Appointments> allAppointments) {
        Appointments.allAppointments = allAppointments;
    }

    /**
     * Constructors with all parameters
     */
    public Appointments(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * Constructors without all parameters/variables identified above
     */
    public Appointments(int contactID, String contactName, int appointmentID, String type, String description, LocalDateTime start, LocalDateTime end, int customerID) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.appointmentID = appointmentID;
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
    }

    /**
     * Constructs an empty Appointment
     */
    public Appointments() {
    }

    /**
     * @return the appointmentID.
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * @param appointmentID the appointmentID to set.
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the location.
     */

    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set.
     */

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the type.
     */

    public String getType() {
        return type;
    }

    /**
     * @param type the type to set.
     */

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the start.
     */

    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @param start the start to set.
     */

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * @return the end.
     */

    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @param end the end to set.
     */

    public void setEnd(LocalDateTime end) {
        this.end = end;
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
     * @return the userID.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return the contactID.
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID the contactID to set.
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * The list of all appointments.
     */
    private static ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();

    /**
     * Retrieves all appointments
     *
     * @return Returns Observable List of all appointments
     */

    public static ObservableList<Appointments> getAllAppointments() {
        return allAppointments;
    }

    /**
     * Returns data in the combo box
     *
     * @return the appointment type.
     */
    public String toString() {
        return String.valueOf(type);
    }

    /**
     * Retrieves the ID of the appointment.
     *
     * @return the appointmentID.
     */

    public int getId() {
        return appointmentID;
    }
}
