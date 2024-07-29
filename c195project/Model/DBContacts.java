package com.example.c195project.Model;

import com.example.c195project.Helper.DBConnection;
import com.example.c195project.Model.Contacts;
import com.example.c195project.Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class created for handling database operations related to contacts.
 *
 * @author Jamal Creary
 */
public class DBContacts {

    /**
     * Retrieves all contacts from the database.
     *
     * @return an array list of contacts containing all contacts retrieved from the database
     * @throws SQLException if SQL exception occurs
     */
            public static ObservableList<Contacts> getAllContacts() throws SQLException {
                //Creates contacts list
                ObservableList<Contacts> contacts = FXCollections.observableArrayList();
                // SQL query to select all contacts
                String sql = "SELECT * FROM CLIENT_SCHEDULE.CONTACTS";
                // Prepare the SQL statement
                PreparedStatement ps2 = DBConnection.connection.prepareStatement(sql);
                //Result set is the 2-dimensional list used to execute and obtain the result set
                ResultSet rs2 = ps2.executeQuery();
                try {
                    //Loops through the result set using the rs next method
                    while (rs2.next()) {
                        // Retrieve contact information from the result set
                        int contactID = rs2.getInt("Contact_ID");
                        String contactName = rs2.getString("Contact_Name");
                        String email = rs2.getString("Email");
                        // Create a Contact object and add it to the list
                        Contacts contact = new Contacts(contactID, contactName, email);
                        contacts.add(contact);
                    }
                } catch (Exception ee){
                    ee.printStackTrace();
                }
                //Returns list of contacts
                return contacts;
            }
    /**
     * Retrieves all contact IDs from the Contacts database.
     *
     * @return a list of all contact IDs retrieved from the contacts table in the database
     * @throws SQLException if SQL exception occurs
     */
    public static ObservableList<Integer> getAllContactID() throws SQLException {
        //Creates contacts list
        ObservableList<Integer> contactIDs = FXCollections.observableArrayList();
        // SQL query to select all contacts
        String sql = "SELECT Contact_ID FROM CLIENT_SCHEDULE.CONTACTS";
        // Prepare the SQL statement
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        //Result set is the 2-dimensional list used to execute and obtain the result set
        ResultSet rs = ps.executeQuery();
        //While exists
        while (rs.next()) {
            int contactID = rs.getInt("Contact_ID");
            contactIDs.add(contactID);
        }
        //Returns list of contactIDs
        return contactIDs;
    }
}


