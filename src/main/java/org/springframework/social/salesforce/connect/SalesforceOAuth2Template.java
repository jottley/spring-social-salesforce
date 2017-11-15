/**
 * Copyright (C) 2017 https://github.com/jottley/spring-social-salesforce
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
package org.springframework.social.salesforce.connect;

import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Salesforce OAuth2Template implementation.
 * <p/>
 * The reason to extend is to extract non-standard instance_url from Salesforce's oauth token response.
 *
 * @author Umut Utkan
 * @author Jared Ottley
 */
public class SalesforceOAuth2Template extends OAuth2Template {

    private String instanceUrl = null;


    public SalesforceOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        this(clientId, clientSecret, authorizeUrl, null, accessTokenUrl);
    }

    public SalesforceOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String authenticateUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, authenticateUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
    }


    @Override
    protected AccessGrant createAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn, Map<String, Object> response) {
        this.instanceUrl = (String) response.get("instance_url");

        return super.createAccessGrant(accessToken, scope, refreshToken, expiresIn, response);
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }
    
    
    /**
     * The default method from OAuth2Template is unable to handle the repsonse from Salesforce for an AccessGrant.  The Response is returned with a 
     * content type of application/octet-stream. Spring-social-core's OAuth2Template does not have a HttpMessageConverter registered that is able to
     * covert the response to a map even though it is Json but with a non-Json content type.
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        JsonNode response = getRestTemplate().postForObject(accessTokenUrl, parameters, JsonNode.class);
        
        ObjectMapper mapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, Object> result = mapper.convertValue(response, Map.class);
        
        return this.createAccessGrant((String) result.get("access_token"), (String) result.get("scope"), (String) result.get("refresh_token"), getIntegerValue(result, "expires_in"), result);
    }
    
    
    // Retrieves object from map into an Integer, regardless of the object's actual type. Allows for flexibility in object type (eg, "3600" vs 3600).
    // Private Method from OAuth2Template
    private Long getIntegerValue(Map<String, Object> map, String key) {
        try {
            return Long.valueOf(String.valueOf(map.get(key))); // normalize to String before creating integer value;            
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
