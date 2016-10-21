package org.springframework.social.salesforce.api.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.api.SalesforceUserDetails;
import org.springframework.social.salesforce.api.UserOperations;
import org.springframework.web.client.RestTemplate;

/**
 * Default implementation of UserOperations.
 */
public class UserOperationsTemplate extends AbstractSalesForceOperations<Salesforce> implements UserOperations {
    private static final String USER_INFO_ENDPOINT = "https://login.salesforce.com/services/oauth2/userinfo";

    private RestTemplate restTemplate;

    public UserOperationsTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;
    }

    @Override
    public SalesforceUserDetails getSalesforceUserDetails() {
        requireAuthorization();
        return restTemplate.exchange(USER_INFO_ENDPOINT, HttpMethod.GET, new HttpEntity<String>(""), SalesforceUserDetails.class, "v23.0").getBody();
    }

}
