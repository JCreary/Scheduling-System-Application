package com.example.c195project.Model;


import com.example.c195project.Helper.DBConnection;
import com.example.c195project.Model.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
/**
 * DAO class created for handling database operations related to countries.
 *
 * @author Jamal Creary
 */
public class DBCountries {
    /**
     * Retrieves all countries from the database.
     *
     * @return an array list of contacts containing all contacts retrieved from the database
     */
    public static ObservableList<Countries> getAllCountries() {
        //Creates list called countryList
        ObservableList<Countries> countryList = FXCollections.observableArrayList();

        try {
            //Makes the query
            String sql = "SELECT * from countries";
            //Creates the DB connection
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            //Gets the result set after it creates the prepared statement with the sql in it on an execute query
            ResultSet rs = ps.executeQuery();
            //Loops through the result set using the rs next method
            while (rs.next()) {
                //The country ID is received from the loop
                int countryId = rs.getInt("Country_ID");
                //The country Name is received from the loop
                String country = rs.getString("Country");
                //Creates object with the country ID and country Name
                Countries c = new Countries(countryId, country);
                // Adds the country ID and country Name to the list
                countryList.add(c);
            }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //Returns the created list
            return countryList;
        }
}
