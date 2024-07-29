package com.example.c195project.Model;

/**
 * This class represents divisions in the scheduling system.
 * This class contains information such as divisionID, division, and countryID.
 *
 * @author Jamal Creary
 */

public class Divisions {
    private int divisionID;
    private String division;
    private int countryID;

    /**
     * Constructors with all parameters
     */
    public Divisions(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
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
     * Returns data in the combo box
     *
     * @return the division.
     */
    public String toString(){
        return (this.division);
    }

    /**
     * @return the division.
     */

    public String getName() {
        return division;
    }
}
