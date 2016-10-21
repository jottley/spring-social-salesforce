package org.springframework.social.salesforce.api.impl.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@see org.springframework.social.salesforce.api.UserDetails} Mixin for api v23.0.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesforceUserDetailsMixin {

    @JsonCreator
    SalesforceUserDetailsMixin(
            @JsonProperty("user_id") String id,
            @JsonProperty("given_name") String firstName,
            @JsonProperty("family_name") String lastName,
            @JsonProperty("email") String email,
            @JsonProperty("name") String name,            
            @JsonProperty("organization_id") String organizationId,
            @JsonProperty("nickname") String nickname,
            @JsonProperty("preferred_username") String preferredUsername) {
    }
}
