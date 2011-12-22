package org.springframework.social.salesforce.client;

import org.springframework.social.salesforce.api.Salesforce;

/**
 * Salesforce template factory for those who want to use salesforce with client login.
 *
 * @author Umut Utkan
 */
public interface SalesforceFactory {

    Salesforce create(String username, String password, String securityToken);

}
