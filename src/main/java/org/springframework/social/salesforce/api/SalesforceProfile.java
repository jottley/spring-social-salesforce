package org.springframework.social.salesforce.api;

import java.io.Serializable;

/**
 * @author Umut Utkan
 */
@SuppressWarnings("serial")
public class SalesforceProfile implements Serializable {

    private String id;

    private String email;

    private String firstName;

    private String lastName;

    private Photo photo;


    public SalesforceProfile(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Photo getPhoto() {
        return this.photo;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getUsername() {
        return this.id;
    }

}
