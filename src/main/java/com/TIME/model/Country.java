package com.TIME.model;

import com.TIME.dao.CountriesQuery;
import javafx.collections.ObservableList;

/** Defines a countries object. */
public class Country {

    private final int countryId;
    private final String country;
    private static final ObservableList<Country> countries = CountriesQuery.selectAll();

    /** Overrides the toString method to only return the country name.
     * @return just the country name from the country object. */
    @Override
    public String toString() {
        return country;
    }

    /** Constructs an instance of countries.
     * @param id Automatically assigned unique value.
     * @param country Name of the country. */
    public Country(int id, String country) {
        this.countryId = id;
        this.country = country;
    }

    /** Gets the list of countries.
     * @return A list of countries. */
    public static ObservableList<Country> getCountries() {
        return countries;
    }

    /** Gets the country id.
     * @return Automatically assigned unique value. */
    public int getCountryId() {
        return countryId;
    }

    /** Gets the name of the country.
     * @return Name of the country. */
    public String getCountry() {
        return country;
    }

}
