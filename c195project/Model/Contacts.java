package com.example.c195project.Model;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * This class represents contacts in the scheduling system.
 * This class contains information such as contactID, contactName, and email.
 *
 * @author Jamal Creary
 */
public class Contacts {

    private int contactID;
    private String contactName;
    private String email;

    /**
     * Constructors with all parameters
     */
    public Contacts(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
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
     * @return the contactName.
     */

    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName the contactName to set.
     */

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return the email.
     */

    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set.
     */

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns data in the combo box
     *
     * @return the contactID.
     */

    public String toString() {
        return Integer.toString(contactID);
    }
}
