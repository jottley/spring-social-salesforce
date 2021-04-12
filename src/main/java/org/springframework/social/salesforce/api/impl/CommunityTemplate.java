/**
 * Copyright (C) 2019 https://github.com/jottley/spring-social-salesforce
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

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.social.salesforce.api.Community;
import org.springframework.social.salesforce.api.CommunityOperations;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class CommunityTemplate extends AbstractSalesForceOperations<Salesforce> implements CommunityOperations {

    private RestTemplate restTemplate;

    public CommunityTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Community> getCommunities() {

        requireAuthorization();

        JsonNode dataNode = restTemplate.getForObject(api.getBaseUrl() + "/" + getVersion() + "/connect/communities", JsonNode.class, getVersion());

        return api.readList(dataNode.get("communities"), Community.class);
    }

}
