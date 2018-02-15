package com.michael.commcloudapp.Models;

/**
 * Created by Michael macahilig on 14/08/2015.
 * an class to generate an Alert object saving the sent alert messages
 */
public class Alert {

    private String details;
    private String alertType;
    private String alertRating;
    private String time;
    private String location;
    private String address;
    private String city;
    private String state;


    /**
     * COnstructor function for the generation of sent alerts
     * @param details
     * @param alertType
     * @param alertRating
     * @param time
     * @param address
     * @param city
     * @param state
     */
    public Alert(String details, String alertType, String alertRating,
                 String time, String address, String city, String state) {
        this.details = details;
        this.alertType = alertType;
        this.alertRating = alertRating;
        this.time = time;
        this.address = address;
        this.city = city;
        this.state = state;

    }

    /**
     * function that returns the address
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * sets the address in the address object
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * returns the saved city in the object
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * sets the city in the object by the input string
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * returns the state saved in the object
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * saves the state parameter onto the object
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * function that gets the stored details
     * @return details
     */
    public String getDetails() {
        return  details;
    }

    /**
     * saved the details params onto the object
     * @param details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * sets the clientname of the object
     * @param clientName
     */
    public void setClientName(String clientName) {
        this.details = clientName;
    }

    /**
     * returns the alerttype of the object
     * @return alerttype
     */
    public String getAlertType() {
        return alertType;
    }

    /**
     * saves the alerttype onto the object
     * @param alertType
     */
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    /**
     * returns the alert rating saved in the object
     * @return alertRating
     */
    public String getAlertRating() {
        return alertRating;
    }

    /**
     * saves the alertrating onto the object
     * @param alertRating
     */
    public void setAlertRating(String alertRating) {
        this.alertRating = alertRating;
    }

    /**
     * returns the time set onto the object
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * saves the time params in the object
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * returns the location saved in the object
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * sets the location params within the object
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
