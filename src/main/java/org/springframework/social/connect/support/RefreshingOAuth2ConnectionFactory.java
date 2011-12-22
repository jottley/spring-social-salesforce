package org.springframework.social.connect.support;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * @author Umut Utkan
 */
public class RefreshingOAuth2ConnectionFactory<S> extends OAuth2ConnectionFactory<S> {

    public RefreshingOAuth2ConnectionFactory(String providerId, OAuth2ServiceProvider<S> soAuth2ServiceProvider, ApiAdapter<S> sApiAdapter) {
        super(providerId, soAuth2ServiceProvider, sApiAdapter);
    }


    @Override
    public Connection<S> createConnection(AccessGrant accessGrant) {
        return new RefreshingOAuth2Connection<S>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), (OAuth2ServiceProvider<S>) getServiceProvider(), getApiAdapter());
    }

    @Override
    public Connection<S> createConnection(ConnectionData data) {
        return new RefreshingOAuth2Connection<S>(data, (OAuth2ServiceProvider<S>) getServiceProvider(), getApiAdapter());
    }

}
