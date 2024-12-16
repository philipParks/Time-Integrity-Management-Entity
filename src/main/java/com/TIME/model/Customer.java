package com.TIME.model;

/** Defines a customer object. */
public class Customer {

    private int customerId;
    private String name;
    private String streetAddress;
    private FirstLevelDivision division;
    private String postalCode;
    private String phone;

    /** Overrides the toString() method to only return the customer name.
     * @return just the name from the customer object. */
    @Override
    public String toString() {
        return name;
    }

    /** Constructs a customer object.
     *
     * @param id         Automatically assigned unique value.
     * @param name       Name of the customer.
     * @param address    Street address of the customer.
     * @param division   First level division of the customer.
     * @param postalCode Postal code of the customer.
     * @param phone      Phone number of the customer. */
    public Customer(int id, String name, String address, FirstLevelDivision division, String postalCode, String phone) {
        this.customerId = id;
        this.name = name;
        this.streetAddress = address;
        this.division = division;
        this.postalCode = postalCode;
        this.phone = phone;
    }

    /** Gets customer id.
     * @return Automatically assigned unique value. */
    public int getCustomerId() {
        return customerId;
    }

    /** Sets customer id.
     * @param customerId Automatically assigned unique value. */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /** Gets customer name.
     * @return Customer name. */
    public String getName() {
        return name;
    }

    /** Sets customer name.
     * @param name Customer name. */
    public void setName(String name) {
        this.name = name;
    }

    /** Get customer address.
     * @return Customer address. */
    public String getStreetAddress() {
        return streetAddress;
    }

    /** Sets customer address.
     * @param streetAddress Customer address. */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /** Gets customer state/province.
     * @return Customer state/province. */
    public FirstLevelDivision getDivision() {
        return division;
    }

    /** Sets customer state/province.
     * @param division Customer state/province. */
    public void setDivision(FirstLevelDivision division) {
        this.division = division;
    }

    /** Gets customer postal code.
     * @return Customer postal code. */
    public String getPostalCode() {
        return postalCode;
    }

    /** Sets customer postal code.
     * @param postalCode Customer postal code. */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /** Gets customer phone number.
     * @return Customer phone number. */
    public String getPhone() {
        return phone;
    }

    /** Sets customer phone number.
     * @param phone Customer phone number. */
    public void setPhone(String phone) {
        this.phone = phone;
    }

}
