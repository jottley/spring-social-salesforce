package org.springframework.social.salesforce.api;

/**
 * @author Umut Utkan
 */
public class Relationship {

    private String field;

    private boolean deprecatedAndHidden;

    private String relationshipName;

    private boolean cascadeDelete;

    private boolean restrictedDelete;

    private String childSObject;


    public Relationship(String field, String relationshipName, String childObject) {
        this.field = field;
        this.relationshipName = relationshipName;
        this.childSObject = childObject;
    }


    public String getField() {
        return field;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public String getChildSObject() {
        return childSObject;
    }

    public boolean isDeprecatedAndHidden() {
        return deprecatedAndHidden;
    }

    public void setDeprecatedAndHidden(boolean deprecatedAndHidden) {
        this.deprecatedAndHidden = deprecatedAndHidden;
    }

    public boolean isCascadeDelete() {
        return cascadeDelete;
    }

    public void setCascadeDelete(boolean cascadeDelete) {
        this.cascadeDelete = cascadeDelete;
    }

    public boolean isRestrictedDelete() {
        return restrictedDelete;
    }

    public void setRestrictedDelete(boolean restrictedDelete) {
        this.restrictedDelete = restrictedDelete;
    }

}
