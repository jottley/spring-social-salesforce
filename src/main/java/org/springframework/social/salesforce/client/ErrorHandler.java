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
package org.springframework.social.salesforce.client;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.RateLimitExceededException;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.Map;

/**
 * Custom error handler for handling Salesforce API specific error responses.
 *
 * @author Umut Utkan
 */
public class ErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            Map<String, String> error = extractErrorDetailsFromResponse(response);
            if ("unsupported_response_type".equals(error.get("error"))) {
                throw new OperationNotPermittedException(error.get("error_description"));
            } else if ("invalid_client_id".equals(error.get("error"))) {
                throw new InvalidAuthorizationException(error.get("error_description"));
            } else if ("invalid_request".equals(error.get("error"))) {
                throw new OperationNotPermittedException(error.get("error_description"));
            } else if ("invalid_client_credentials".equals(error.get("error"))) {
                throw new InvalidAuthorizationException(error.get("error_description"));
            } else if ("invalid_grant".equals(error.get("error"))) {
                if ("invalid user credentials".equals(error.get("error_description"))) {
                    throw new InvalidAuthorizationException(error.get("error_description"));
                } else if ("IP restricted or invalid login hours".equals(error.get("error_description"))) {
                    throw new OperationNotPermittedException(error.get("error_description"));
                }
                throw new InvalidAuthorizationException(error.get("error_description"));
            } else if ("inactive_user".equals(error.get("error"))) {
                throw new OperationNotPermittedException(error.get("error_description"));
            } else if ("inactive_org".equals(error.get("error"))) {
                throw new OperationNotPermittedException(error.get("error_description"));
            } else if ("rate_limit_exceeded".equals(error.get("error"))) {
                throw new RateLimitExceededException();
            } else if ("invalid_scope".equals(error.get("error"))) {
                throw new InvalidAuthorizationException(error.get("error_description"));
            }
        }

        super.handleError(response);
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> extractErrorDetailsFromResponse(ClientHttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        try {
            return (Map<String, String>) mapper.readValue(response.getBody(), Map.class);
        } catch (JsonParseException e) {
            return null;
        }
    }

}
