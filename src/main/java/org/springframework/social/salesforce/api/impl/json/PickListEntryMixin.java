package org.springframework.social.salesforce.api.impl.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

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
