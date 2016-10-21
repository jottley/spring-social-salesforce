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
