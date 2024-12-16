package com.TIME.model;

import com.TIME.dao.FirstLevelDivisionsQuery;
import javafx.collections.ObservableList;

/** Defines a first level division object. */
public class FirstLevelDivision {

    private final int divisionId;
    private final String division;
    private final int countryId;
    private static final ObservableList<FirstLevelDivision> divisions = FirstLevelDivisionsQuery.selectAll();

    /** Overrides the toString() method to only return the name of the division.
     * @return Only the name of the first level division. */
    @Override
    public String toString() {
        return division;
    }

    /** Constructs a first level division object.
     * @param id Automatically assigned unique value.
     * @param division State/province.
     * @param countryId Country id for divisions.*/
    public FirstLevelDivision(int id, String division, int countryId) {
        this.divisionId = id;
        this.division = division;
        this.countryId = countryId;
    }

    /** Gets a list of all the first level divisions.
     * @return A list of first level divisions. */
    public static ObservableList<FirstLevelDivision> getDivisions() {
        return divisions;
    }

    /** Gets division id.
     * @return Automatically assigned unique value. */
    public int getDivisionId() {
        return divisionId;
    }

    /** Gets division.
     * @return Division. */
    public String getDivision() {
        return division;
    }

    /** Gets division country id.
     * @return Division country id. */
    public int getCountryId() {
        return countryId;
    }

}
