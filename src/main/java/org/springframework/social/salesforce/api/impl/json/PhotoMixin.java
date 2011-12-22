package org.springframework.social.salesforce.api.impl.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * {@see org.springframework.social.salesforce.api.Photo} Mixin for api v23.0.
 *
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoMixin {

    @JsonCreator
    PhotoMixin(
            @JsonProperty String smallPhotoUrl,
            @JsonProperty String largePhotoUrl) {
    }

}
