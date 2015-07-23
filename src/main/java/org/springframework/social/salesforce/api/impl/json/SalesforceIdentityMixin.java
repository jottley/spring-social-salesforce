package org.springframework.social.salesforce.api.impl.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesforceIdentityMixin
{
    @JsonCreator
    public SalesforceIdentityMixin(@JsonProperty("user_id") String userId,
                                   @JsonProperty("username") String username,
                                   @JsonProperty("organization_id") String organizationId,
                                   @JsonProperty("email") String email,
                                   @JsonProperty("first_name") String firstName,
                                   @JsonProperty("last_name") String lastName)
    {
    }
}
