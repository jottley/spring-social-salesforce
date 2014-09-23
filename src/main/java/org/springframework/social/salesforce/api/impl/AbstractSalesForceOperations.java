package org.springframework.social.salesforce.api.impl;

import org.springframework.social.ApiBinding;
import org.springframework.social.MissingAuthorizationException;

/**
 * @author Umut Utkan
 */
public class AbstractSalesForceOperations<T extends ApiBinding> {

    protected T api;


    public AbstractSalesForceOperations(T api) {
        this.api = api;
    }

    protected void requireAuthorization() {
        if (!api.isAuthorized()) {
            throw new MissingAuthorizationException("salesforce");
        }
    }

    protected static String BASE_URL = "https://na7.salesforce.com/services/data";

}
