package org.springframework.social.salesforce.api.impl.json;

import org.springframework.social.salesforce.api.ApiVersion;
import org.springframework.social.salesforce.api.Field;
import org.springframework.social.salesforce.api.Photo;
import org.springframework.social.salesforce.api.PickListEntry;
import org.springframework.social.salesforce.api.QueryResult;
import org.springframework.social.salesforce.api.RecordTypeInfo;
import org.springframework.social.salesforce.api.Relationship;
import org.springframework.social.salesforce.api.ResultItem;
import org.springframework.social.salesforce.api.SObjectDetail;
import org.springframework.social.salesforce.api.SObjectSummary;
import org.springframework.social.salesforce.api.SalesforceProfile;
import org.springframework.social.salesforce.api.Status;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Jackson module for api version v23.0.
 *
 * @author Umut Utkan
 */
public class SalesforceModule extends SimpleModule {

    public SalesforceModule() {
        super("SalesforceModule",new Version(23, 0, 0, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        context.setMixInAnnotations(ApiVersion.class, ApiVersionMixin.class);
        context.setMixInAnnotations(SalesforceProfile.class, SalesforceProfileMixin.class);
        context.setMixInAnnotations(Photo.class, PhotoMixin.class);
        context.setMixInAnnotations(Status.class, StatusMixin.class);
        context.setMixInAnnotations(SObjectSummary.class, SObjectSummaryMixin.class);
        context.setMixInAnnotations(RecordTypeInfo.class, RecordTypeInfoMixin.class);
        context.setMixInAnnotations(Relationship.class, RelationshipMixin.class);
        context.setMixInAnnotations(PickListEntry.class, PickListEntryMixin.class);
        context.setMixInAnnotations(Field.class, FieldMixin.class);
        context.setMixInAnnotations(SObjectDetail.class, SObjectDetailMixin.class);
        context.setMixInAnnotations(QueryResult.class, QueryResultMixin.class);
        context.setMixInAnnotations(ResultItem.class, ResultItemMixin.class);
    }

}
