package com.TIME.model;

import java.time.LocalDateTime;

/** Defines an appointment object. */
public class Appointment {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customer;
    private Contact contact;
    private int user;

    /** Constructs an instance of an appointment object.
     * @param id Automatically assigned unique value.
     * @param title A title that names the appointment.
     * @param description A description of the appointment.
     * @param location Where the appointment will be.
     * @param type What type of appointment it is.
     * @param start Date time when the appointment begins.
     * @param end Date time when the appointment ends.
     * @param customer Customer associated with the appointment.
     * @param contact Contact associated with the appointment.
     * @param user User associated with the appointment */
    public Appointment(int id, String title, String description, String location, String type, LocalDateTime start,
                       LocalDateTime end, int customer, Contact contact, int user) {
        this.appointmentId = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer = customer;
        this.contact = contact;
        this.user = user;
    }

    /** Gets the appointment id.
     * @return Automatically assigned unique value. */
    public int getAppointmentId() {
        return appointmentId;
    }

    /** Sets the appointment id.
     * @param appointmentId Automatically assigned unique value. */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /** Gets the appointment title.
     * @return appointment title. */
    public String getTitle() {
        return title;
    }

    /** Sets the appointment title.
     * @param title Title of the appointment. */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Gets the appointment description.
     * @return Description of the appointment. */
    public String getDescription() {
        return description;
    }

    /** Sets the appointment description.
     * @param description A description of the appointment. */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Gets the appointment location.
     * @return Physical location of the appointment. */
    public String getLocation() {
        return location;
    }

    /** Sets the appointment location.
     * @param location Physical location of the appointment. */
    public void setLocation(String location) {
        this.location = location;
    }

    /** Gets the appointment type.
     * @return Type of appointment. */
    public String getType() {
        return type;
    }

    /** Sets the appointment type.
     * @param type Type of appointment. */
    public void setType(String type) {
        this.type = type;
    }

    /** Gets the appointment start date and time.
     * @return Start date and time of the appointment. */
    public LocalDateTime getStart() {
        return start;
    }

    /** Sets the appointment start date and time.
     * @param start Start date and time of the appointment. */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /** Gets the appointment end date and time.
     * @return End date and time of the appointment. */
    public LocalDateTime getEnd() {
        return end;
    }

    /** Sets the appointment end date and time.
     * @param end End date and time of the appointment. */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /** Gets a contact.
     * @return A contact. */
    public Contact getContact() {
        return contact;
    }

    /** Sets a contact.
     * @param contact A contact. */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /** Gets a customer.
     * @return A customer. */
    public int getCustomer() {
        return customer;
    }

    /** Sets a customer.
     * @param customer A customer. */
    public void setCustomer(int customer) {
        this.customer = customer;
    }

    /** Gets a user.
     * @return A user*/
    public int getUser() {
        return user;
    }

    /** Sets a user.
     * @param user A user. */
    public void setUser(int user) {
        this.user = user;
    }

}
