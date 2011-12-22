package org.springframework.social.salesforce.api.impl.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

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
