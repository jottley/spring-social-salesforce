package org.springframework.social.salesforce.api.impl.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.social.salesforce.api.PickListEntry;

import java.util.List;

/**
 * {@see org.springframework.social.salesforce.api.Field} Mixin for api v23.0.
 *
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldMixin {

    @JsonCreator
    FieldMixin(
            @JsonProperty("name") String name,
            @JsonProperty("type") String type,
            @JsonProperty("label") String label) {
    }

    @JsonProperty("length")
    int length;

    @JsonProperty("defaultValue")
    String defaultValue;

    @JsonProperty("updateable")
    boolean updateable;

    @JsonProperty("calculated")
    boolean calculated;

    @JsonProperty("filterable")
    boolean filterable;

    @JsonProperty("sortable")
    boolean sortable;

    @JsonProperty("controllerName")
    String controllerName;

    @JsonProperty("unique")
    boolean unique;

    @JsonProperty("nillable")
    boolean nillable;

    @JsonProperty("precision")
    int precision;

    @JsonProperty("scale")
    int scale;

    @JsonProperty("caseSensitive")
    boolean caseSensitive;

    @JsonProperty("byteLength")
    int byteLength;

    @JsonProperty("nameField")
    boolean nameField;

    @JsonProperty("externalId")
    boolean externalId;

    @JsonProperty("idLookup")
    boolean idLookup;

    @JsonProperty("inlineHelpText")
    String inlineHelpText;

    @JsonProperty("createable")
    boolean createable;

    @JsonProperty("soapType")
    String soapType;

    @JsonProperty("autoNumber")
    boolean autoNumber;

    @JsonProperty("namePointing")
    boolean namePointing;

    @JsonProperty("custom")
    boolean custom;

    @JsonProperty("defaultedOnCreate")
    boolean defaultedOnCreate;

    @JsonProperty("deprecatedAndHidden")
    boolean deprecatedAndHidden;

    @JsonProperty("htmlFormatted")
    boolean htmlFormatted;

    @JsonProperty("defaultValueFormula")
    String defaultValueFormula;

    @JsonProperty("calculatedFormula")
    String calculatedFormula;

    @JsonProperty("restrictedPicklist")
    boolean restrictedPicklist;

    @JsonProperty("picklistValues")
    List<PickListEntry> picklistValues;

    @JsonProperty("dependentPicklist")
    boolean dependentPicklist;

    @JsonProperty("referenceTo")
    String[] referenceTo;

    @JsonProperty("relationshipName")
    String relationshipName;

    @JsonProperty("relationshipOrder")
    int relationshipOrder;

    @JsonProperty("writeRequiresMasterRead")
    boolean writeRequiresMasterRead;

    @JsonProperty("cascadeDelete")
    boolean cascadeDelete;

    @JsonProperty("restrictedDelete")
    boolean restrictedDelete;

    @JsonProperty("digits")
    int digits;

    @JsonProperty("groupable")
    boolean groupable;

}
