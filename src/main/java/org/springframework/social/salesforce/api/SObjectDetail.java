package org.springframework.social.salesforce.api;

import java.util.List;

/**
 * @author Umut Utkan
 */
public class SObjectDetail extends SObjectSummary {

    private List<Field> fields;

    private List<Relationship> childRelationships;

    private boolean listviewable;

    private boolean lookupLayoutable;

    private List<RecordTypeInfo> recordTypeInfos;

    private boolean searchLayoutable;


    public SObjectDetail() {

    }


    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Relationship> getChildRelationships() {
        return childRelationships;
    }

    public void setChildRelationships(List<Relationship> childRelationships) {
        this.childRelationships = childRelationships;
    }

    public boolean isListviewable() {
        return listviewable;
    }

    public void setListviewable(boolean listviewable) {
        this.listviewable = listviewable;
    }

    public boolean isLookupLayoutable() {
        return lookupLayoutable;
    }

    public void setLookupLayoutable(boolean lookupLayoutable) {
        this.lookupLayoutable = lookupLayoutable;
    }

    public List<RecordTypeInfo> getRecordTypeInfos() {
        return recordTypeInfos;
    }

    public void setRecordTypeInfos(List<RecordTypeInfo> recordTypeInfos) {
        this.recordTypeInfos = recordTypeInfos;
    }

    public boolean isSearchLayoutable() {
        return searchLayoutable;
    }

    public void setSearchLayoutable(boolean searchLayoutable) {
        this.searchLayoutable = searchLayoutable;
    }

}
