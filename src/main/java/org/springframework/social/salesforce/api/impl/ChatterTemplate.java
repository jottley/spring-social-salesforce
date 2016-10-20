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

import org.codehaus.jackson.JsonNode;
import org.springframework.social.salesforce.api.ChatterOperations;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.api.SalesforceProfile;
import org.springframework.social.salesforce.api.Status;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of ChatterOperations.
 *
 * @author Umut Utkan
 */
public class ChatterTemplate extends AbstractSalesForceOperations<Salesforce> implements ChatterOperations {

    private RestTemplate restTemplate;


    public ChatterTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;
        // adds interceptor to rest template for adding X-Chatter-Entity-Encoding=false header
        // this header informs Salesforce not to encode special characters and to return as is.
        this.restTemplate = addInterceptors(restTemplate);
    }


    @Override
    public SalesforceProfile getUserProfile() {
        return getUserProfile("me");
    }

    @Override
    public SalesforceProfile getUserProfile(String userId) {
        requireAuthorization();
        return restTemplate.getForObject(api.getBaseUrl() + "/v23.0/chatter/users/{id}",
                SalesforceProfile.class, userId);
    }

    @Override
    public Status getStatus() {
        return getStatus("me");
    }

    @Override
    public Status getStatus(String userId) {
        requireAuthorization();

        JsonNode node = restTemplate.getForObject(api.getBaseUrl() + "/v23.0/chatter/users/{userId}/status",
                JsonNode.class, userId);
        return api.readObject(node.get("body"), Status.class);
    }

    public Status updateStatus(String message) {
        return updateStatus("me", message);
    }

    @Override
    public Status updateStatus(String userId, String message) {
        requireAuthorization();

        MultiValueMap<String, String> post = new LinkedMultiValueMap<String, String>();
        post.add("text", message);
        JsonNode node = restTemplate.postForObject(api.getBaseUrl() + "/v23.0/chatter/users/{userId}/status",
                post, JsonNode.class, userId);
        return api.readObject(node.get("body"), Status.class);
    }

    private RestTemplate addInterceptors(RestTemplate restTemplate) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-Chatter-Entity-Encoding", "false");

        restTemplate.getInterceptors().add(new HeaderAddingInterceptor(headers));

        return restTemplate;
    }

}
