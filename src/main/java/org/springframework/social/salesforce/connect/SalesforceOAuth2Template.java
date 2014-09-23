package org.springframework.social.salesforce.connect;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Salesforce OAuth2Template implementation.
 * <p/>
 * The reason to extend is to extract non-standard instance_url from Salesforce's oauth token response.
 *
 * @author Umut Utkan
 */
public class SalesforceOAuth2Template extends OAuth2Template {

	private String instanceUrl = null;

	public SalesforceOAuth2Template(final String clientId, final String clientSecret, final String authorizeUrl, final String accessTokenUrl) {
		this(clientId, clientSecret, authorizeUrl, null, accessTokenUrl);
	}

	public SalesforceOAuth2Template(final String clientId, final String clientSecret, final String authorizeUrl,
			final String authenticateUrl, final String accessTokenUrl) {
		super(clientId, clientSecret, authorizeUrl, authenticateUrl, accessTokenUrl);
		setUseParametersForClientAuthentication(true);
	}

	public String getInstanceUrl() {
		return instanceUrl;
	}

	@Override
	protected RestTemplate createRestTemplate() {
		final RestTemplate restTemplate = new RestTemplate(ClientHttpRequestFactorySelector.getRequestFactory());

		// salesforce doesn't set content-type; fix it
		final FormHttpMessageConverter messageConverter = new FormHttpMessageConverter() {
			@Override
			public boolean canRead(final Class<?> clazz, final MediaType mediaType) {
				return true;
			}

			@Override
			public MultiValueMap<String, String> read(final Class<? extends MultiValueMap<String, ?>> clazz,
					final HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
				if (inputMessage.getHeaders().getContentType() == null) {
					inputMessage.getHeaders().setContentType(MediaType.TEXT_PLAIN);
				}
				return super.read(clazz, inputMessage);
			}
		};
		restTemplate.setMessageConverters(Collections.<HttpMessageConverter<?>>singletonList(messageConverter));
		return restTemplate;
	}

	// resteasy gives me MultiValueMap from salesforce instead of Map; fix it:
	@Override
	@SuppressWarnings("unchecked")
	protected AccessGrant postForAccessGrant(final String accessTokenUrl, final MultiValueMap<String, String> parameters) {
		final MultiValueMap<String, String> result = getRestTemplate().postForObject(accessTokenUrl, parameters, MultiValueMap.class);
		instanceUrl = result.getFirst("instance_url");
		return createAccessGrant(result.getFirst("access_token"), result.getFirst("scope"), result.getFirst("refresh_token"), null, null);
	}
}
