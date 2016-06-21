package org.springframework.social.salesforce.api;
import org.springframework.social.salesforce.api.SalesforceUserDetails;

/**
 * Defines operations for retrieving user related info.
 */
public interface UserOperations {

    /**
     * Retrieves the details for the current logged in user.
     * @return user details 
     */
    SalesforceUserDetails getSalesforceUserDetails(); 

}
