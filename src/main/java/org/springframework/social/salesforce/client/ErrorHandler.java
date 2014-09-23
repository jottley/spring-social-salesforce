package org.springframework.social.salesforce.client;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.RateLimitExceededException;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Custom error handler for handling Salesforce API specific error responses.
 *
 * @author Umut Utkan
 */
public class ErrorHandler extends DefaultResponseErrorHandler {

	@Override
	public void handleError(final ClientHttpResponse response) throws IOException {
		if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
			final Map<String, String> error = extractErrorDetailsFromResponse(response);
			if ("unsupported_response_type".equals(error.get("error"))) {
				throw new OperationNotPermittedException("salesforce", error.get("error_description"));
			} else if ("invalid_client_id".equals(error.get("error"))) {
				throw new InvalidAuthorizationException("salesforce", error.get("error_description"));
			} else if ("invalid_request".equals(error.get("error"))) {
				throw new OperationNotPermittedException("salesforce", error.get("error_description"));
			} else if ("invalid_client_credentials".equals(error.get("error"))) {
				throw new InvalidAuthorizationException("salesforce", error.get("error_description"));
			} else if ("invalid_grant".equals(error.get("error"))) {
				if ("invalid user credentials".equals(error.get("error_description"))) {
					throw new InvalidAuthorizationException("salesforce", error.get("error_description"));
				} else if ("IP restricted or invalid login hours".equals(error.get("error_description"))) {
					throw new OperationNotPermittedException("salesforce", error.get("error_description"));
				}
				throw new InvalidAuthorizationException("salesforce", error.get("error_description"));
			} else if ("inactive_user".equals(error.get("error"))) {
				throw new OperationNotPermittedException("salesforce", error.get("error_description"));
			} else if ("inactive_org".equals(error.get("error"))) {
				throw new OperationNotPermittedException("salesforce", error.get("error_description"));
			} else if ("rate_limit_exceeded".equals(error.get("error"))) {
				throw new RateLimitExceededException("salesforce");
			} else if ("invalid_scope".equals(error.get("error"))) {
				throw new InvalidAuthorizationException("salesforce", error.get("error_description"));
			}
		}

		super.handleError(response);
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> extractErrorDetailsFromResponse(final ClientHttpResponse response) throws IOException {
		final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
		try {
			return mapper.readValue(response.getBody(), Map.class);
		} catch (final JsonParseException e) {
			return null;
		}
	}

}
