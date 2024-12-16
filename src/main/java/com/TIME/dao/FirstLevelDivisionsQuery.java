package com.TIME.dao;

import com.TIME.model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Retrieves data from the first level divisions table of the client schedule database. */
public abstract class FirstLevelDivisionsQuery {

    /** Retrieves all data from the first level divisions table.
     * @return List of all divisions. */
    public static ObservableList<FirstLevelDivision> selectAll() {
        ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
        String selectAllSql = "SELECT * FROM first_level_divisions";

        try {
            PreparedStatement selectAllPs = JDBConnection.getConnection().prepareStatement(selectAllSql);
            ResultSet resultSet = selectAllPs.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Division_ID");
                String division = resultSet.getString("Division");
                int countryId = resultSet.getInt("Country_ID");
                FirstLevelDivision DBDivision = new FirstLevelDivision(id, division, countryId);
                divisions.add(DBDivision);
            }
        } catch (SQLException e) {
            System.out.println("Error with the select all divisions method: " + e.getMessage());
        }

        return divisions;
    }

    /** Retrieves data from the first level divisions table by country id.
     * @param countryId ID of the country to target in the query.
     * @return List of divisions that match the countryId. */
    public static ObservableList<FirstLevelDivision> select(int countryId) {
        ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
        String selectSql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";

        try {
            PreparedStatement selectPs = JDBConnection.getConnection().prepareStatement(selectSql);
            selectPs.setInt(1, countryId);
            ResultSet resultSet = selectPs.executeQuery();

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String division = resultSet.getString("Division");
                int id = resultSet.getInt("Country_ID");
                FirstLevelDivision DBDivisions = new FirstLevelDivision(divisionId, division, id);
                divisions.add(DBDivisions);
            }
        } catch (SQLException e) {
            System.out.println("Error with the select division by country id method: " + e.getMessage());
        }
        return divisions;
    }

    /** Retrieves a division by its id.
     * @param divisionId Requested division id.
     * @return  Requested division. */
    public static FirstLevelDivision getDivision(int divisionId) {
        FirstLevelDivision division = null;
        String divisionSql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";

        try {
            PreparedStatement divisionPs = JDBConnection.getConnection().prepareStatement(divisionSql);
            divisionPs.setInt(1, divisionId);
            ResultSet resultSet = divisionPs.executeQuery();

            while (resultSet.next()) {
                int divId = resultSet.getInt("Division_ID");
                String divName = resultSet.getString("Division");
                int countryId = resultSet.getInt("Country_ID");
                division = new FirstLevelDivision(divId, divName, countryId);
            }
        } catch (SQLException e) {
            System.out.println("Error with the get division by id method: " + e.getMessage());
        }
        return division;
    }

}
