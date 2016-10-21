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

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.social.salesforce.api.ApiOperations;
import org.springframework.social.salesforce.api.ApiVersion;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of ApiOperations.
 *
 * @author Umut Utkan
 */
public class ApiTemplate extends AbstractSalesForceOperations<Salesforce> implements ApiOperations {

    private RestTemplate restTemplate;


    public ApiTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;
    }


    public List<ApiVersion> getVersions() {
        URI uri = URIBuilder.fromUri(api.getBaseUrl()).build();
        JsonNode dataNode = restTemplate.getForObject(uri, JsonNode.class);
        return api.readList(dataNode, ApiVersion.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getServices(String version) {
        requireAuthorization();
        return restTemplate.getForObject(api.getBaseUrl() + "/v{version}", Map.class, version);
    }

}
