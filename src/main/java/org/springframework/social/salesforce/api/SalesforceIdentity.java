package org.springframework.social.salesforce.api;

import java.io.Serializable;

public class SalesforceIdentity implements Serializable
{
    private String organizationId;

    private String userId;

    public SalesforceIdentity(String organizationId,
                              String userId)
    {
        this.organizationId = organizationId;
        this.userId = userId;
    }

    public String getOrganizationId()
    {
        return organizationId;
    }

    public String getUserId()
    {
        return userId;
    }
}
