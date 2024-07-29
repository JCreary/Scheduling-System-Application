package com.example.c195project.Model;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * This class represents users in the scheduling system.
 * This class contains information such as userID, userName, and password.
 *
 * @author Jamal Creary
 */

public class Users {
    private int userID;
    private String userName;
    private String password;

    /**
     * Constructors with all parameters
     */

    public Users(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
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
     * @return the userName.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns data in the combo box
     *
     * @return the userID
     */

    public String toString() {
        return Integer.toString(userID);
    }
}