package org.springframework.social.salesforce.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.api.impl.SalesforceTemplate;

/**
 * Salesforce ServiceProvider implementation.
 *
 * @author Umut Utkan
 */
public class SalesforceServiceProvider extends AbstractOAuth2ServiceProvider<Salesforce> {

    public SalesforceServiceProvider(String clientId, String clientSecret) {
        super(new SalesforceOAuth2Template(clientId, clientSecret,
                "https://na7.salesforce.com/services/oauth2/authorize",
                "https://na7.salesforce.com/services/oauth2/token"));
    }

    public Salesforce getApi(String accessToken) {
        SalesforceTemplate template = new SalesforceTemplate(accessToken);

        // gets the returned instance url and sets to Salesforce template as base url.
        String instanceUrl = ((SalesforceOAuth2Template) getOAuthOperations()).getInstanceUrl();
        if (instanceUrl != null) {
            template.setInstanceUrl(instanceUrl);
        }

        return template;
    }

}
