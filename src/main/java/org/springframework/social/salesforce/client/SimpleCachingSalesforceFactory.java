package org.springframework.social.salesforce.client;

import org.springframework.social.salesforce.api.Salesforce;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple caching wrapper for SalesforceFactory.
 *
 * @author Umut Utkan
 */
public class SimpleCachingSalesforceFactory implements SalesforceFactory {

    private SalesforceFactory delegate;

    private final Map<String, Salesforce> cache = new HashMap<String, Salesforce>();


    public SimpleCachingSalesforceFactory(SalesforceFactory delegate) {
        this.delegate = delegate;
    }


    @Override
    public Salesforce create(String username, String password, String securityToken) {
        synchronized (this.cache) {
            Salesforce template = cache.get(username);
            if (template == null) {
                this.cache.put(username, this.delegate.create(username, password, securityToken));
            }
            return this.cache.get(username);
        }
    }

}
