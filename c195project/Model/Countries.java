package com.example.c195project.Model;

/**
 * This class represents countries in the scheduling system.
 * This class contains information such as countryID and country.
 *
 * @author Jamal Creary
 */

public class Countries {
    private int countryID;
    private String country;

    /**
     * Constructors with all parameters
     */
    public Countries(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }

    /**
     * @return the countryID.
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * @param countryID the countryID to set.
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * @return the country.
     */

    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set.
     */

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns data in the combo box
     *
     * @return the countryID + country.
     */
    public String toString(){
        return String.valueOf(countryID + ". " + country);
    }

}
