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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.*;
import org.springframework.social.salesforce.api.SalesforceRequestException;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Umut Utkan
 */
public class SalesforceErrorHandler extends DefaultResponseErrorHandler {
    Logger logger = LoggerFactory.getLogger(SalesforceErrorHandler.class);

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        Map<String, Object> errorDetails = extractErrorDetailsFromResponse(response);
        if (errorDetails == null) {
            handleUncategorizedError(response, errorDetails);
        }

        handleSalesforceError(response.getStatusCode(), errorDetails);

        // if not otherwise handled, do default handling and wrap with
        // UncategorizedApiException
        handleUncategorizedError(response, errorDetails);
    }

    private void handleSalesforceError(HttpStatus statusCode, Map<String, Object> errorDetails) {
        if (statusCode.equals(HttpStatus.NOT_FOUND)) {
            throw new ResourceNotFoundException(extractErrorMessage(errorDetails));
        } else if (statusCode.equals(HttpStatus.SERVICE_UNAVAILABLE)) {
            throw new RateLimitExceededException();
        } else if (statusCode.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            throw new InternalServerErrorException(errorDetails == null ? "Contact Salesforce administrator." : extractErrorMessage(errorDetails));
        } else if (statusCode.equals(HttpStatus.BAD_REQUEST) || statusCode.equals(HttpStatus.MULTIPLE_CHOICES)) {
            throw new SalesforceRequestException(errorDetails);
        } else if (statusCode.equals(HttpStatus.UNAUTHORIZED)) {
            throw new InvalidAuthorizationException(extractErrorMessage(errorDetails));
        } else if (statusCode.equals(HttpStatus.FORBIDDEN)) {
            throw new InsufficientPermissionException(extractErrorMessage(errorDetails));
        }
    }

    private void handleUncategorizedError(ClientHttpResponse response, Map<String, Object> errorDetails) {
        try {
            super.handleError(response);
        } catch (Exception e) {
            if (errorDetails == null) {
                throw new UncategorizedApiException("No error details from Salesforce.", e);
            } else {
                throw new UncategorizedApiException(extractErrorMessage(errorDetails), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractErrorDetailsFromResponse(ClientHttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        try {
            CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, Map.class);
            List<Map<String, Object>> errorList = (List<Map<String, Object>>) mapper.readValue(response.getBody(), listType);
            if (errorList.size() > 0) {
                return errorList.get(0);
            }
        } catch (JsonParseException e) {
            logger.error("Unable to parse salesforce response: {} ", response);
            throw new UncategorizedApiException("Unable to read salesforce response.", e);
        }

        return null;
    }

    private String extractErrorMessage(Map<String, Object> errorDetails) {
        return (String) errorDetails.get("message");
    }

}
