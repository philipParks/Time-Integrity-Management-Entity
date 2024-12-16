package com.TIME.model;

/** Defines a contact object. */
public class Contact {

    private final int contactId;
    private final String contactName;
    private final String email;

    /** Overrides the toString method to only return the contact name.
     * @return just the name from the contact object. */
    @Override
    public String toString() {
        return contactName;
    }

    /** Constructs an instance of a contact object.
     * @param id Automatically assigned unique value.
     * @param name The contacts name.
     * @param email The contacts email address.*/
    public Contact(int id, String name, String email) {
        this.contactId = id;
        this.contactName = name;
        this.email = email;
    }

    /** Gets the contact id.
     * @return Automatically assigned unique value.*/
    public int getContactId() {
        return contactId;
    }

    /** Gets the contact name.
     * @return Name of the contact. */
    public String getContactName() {
        return contactName;
    }

    /** Gets the contact email address.
     * @return Email address of the contact. */
    public String getEmail() {
        return email;
    }

}
