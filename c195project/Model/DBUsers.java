package com.example.c195project.Model;

import com.example.c195project.Helper.DBConnection;
import com.example.c195project.Model.Customers;
import com.example.c195project.Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class created for handling database operations related to users.
 *
 * @author Jamal Creary
 */
public class DBUsers {
    /**
     * Retrieves all users from the database
     *
     * @return the users
     * @throws SQLException if SQL exception exists
     */
    public static ObservableList<Users> getAllUsers() throws SQLException {
        //Creates Observable list called users
        ObservableList<Users> users = FXCollections.observableArrayList();
        //Makes the SQL query using to obtain users
        String sql = "SELECT * FROM CLIENT_SCHEDULE.USERS";
        //Creates the DB connection
        PreparedStatement ps2 = DBConnection.connection.prepareStatement(sql);
        //Gets the result set after it creates the prepared statement
        ResultSet rs2 = ps2.executeQuery();
        try {
            //Loops through the result set using the rs next method
            while (rs2.next()) {
                int userID = rs2.getInt("User_ID");
                String userName = rs2.getString("User_Name");
                String password = rs2.getString("Password");
                //Creates new object with the following items below
                Users user = new Users(userID, userName, password);
                //Adds objects to list
                users.add(user);
            }
        } catch (Exception ee){
            ee.printStackTrace();
        }
        //Returns users
        return users;
    }

}
