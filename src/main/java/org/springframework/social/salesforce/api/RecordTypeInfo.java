package org.springframework.social.salesforce.api;

/**
 * @author Umut Utkan
 */
public class RecordTypeInfo {

    private String name;

    private boolean available;

    private String recordTypeId;

    private boolean defaultRecordTypeMapping;


    public RecordTypeInfo(String name, boolean available, String recordTypeId, boolean defaultRecordTypeMapping) {
        this.name = name;
        this.available = available;
        this.recordTypeId = recordTypeId;
        this.defaultRecordTypeMapping = defaultRecordTypeMapping;
    }


    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getRecordTypeId() {
        return recordTypeId;
    }

    public boolean isDefaultRecordTypeMapping() {
        return defaultRecordTypeMapping;
    }

}
