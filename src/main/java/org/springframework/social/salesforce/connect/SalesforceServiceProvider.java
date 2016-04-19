package org.springframework.social.salesforce.connect;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.api.impl.SalesforceTemplate;

/**
 * Salesforce ServiceProvider implementation.
 *
 * @author Umut Utkan
 */
public class SalesforceServiceProvider extends AbstractOAuth2ServiceProvider<Salesforce>
{

    public SalesforceServiceProvider(String clientId,
                                     String clientSecret,
                                     ClientHttpRequestFactory clientHttpRequestFactory)
    {
        this(clientId,
             clientSecret,
             "https://login.salesforce.com/services/oauth2/authorize",
             "https://login.salesforce.com/services/oauth2/token",
             clientHttpRequestFactory);
    }

    public SalesforceServiceProvider(String clientId,
                                     String clientSecret,
                                     String authorizeUrl,
                                     String tokenUrl,
                                     ClientHttpRequestFactory clientHttpRequestFactory)
    {
        super(new SalesforceOAuth2Template(clientId, clientSecret, authorizeUrl, tokenUrl, clientHttpRequestFactory));
    }

    @Override
    public Salesforce getApi(String accessToken)
    {
        SalesforceTemplate template = new SalesforceTemplate(accessToken);

        // gets the returned instance url and sets to Salesforce template as base url.
        String instanceUrl = ((SalesforceOAuth2Template) getOAuthOperations()).getInstanceUrl();
        if (instanceUrl != null) {
            template.setInstanceUrl(instanceUrl);
        }
        return template;
    }
}
