package org.springframework.social.salesforce.api.impl.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@see org.springframework.social.salesforce.api.ApiVersion} Mixin for api v23.0.
 *
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiVersionMixin {

    @JsonCreator
    ApiVersionMixin(
            @JsonProperty("version") String version,
            @JsonProperty("label") String label,
            @JsonProperty("url") String url) {
    }

}
