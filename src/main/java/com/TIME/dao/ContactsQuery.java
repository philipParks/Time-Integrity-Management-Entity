package com.TIME.dao;

import com.TIME.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Retrieves data from the contacts table of the client schedule database. */
public abstract class ContactsQuery {

    /** Retrieves all data from the contacts table.
     * @return List of all contacts. */
    public static ObservableList<Contact> selectAll() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        try {
            String selectAllSql = "SELECT * FROM contacts";
            PreparedStatement selectAllPs = JDBConnection.getConnection().prepareStatement(selectAllSql);
            ResultSet resultSet = selectAllPs.executeQuery();

            while (resultSet.next()) {
                int contactId = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                String email = resultSet.getString("Email");
                Contact DBContact = new Contact(contactId, contactName, email);
                contacts.add(DBContact);
            }
        } catch (SQLException e) {
            System.out.println("Error with the select all contacts method: " + e.getMessage());
        }

        return contacts;
    }

    /** Retrieves a contact by its ID.
     * @param contactId Requested contact ID.
     * @return Requested contact. */
    public static Contact select(int contactId) {
        Contact contact = null;
        String selectSql = "SELECT * FROM contacts WHERE Contact_ID = ?";

        try {
            PreparedStatement   selectPs = JDBConnection.getConnection().prepareStatement(selectSql);
            selectPs.setInt(1, contactId);
            ResultSet resultSet = selectPs.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                String email = resultSet.getString("Email");
                contact = new Contact(id, contactName, email);
            }
        } catch (SQLException e) {
            System.out.println("Error with the select contact by id method: " + e.getMessage());
        }
        return contact;
    }
}
