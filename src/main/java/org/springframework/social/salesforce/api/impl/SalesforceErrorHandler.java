package org.springframework.social.salesforce.api.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.InsufficientPermissionException;
import org.springframework.social.InternalServerErrorException;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.RateLimitExceededException;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.social.UncategorizedApiException;
import org.springframework.social.salesforce.api.InvalidIDException;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * @author Umut Utkan
 */
public class SalesforceErrorHandler extends DefaultResponseErrorHandler {

	@Override
	public void handleError(final ClientHttpResponse response) throws IOException {
		final Map<String, Object> errorDetails = extractErrorDetailsFromResponse(response);
		if (errorDetails == null) {
			handleUncategorizedError(response, errorDetails);
		}

		handleSalesforceError(response.getStatusCode(), errorDetails);

		// if not otherwise handled, do default handling and wrap with UncategorizedApiException
		handleUncategorizedError(response, errorDetails);
	}

	private void handleSalesforceError(final HttpStatus statusCode, final Map<String, Object> errorDetails) {
		if (statusCode.equals(HttpStatus.NOT_FOUND)) {
			throw new ResourceNotFoundException("salesforce", generateMessage(errorDetails));
		} else if (statusCode.equals(HttpStatus.SERVICE_UNAVAILABLE)) {
			throw new RateLimitExceededException("salesforce");
		} else if (statusCode.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
			throw new InternalServerErrorException("salesforce", errorDetails == null ? "Contact Salesforce administrator."
					: generateMessage(errorDetails));
		} else if (statusCode.equals(HttpStatus.BAD_REQUEST)) {
			throw new InvalidIDException(generateMessage(errorDetails));
		} else if (statusCode.equals(HttpStatus.UNAUTHORIZED)) {
			throw new InvalidAuthorizationException("salesforce", generateMessage(errorDetails));
		} else if (statusCode.equals(HttpStatus.FORBIDDEN)) {
			throw new InsufficientPermissionException(generateMessage(errorDetails));
		}
	}

	private void handleUncategorizedError(final ClientHttpResponse response, final Map<String, Object> errorDetails) {
		try {
			super.handleError(response);
		} catch (final Exception e) {
			if (errorDetails != null) {
				throw new UncategorizedApiException("salesforce", generateMessage(errorDetails), e);
			} else {
				throw new UncategorizedApiException("salesforce", "No error details from Salesforce.", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> extractErrorDetailsFromResponse(final ClientHttpResponse response) throws IOException {
		final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
		try {
			final CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, Map.class);
			final List<Map<String, Object>> errorList = (List<Map<String, Object>>) mapper.readValue(response.getBody(), listType);
			if (errorList.size() > 0) {
				return errorList.get(0);
			}
		} catch (final JsonParseException e) {

		}

		return null;
	}

	private String generateMessage(final Map<String, Object> errorDetails) {
		return (String) errorDetails.get("message");
	}

}
