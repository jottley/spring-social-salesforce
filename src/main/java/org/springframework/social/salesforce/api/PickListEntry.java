package org.springframework.social.salesforce.api;

/**
 * @author Umut Utkan
 */
public class PickListEntry {

    private String value;

    private boolean active;

    private String label;

    private boolean defaultValue;

    //TODO: find how to deserialize.
    //private String validFor;


    public PickListEntry(String value, String label, boolean active, boolean defaultValue) {
        this.value = value;
        this.active = active;
        this.label = label;
        this.defaultValue = defaultValue;
    }


    public String getValue() {
        return value;
    }

    public boolean isActive() {
        return active;
    }

    public String getLabel() {
        return label;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

}
