package org.springframework.social.salesforce.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesforceUserDetails implements Serializable {

    private String id;

    private String organizationId;

    private String nickname;

    private String name;

    private String preferredUsername;

    public SalesforceUserDetails(String id, String organizationId,
        String nickname, String name, String preferredUsername) {
    super();
    this.id = id;
    this.organizationId = organizationId;
    this.nickname = nickname;
    this.name = name;
    this.preferredUsername = preferredUsername;
    }

    public String getId() {
        return id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

}
