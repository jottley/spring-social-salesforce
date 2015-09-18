package org.springframework.social.salesforce.api.impl.json;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
