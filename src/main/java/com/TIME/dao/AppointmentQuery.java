package com.TIME.dao;

import com.TIME.helper.DateTimeInterface;
import com.TIME.model.Appointment;
import com.TIME.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.TimeZone;

/** Retrieves data from the appointments table of the client schedule database. */
public abstract class AppointmentQuery {

    /** Lambda 1: Converts a time from EST to local time. */
    static DateTimeInterface estToLocal = (localDate, localTime) -> {
        ZoneId myZone = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime currentZDT = ZonedDateTime.of(localDate, localTime, myZone);
        return currentZDT.toLocalDateTime();
    };

    /** Retrieves a list of all appointments.
     * <p><b>
     *     Lambda 1: Converts a time from EST to local time.
     * </b></p>
     * @return List of all appointments. */
    public static ObservableList<Appointment> selectAll() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            String selectSql = "SELECT * FROM appointments";
            PreparedStatement selectPs = JDBConnection.getConnection().prepareStatement(selectSql);
            ResultSet resultSet = selectPs.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customer = resultSet.getInt("Customer_ID");
                int contactId = resultSet.getInt("Contact_ID");
                int user = resultSet.getInt("User_ID");

                Contact contact = ContactsQuery.select(contactId);

                LocalDate date = start.toLocalDate();
                LocalTime utcStartTime = start.toLocalTime();
                LocalTime utcEndTime = end.toLocalTime();

                LocalDateTime localStart = estToLocal.convertToDateTime(date, utcStartTime);
                LocalDateTime localEnd = estToLocal.convertToDateTime(date, utcEndTime);

                Appointment DBAppointment = new Appointment(id, title, description, location, type, localStart, localEnd, customer, contact, user);
                appointments.add(DBAppointment);
            }

        } catch (SQLException e) {
            System.out.println("Error with the select all appointments method: " + e.getMessage());
        }

        return appointments;
    }

    /** Selects an appointment by its id.
     * <p><b>
     *     Lambda 1: Converts a time from EST to local time.
     * </b></p>
     * @param appointmentId Requested appointment id.
     * @return Requested appointment. */
    public static Appointment select(int appointmentId) {
        Appointment appointment = null;

        try {
            String selectSql = "SELECT * FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement selectPs = JDBConnection.getConnection().prepareStatement(selectSql);
            selectPs.setInt(1, appointmentId);
            ResultSet resultSet = selectPs.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customerId = resultSet.getInt("Customer_ID");
                int contactId = resultSet.getInt("Contact_ID");
                int userId = resultSet.getInt("User_ID");
                Contact contact = ContactsQuery.select(contactId);

                LocalDate date = start.toLocalDate();
                LocalTime utcStartTime = start.toLocalTime();
                LocalTime utcEndTime = end.toLocalTime();

                LocalDateTime localStart = estToLocal.convertToDateTime(date, utcStartTime);
                LocalDateTime localEnd = estToLocal.convertToDateTime(date, utcEndTime);

                appointment = new Appointment(id, title, description, location, type, localStart, localEnd, customerId, contact, userId);
            }

        } catch (SQLException e) {
            System.out.println("Error with the select appointment by id method: " + e.getMessage());
        }

        return appointment;
    }

    /** Selects strings in the type column of the appointments table.
     * @return List of strings in the type column. */
    public static ObservableList<String> getTypes() {
        ObservableList<String> types = FXCollections.observableArrayList();

        try {
            String selectSql = "SELECT Type FROM appointments";
            PreparedStatement selectPs = JDBConnection.getConnection().prepareStatement(selectSql);
            ResultSet resultSet = selectPs.executeQuery();

            while (resultSet.next()) {
                String type = resultSet.getString("Type");
                types.add(type);
            }

        } catch (SQLException e) {
            System.out.println("Error with the get types method: " + e.getMessage());
        }

        return types;
    }

    /** Selects all appointments associated with a type.
     * <p><b>
     *     Lambda 1: Converts a time from EST to local time.
     * </b></p>
     * @param type The type of appointments to select.
     * @return List of appointments with the selected type. */
    public static ObservableList<Appointment> selectByType(String type) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            String selectSql = "SELECT * FROM appointments WHERE Type = ?";
            PreparedStatement selectPs = JDBConnection.getConnection().prepareStatement(selectSql);
            selectPs.setString(1, type);
            ResultSet resultSet = selectPs.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String apptType = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int userId = resultSet.getInt("User_ID");
                int customerId = resultSet.getInt("Customer_ID");
                int contactId = resultSet.getInt("Contact_ID");
                Contact contact = ContactsQuery.select(contactId);

                LocalDate date = start.toLocalDate();
                LocalTime utcStartTime = start.toLocalTime();
                LocalTime utcEndTime = end.toLocalTime();

                LocalDateTime localStart = estToLocal.convertToDateTime(date, utcStartTime);
                LocalDateTime localEnd = estToLocal.convertToDateTime(date, utcEndTime);

                Appointment appointment = new Appointment(id, title, description, location, apptType, localStart, localEnd, customerId, contact, userId);
                appointments.add(appointment);
            }

        } catch (SQLException e) {
            System.out.println("Error with the select appointment by type method: " + e.getMessage());
        }
        return appointments;
    }

    /** Selects appointments by their user id.
     * @return List of location strings. */
    public static ObservableList<String> getLocations() {
        ObservableList<String> locations = FXCollections.observableArrayList();

        try {
            String selectSql = "SELECT Location FROM appointments";
            PreparedStatement selectPs = JDBConnection.getConnection().prepareStatement(selectSql);
            ResultSet resultSet = selectPs.executeQuery();

            while (resultSet.next()) {
                String location = resultSet.getString("Location");
                locations.add(location);
            }

        } catch (SQLException e) {
            System.out.println("Error with get locations method: " + e.getMessage());
        }

        return locations;
    }

    /** Selects all appointments associated with a location.
     * <p><b>
     *     Lambda 1: Converts a time from EST to local time.
     * </b></p>
     * @param location The location of appointments to select.
     * @return List of appointments with the selected location. */
    public static ObservableList<Appointment> selectByLocation(String location) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            String selectSql = "SELECT * FROM appointments WHERE Location = ?";
            PreparedStatement selectPs = JDBConnection.getConnection().prepareStatement(selectSql);
            selectPs.setString(1, location);
            ResultSet resultSet = selectPs.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String apptLocation = resultSet.getString("Location");
                String apptType = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int userId = resultSet.getInt("User_ID");
                int customerId = resultSet.getInt("Customer_ID");
                int contactId = resultSet.getInt("Contact_ID");
                Contact contact = ContactsQuery.select(contactId);

                LocalDate date = start.toLocalDate();
                LocalTime utcStartTime = start.toLocalTime();
                LocalTime utcEndTime = end.toLocalTime();

                LocalDateTime localStart = estToLocal.convertToDateTime(date, utcStartTime);
                LocalDateTime localEnd = estToLocal.convertToDateTime(date, utcEndTime);

                Appointment appointment = new Appointment(id, title, description, apptLocation, apptType, localStart, localEnd, customerId, contact, userId);
                appointments.add(appointment);
            }

        } catch (SQLException e) {
            System.out.println("Error with the select appointment by location method: " + e.getMessage());
        }
        return appointments;
    }

    /** Selects data from columns in the appointments table that hold string values.
     * <p><b>
     *     Lambda 1: Converts a time from EST to local time.
     * </b></p>
     * @param contactId Requested appointments' customer id.
     * @return List of appointments by customer id. */
    public static ObservableList<Appointment> contactAppointments(int contactId) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            String selectSql = "SELECT * FROM appointments WHERE Contact_ID = ?";
            PreparedStatement selectPs = JDBConnection.getConnection().prepareStatement(selectSql);
            selectPs.setInt(1, contactId);
            ResultSet resultSet = selectPs.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customer = resultSet.getInt("Customer_ID");
                int dbContactId = resultSet.getInt("Contact_ID");
                Contact contact = ContactsQuery.select(dbContactId);

                int user = resultSet.getInt("User_ID");

                LocalDate date = start.toLocalDate();
                LocalTime utcStartTime = start.toLocalTime();
                LocalTime utcEndTime = end.toLocalTime();

                LocalDateTime localStart = estToLocal.convertToDateTime(date, utcStartTime);
                LocalDateTime localEnd = estToLocal.convertToDateTime(date, utcEndTime);

                Appointment DBAppointment = new Appointment(id, title, description, location, type, localStart, localEnd, customer, contact, user);
                appointments.add(DBAppointment);
            }

        } catch (SQLException e) {
            System.out.println("something went wrong with the appointments by customer method: " + e.getMessage());
        }

        return appointments;
    }

    /** Inserts a new appointment into the database.
     * @param title       The appointment title.
     * @param description A description of the appointment.
     * @param location    The physical location of the appointment.
     * @param type        The type of appointment.
     * @param start       The appointment start date and time.
     * @param end         The appointment end date and time.
     * @param userId      The user ID.
     * @param customerId  The customer ID.
     * @param contactId   The contact ID. */
    public static void insert(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int userId, int customerId, int contactId) {

        try {
            String insertSql = "INSERT INTO appointments VALUES(NULL, ?, ?, ?, ?, ?, ?, NULL, NULL, NULL, NULL, ?, ?, ?)";
            PreparedStatement insertPs = JDBConnection.getConnection().prepareStatement(insertSql);
            insertPs.setString(1, title);
            insertPs.setString(2, description);
            insertPs.setString(3, location);
            insertPs.setString(4, type);
            insertPs.setTimestamp(5, Timestamp.valueOf(start));
            insertPs.setTimestamp(6, Timestamp.valueOf(end));
            insertPs.setInt(7, customerId);
            insertPs.setInt(8, userId);
            insertPs.setInt(9, contactId);
            insertPs.executeUpdate();
        } catch (SQLException e) {
            System.out.println("something went wrong with inserting an appointment: " + e.getMessage());
        }

    }

    /** Updates an appointment record.
     * @param title         Title of the appointment.
     * @param description   A description of the appointment
     * @param location      Location of the appointment.
     * @param type          Type of appointment.
     * @param start         Start date time of the appointment.
     * @param end           End date time of the appointment.
     * @param userId        The user ID.
     * @param customerId    The customer ID.
     * @param contactId     The contactID.
     * @param appointmentId The appointment ID. */
    public static void update(int appointmentId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int userId, int customerId, int contactId) {

        try {
            String updateSql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, User_ID = ?, Customer_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement updatePs = JDBConnection.getConnection().prepareStatement(updateSql);
            updatePs.setString(1, title);
            updatePs.setString(2, description);
            updatePs.setString(3, location);
            updatePs.setString(4, type);
            updatePs.setTimestamp(5, Timestamp.valueOf(start));
            updatePs.setTimestamp(6, Timestamp.valueOf(end));
            updatePs.setInt(7, userId);
            updatePs.setInt(8, customerId);
            updatePs.setInt(9, contactId);
            updatePs.setInt(10, appointmentId);
            updatePs.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with the update appointment method: " + e.getMessage());
        }

    }

    /** Removes a customer from the customers table.
     * @param appointmentId The ID of the appointment to delete. */
    public static void delete(int appointmentId) {

        try {
            String deleteSql = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement deletePs = JDBConnection.getConnection().prepareStatement(deleteSql);
            deletePs.setInt(1, appointmentId);
            deletePs.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error with the appointment delete method: " + e.getMessage());
        }

    }

}

