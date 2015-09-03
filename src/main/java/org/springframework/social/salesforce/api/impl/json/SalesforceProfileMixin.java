package org.springframework.social.salesforce.api.impl.json;

import org.springframework.social.salesforce.api.Photo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@see org.springframework.social.salesforce.api.SalesforceProfile} Mixin for api v23.0.
 *
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesforceProfileMixin {

    @JsonCreator
    SalesforceProfileMixin(
            @JsonProperty("id") String id,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("email") String email) {
    }

    @JsonProperty("photo")
    Photo photo;

}
