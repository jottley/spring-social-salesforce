package org.springframework.social.salesforce.api.impl.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@see org.springframework.social.salesforce.api.Relationship} Mixin for api v23.0.
 *
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipMixin {

    @JsonCreator
    RelationshipMixin(
            @JsonProperty("field") String field,
            @JsonProperty("relationshipName") String relationshipName,
            @JsonProperty("childObject") String childObject) {
    }

    @JsonProperty("deprecatedAndHidden")
    boolean deprecatedAndHidden;

    @JsonProperty("cascadeDelete")
    boolean cascadeDelete;

    @JsonProperty("restrictedDelete")
    boolean restrictedDelete;

}
