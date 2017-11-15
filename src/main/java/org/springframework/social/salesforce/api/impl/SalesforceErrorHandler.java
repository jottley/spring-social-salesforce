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
import org.springframework.social.InsufficientPermissionException;
import org.springframework.social.InternalServerErrorException;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.RateLimitExceededException;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.social.UncategorizedApiException;
import org.springframework.social.salesforce.api.SalesforceRequestException;
import org.springframework.social.salesforce.connect.SalesforceServiceProvider;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Umut Utkan
 * @author Jared Ottley
 */
public class SalesforceErrorHandler extends DefaultResponseErrorHandler {
    Logger logger = LoggerFactory.getLogger(SalesforceErrorHandler.class);
    
    private static final String ERROR_CODE = "errorCode";
    private static final String BAD_OAUTH_TOKEN = "Bad_OAuth_Token";
    private static final String MESSAGE = "message";
    private static final String SESSION_EXPIRED_INVALID = "Invalid access token";

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        Map<String, Object> errorDetails = extractErrorDetailsFromResponse(response);
        if (errorDetails == null) {
            handleUncategorizedError(response, errorDetails);
        }

        handleSalesforceError(response.getStatusCode(), errorDetails);

        // if not otherwise handled, do default handling and wrap with UncategorizedApiException
        handleUncategorizedError(response, errorDetails);
    }

    private void handleSalesforceError(HttpStatus statusCode, Map<String, Object> errorDetails) {
        if (statusCode.equals(HttpStatus.NOT_FOUND)) {
            throw new ResourceNotFoundException(SalesforceServiceProvider.ID, extractErrorMessage(errorDetails));
        } else if (statusCode.equals(HttpStatus.SERVICE_UNAVAILABLE)) {
            throw new RateLimitExceededException(SalesforceServiceProvider.ID);
        } else if (statusCode.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            throw new InternalServerErrorException(SalesforceServiceProvider.ID, errorDetails == null ? "Contact Salesforce administrator." : extractErrorMessage(errorDetails));
        } else if (statusCode.equals(HttpStatus.BAD_REQUEST) || statusCode.equals(HttpStatus.MULTIPLE_CHOICES)) {
            throw new SalesforceRequestException(SalesforceServiceProvider.ID, errorDetails);
        } else if (statusCode.equals(HttpStatus.UNAUTHORIZED)) {
            throw new InvalidAuthorizationException(SalesforceServiceProvider.ID, extractErrorMessage(errorDetails));
        } else if (statusCode.equals(HttpStatus.FORBIDDEN)) {
            if(errorDetails.get(ERROR_CODE).equals(BAD_OAUTH_TOKEN))
            {
                //This is recoverable by refreshing oauth tokens.
                throw new InsufficientPermissionException(extractErrorMessage(errorDetails));
            }
            else
            {
                throw new InsufficientPermissionException(extractErrorMessage(errorDetails));
            }
        }
    }

    private void handleUncategorizedError(ClientHttpResponse response, Map<String, Object> errorDetails) {
        try {
            super.handleError(response);
        } catch (Exception e) {
            if (errorDetails == null) {
                throw new UncategorizedApiException(SalesforceServiceProvider.ID, "No error details from Salesforce.", e);
            } else {
                throw new UncategorizedApiException(SalesforceServiceProvider.ID, extractErrorMessage(errorDetails), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractErrorDetailsFromResponse(ClientHttpResponse response) throws IOException {
        //Extract the response body so that it can be reused.  The InputStream provided by the response is closed after single use.
        byte[]  body = extractResponseBodyAsByteArray(response);
        
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        try {
            CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, Map.class);
            List<Map<String, Object>> errorList = (List<Map<String, Object>>) mapper.readValue(new ByteArrayInputStream(body), listType);
            if (errorList.size() > 0) {
                return errorList.get(0);
            }
        } catch (JsonParseException e) {
            //Salesforce is returning Bad_OAuth_Toekn in poorly formatted JSON.  We need to handle this case.
            if (BAD_OAUTH_TOKEN.equals(new String(body)))
            {
                Map<String, Object> errorDetails = new HashMap<>();
                errorDetails.put(ERROR_CODE, BAD_OAUTH_TOKEN);
                errorDetails.put(MESSAGE, SESSION_EXPIRED_INVALID);
                
                return errorDetails;
            }
            
            logger.error("Unable to parse salesforce response: {} ", new String(body));
            throw new UncategorizedApiException(SalesforceServiceProvider.ID, "Unable to read salesforce response.", e);
        }

        return null;
    }

    private String extractErrorMessage(Map<String, Object> errorDetails) {
        return (String) errorDetails.get(MESSAGE);
    }
    
    /**
     * The ClientHttpResponse is only valid for a single use before it is closed.  We need to pull it out into a reusable byte array
     * so we can use it in other places.
     *
     * @param response
     * @return byte[]
     */
    private byte[] extractResponseBodyAsByteArray(ClientHttpResponse response)
    {
        try{
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            byte[] data = new byte[1024];
            int length;
            while ((length = response.getBody().read(data)) != -1)
            {
                buffer.write(data, 0, length);
            }

            return buffer.toByteArray();
        }
        catch (IOException e)
        {
            throw new UncategorizedApiException(SalesforceServiceProvider.ID, "Unable to extract Salesforce response.", e);
        }
    }
}
