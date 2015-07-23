package org.springframework.social.salesforce.api;

import java.io.Serializable;

public class SalesforceIdentity implements Serializable
{
    private String userId;

    private String username;

    private String organizationId;

    private String email;

    private String firstName;

    private String lastName;

    public SalesforceIdentity(String userId,
                              String username,
                              String organizationId,
                              String email,
                              String firstName,
                              String lastName)
    {
        this.userId = userId;
        this.username = username;
        this.organizationId = organizationId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getUsername()
    {
        return username;
    }

    public String getOrganizationId()
    {
        return organizationId;
    }

    public String getEmail()
    {
        return email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getFullName()
    {
        return (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
    }
}
