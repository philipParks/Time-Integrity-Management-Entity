package com.TIME.dao;

import com.TIME.model.Customer;
import com.TIME.model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/** Retrieves data from the customers table of the client schedule database. */
public abstract class CustomerQuery {

    /** Retrieves all requested data from the customers table.
     * @return A list of all customers. */
    public static ObservableList<Customer> selectAll() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        try {
            String selectAllSql = "SELECT * FROM customers";
            PreparedStatement selectAllPs = JDBConnection.getConnection().prepareStatement(selectAllSql);
            ResultSet resultSet = selectAllPs.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Customer_ID");
                String name = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                int divisionId = resultSet.getInt("Division_ID");
                FirstLevelDivision division = FirstLevelDivisionsQuery.getDivision(divisionId);
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");

                Customer customer = new Customer(id, name, address, division, postalCode, phone);
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Error with the select all customers method: " + e.getMessage());
        }

        return customers;
    }

    /** Retrieves a Customer with a specific ID. */
    public static Customer select(int customerId) {
        Customer customer = null;
        String selectSql = "SELECT * FROM customers WHERE Customer_ID = ?";

        try {
            PreparedStatement selectPs = JDBConnection.getConnection().prepareStatement(selectSql);
            selectPs.setInt(1, customerId);
            ResultSet resultSet = selectPs.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Customer_ID");
                String name = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                int division = resultSet.getInt("Division_ID");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                FirstLevelDivision fld = FirstLevelDivisionsQuery.getDivision(division);
                customer = new Customer(id, name, address, fld, postalCode, phone);
            }

        } catch (SQLException e) {
            System.out.println("Error with the select by customer ID method: " + e.getMessage());
        }
        return customer;
    }

    /** Adds a customer to the customers table.
     * @param name The customer name.
     * @param address The customer address.
     * @param postalCode The customer postal code.
     * @param phone The customer phone.
     * @param divisionId The customer division id. */
    public static void insert(String name, String address, String postalCode, String phone, int divisionId) {
        String insertSql = "INSERT INTO customers VALUES (NULL, ?, ?, ?, ?, NULL, NULL, NULL, NULL, ?)";
        try {
            PreparedStatement insertPs = JDBConnection.getConnection().prepareStatement(insertSql);
            insertPs.setString(1, name);
            insertPs.setString(2, address);
            insertPs.setString(3, postalCode);
            insertPs.setString(4, phone);
            insertPs.setInt(5, divisionId);
            insertPs.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with the customer insert method: " + e.getMessage());
        }
    }

    /** Updates a customer record.
     * @param customerId The ID of the customer to update.
     * @param name       The name of the customer.
     * @param address The address of the customer.
     * @param postalCode The postal code of the customer.
     * @param phone The customers phone number.
     * @param divisionId The division id of the customer.*/
    public static void update(int customerId, String name, String address, String postalCode, String phone, int divisionId) {
        String updateSql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        try {
            PreparedStatement updatePs = JDBConnection.getConnection().prepareStatement(updateSql);
            updatePs.setString(1, name);
            updatePs.setString(2, address);
            updatePs.setString(3, postalCode);
            updatePs.setString(4, phone);
            updatePs.setInt(5, divisionId);
            updatePs.setInt(6, customerId);
            updatePs.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with the customer update method: " + e.getMessage());
        }
    }

    /** Removes a customer from the customers table.
     * @param customerId The ID of the customer to delete. */
    public static void delete(int customerId) {

        try {
            String deleteSql = "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement deletePs = JDBConnection.getConnection().prepareStatement(deleteSql);
            deletePs.setInt(1, customerId);
            deletePs.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with the customer delete method: " + e.getMessage());
        }

    }

}
