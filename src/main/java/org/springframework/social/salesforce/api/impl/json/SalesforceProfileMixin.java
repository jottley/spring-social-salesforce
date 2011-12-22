package org.springframework.social.salesforce.api.impl.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.social.salesforce.api.Photo;

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
