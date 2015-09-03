package org.springframework.social.salesforce.api.impl.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@see org.springframework.social.salesforce.api.PickListEntry} Mixin for api v23.0.
 *
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PickListEntryMixin {

    @JsonCreator
    PickListEntryMixin(
            @JsonProperty("value") String value,
            @JsonProperty("label") String label,
            @JsonProperty("active") boolean active,
            @JsonProperty("defaultValue") boolean defaultValue) {
    }

}
