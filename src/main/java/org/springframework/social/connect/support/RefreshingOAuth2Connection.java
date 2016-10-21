/**
 * Copyright (C) 2016 https://github.com/jottley/spring-social-salesforce
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.connect.support;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * @author Umut Utkan
 */
public class RefreshingOAuth2Connection<A> extends OAuth2Connection<A> {

    public RefreshingOAuth2Connection(String providerId, String providerUserId, String accessToken, String refreshToken, Long expireTime, OAuth2ServiceProvider<A> aoAuth2ServiceProvider, ApiAdapter<A> aApiAdapter) {
        super(providerId, providerUserId, accessToken, refreshToken, expireTime, aoAuth2ServiceProvider, aApiAdapter);
    }

    public RefreshingOAuth2Connection(ConnectionData data, OAuth2ServiceProvider<A> aoAuth2ServiceProvider, ApiAdapter<A> aApiAdapter) {
        super(data, aoAuth2ServiceProvider, aApiAdapter);
    }



    @Override
    public A getApi() {
        if (hasExpired()) {
            refresh();
        }
        return super.getApi();
    }

}
