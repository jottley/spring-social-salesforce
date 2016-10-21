package org.springframework.social.salesforce.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesforceUserDetails extends SalesforceProfile {
    private String organizationId;

    private String nickname;

    private String preferredUsername;

    public SalesforceUserDetails(String id, String firstName, String lastName,
        String email, String name, String organizationId, String nickname,
        String preferredUsername) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.name = name;
        this.organizationId = organizationId;
        this.nickname = nickname;
        this.preferredUsername = preferredUsername;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }
}
