package com.example.c195project.Model;

import com.example.c195project.Helper.DBConnection;
import com.example.c195project.Model.Countries;
import com.example.c195project.Model.Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * DAO class created for handling database operations related to divisions.
 *
 * @author Jamal Creary
 */
public class DBDivisions {
    /**
     * Retrieves divisions from the database using the country ID.
     *
     * @param countryID
     * @return the divisions
     */
    public static ObservableList<Divisions> getAllDivisionsUsingCountryID(int countryID) {
        //Creates Observable list called divisionList
        ObservableList<Divisions> divisionList = FXCollections.observableArrayList();
        try {
            //Makes the SQL query using inner join to obtain the division using the country ID
            String sql = "SELECT Division_ID, Division \n" +
                    "FROM client_schedule.first_level_divisions\n" +
                    "INNER JOIN client_schedule.countries ON countries.Country_ID = first_level_divisions.Country_ID\n" +
                    "WHERE countries.Country_ID = ?";
            //Creates the DB connection
            PreparedStatement ps2 = DBConnection.getConnection().prepareStatement(sql);
            ps2.setInt(1, countryID);
            //Gets the result set after it creates the prepared statement with the sql in it on an execute query
            ResultSet rs2 = ps2.executeQuery();
            //Loops through the result set using the rs next method
            while (rs2.next()) {

                int divisionId = rs2.getInt("Division_ID");
                //The division is received from the loop
                String division = rs2.getString("Division");
                //Creates object with the division ID, division, and countryID
                Divisions D = new Divisions(divisionId, division, countryID);
                // Adds object to list
                divisionList.add(D);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Returns the created list
        return divisionList;
    }

    /**
     * Retrieves all divisions from the database.
     *
     * @return the divisions
     */
    public static ObservableList<Divisions> getAllDivisions(){
        //Creates Observable list called divisionList
        ObservableList<Divisions> divisionList = FXCollections.observableArrayList();
        try {
            //Makes the query
            String sql = "SELECT * from first_level_divisions";
            //Creates the DB connection
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            //Gets the result set after it creates the prepared statement with the sql in it on an execute query
            ResultSet rs = ps.executeQuery();
            //Loops through the result set using the rs next method
            while (rs.next()) {
                //The division ID is received from the loop
                int divisionId = rs.getInt("Division_ID");
                //The division received from the loop
                String division = rs.getString("Division");
                //The country ID is received from the loop
                int countryId = rs.getInt("Country_ID");
                //creates object with the division ID, division, and country ID.
                Divisions d = new Divisions(divisionId, division, countryId);
                // Adds object to list
                divisionList.add(d);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Returns the created list
        return divisionList;
    }
}
