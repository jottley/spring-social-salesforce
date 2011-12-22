package org.springframework.social.salesforce.api.impl.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * {@see org.springframework.social.salesforce.api.Status} Mixin for api v23.0.
 *
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusMixin {

    @JsonCreator
    StatusMixin(
            @JsonProperty("text") String text) {
    }

    @JsonProperty("messageSegments")
    List<Map<String, String>> segments;

}
