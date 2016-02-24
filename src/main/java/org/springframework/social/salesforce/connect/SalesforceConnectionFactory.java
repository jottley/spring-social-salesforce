package org.springframework.social.salesforce.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.salesforce.api.Salesforce;

/**
 * @author Umut Utkan
 * @author Jared Ottley
 */
public class SalesforceConnectionFactory extends OAuth2ConnectionFactory<Salesforce> {

    public SalesforceConnectionFactory(String clientId, String clientSecret) {
        super("salesforce", new SalesforceServiceProvider(clientId, clientSecret),
                new SalesforceAdapter());
    }

    public SalesforceConnectionFactory(String clientId, String clientSecret, String instanceUrl) {
        super("salesforce", new SalesforceServiceProvider(clientId, clientSecret),
              new SalesforceAdapter(instanceUrl));
    }

    public SalesforceConnectionFactory(String clientId, String clientSecret, String authorizeUrl, String tokenUrl) {
        super("salesforce", new SalesforceServiceProvider(clientId, clientSecret,
                authorizeUrl, tokenUrl), new SalesforceAdapter());
    }

    public SalesforceConnectionFactory(String clientId, String clientSecret, String authorizeUrl, String tokenUrl, String instanceUrl) {
        super("salesforce", new SalesforceServiceProvider(clientId, clientSecret,
                                                          authorizeUrl, tokenUrl), new SalesforceAdapter(instanceUrl));
    }

}
