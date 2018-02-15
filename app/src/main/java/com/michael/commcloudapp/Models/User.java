package com.michael.commcloudapp.Models;

/**
 * Created by michaelmacahilig on 7/11/15.
 * Generator class for the user of the application
 */
public class User {
    private String firstName;
    private String lastName;
    private String id;
    private String email;
    private String address;
    private String city;
    private String state;

    /**
     * constructor class for the User
     * @param firstName
     * @param lastName
     * @param id
     * @param email
     * @param address
     * @param city
     * @param state
     */
    public User(String firstName, String lastName, String id, String email, String address, String city, String state) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.email = email;
        this.address = address;
        this.city = city;
        this.state = state;

    }


    /**
     *returns the stored firstname from the object
     * @return firstname
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * sets the firstname params onto the object
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * returns the lastname stored in the object
     * @return lastname
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * sets the lastname params onto the object
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * returns the user id stored in the object
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * saves the input id onto the object
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * returns the address from the object
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * sets the address params onto the object
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * returns the city stored in the object
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * sets the city params onto the object
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * returns the state stored in the object
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * set the state params onto the object
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }


}
