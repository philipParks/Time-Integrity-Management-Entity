package com.TIME.dao;

import com.TIME.model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Retrieves data from the countries table of the client schedule database. */
public abstract class CountriesQuery {

    /** Retrieves all data from the countries table.
     * @return List of all countries. */
    public static ObservableList<Country> selectAll() {
        String selectAllSql = "SELECT * FROM countries";
        ObservableList<Country> countries = FXCollections.observableArrayList();

        try {
            PreparedStatement selectAllPs = JDBConnection.getConnection().prepareStatement(selectAllSql);
            ResultSet resultSet = selectAllPs.executeQuery();

            while (resultSet.next()) {
                int countryId = resultSet.getInt("Country_ID");
                String country = resultSet.getString("Country");
                Country DBCountry = new Country(countryId, country);
                countries.add(DBCountry);
            }
        } catch (SQLException e) {
            System.out.println("Error with the select all countries method: " + e.getMessage());
        }

        return countries;
    }

    /** Retrieves a country that matches a country ID.
     * @param countryId Requested country ID.
     * @return Requested country. */
    public static Country select(int countryId) {
        String selectSql = "SELECT * FROM countries WHERE Country_ID = ?";
        Country country = null;

        try {
            PreparedStatement selectCountry = JDBConnection.getConnection().prepareStatement(selectSql);
            selectCountry.setInt(1, countryId);
            ResultSet resultSet = selectCountry.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Country_ID");
                String selectedCountry = resultSet.getString("Country");
                country = new Country(id, selectedCountry);
            }
        } catch (SQLException e) {
            System.out.println("Error with the select country by id method: " + e.getMessage());
        }
        return country;
    }

}
