package org.springframework.social.salesforce.api;
import org.springframework.social.salesforce.api.SalesforceUserDetails;

/**
 * Defines operations for retrieving user related info.
 */
public interface UserOperations {

    SalesforceUserDetails getSalesforceUserDetails(); 

}
