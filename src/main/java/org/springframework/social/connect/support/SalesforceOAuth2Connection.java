package org.springframework.social.connect.support;

import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.SalesforceConnectionData;
import org.springframework.social.oauth2.OAuth2ServiceProvider;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.connect.SalesforceAdapter;

public class SalesforceOAuth2Connection extends OAuth2Connection<Salesforce>
{
    private static final long serialVersionUID = -1820218900251701905L;

    private String identityUrl;

    public SalesforceOAuth2Connection(String providerId,
                                      String providerUserId,
                                      String accessToken,
                                      String refreshToken,
                                      Long expireTime,
                                      String identityUrl,
                                      OAuth2ServiceProvider<Salesforce> serviceProvider)
    {
        super(providerId,
              providerUserId,
              accessToken,
              refreshToken,
              expireTime,
              serviceProvider,
              new SalesforceAdapter(identityUrl));
        this.identityUrl = identityUrl;
    }

    public SalesforceOAuth2Connection(SalesforceConnectionData data, OAuth2ServiceProvider<Salesforce> serviceProvider)
    {
        super(data, serviceProvider, new SalesforceAdapter(data.getIdentityUrl()));
        this.identityUrl = data.getIdentityUrl();
    }

    @Override
    public ConnectionData createData()
    {
        ConnectionData connectionData = super.createData();
        synchronized (getMonitor()) {
            return new SalesforceConnectionData(getKey().getProviderId(),
                                                getKey().getProviderUserId(),
                                                getDisplayName(),
                                                getProfileUrl(),
                                                getImageUrl(),
                                                connectionData.getAccessToken(),
                                                null,
                                                connectionData.getRefreshToken(),
                                                connectionData.getExpireTime(),
                                                identityUrl);
        }
    }
}
