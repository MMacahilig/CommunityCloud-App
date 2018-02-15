package com.michael.commcloudapp.Models;

/**
 * Created by michaelmacahilig on 4/11/15.
 * generates the event message object used for displaying event information
 */
public class Event {

    private String details;
    private String alertType;
    private String alertRating;
    private String time;
    private String location;
    private String address;
    private String city;
    private String state;

    /**
     * event constructor class
     * @param details
     * @param alertType
     * @param alertRating
     * @param time
     * @param address
     * @param city
     * @param state
     */
    public Event(String details, String alertType, String alertRating,
                 String time, String address, String city, String state){

        this.details = details;
        this.alertType = alertType;
        this.alertRating = alertRating;
        this.time = time;
        this.address = address;
        this.city = city;
        this.state = state;
    }


    /**
     * returns the details stored in the object
     * @return details
     */
    public String getDetails() {
        return details;
    }

    /**
     * sets the details params onto the object
     * @param details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * returns the alert type stored in the object
     * @return alertype
     */
    public String getAlertType() {
        return alertType;
    }

    /**
     * sets the alerttype params onto the object
     * @param alertType
     */
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    /**
     * returns the alert rating stored in the object
     * @return alertrating
     */
    public String getAlertRating() {
        return alertRating;
    }

    /**
     * sets the alert rating params onto the object
     * @param alertRating
     */
    public void setAlertRating(String alertRating) {
        this.alertRating = alertRating;
    }

    /**
     * returns the time stored in the object
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * sets the time onto the object
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * returns the location string of the object
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * sets the location params onto the object
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * returns the address stored onto the object
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
     * sets the state onto the object
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }
}
