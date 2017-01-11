package org.springframework.social.salesforce.connect;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SalesforceOAuth2TemplateComposite implements OAuth2Operations
{
    public static final String SALESFORCE_SANDBOX_SESSION_PARAMETER = "sandboxSession";
    private static final String SALESFORCE_SANDBOX_PARAMETER = "sandbox";

    private SalesforceOAuth2Template productionSalesforceOAuth2Template;
    private SalesforceOAuth2Template sandboxSalesforceOAuth2Template;

    public SalesforceOAuth2TemplateComposite(String clientId,
                                             String clientSecret,
                                             String authorizeUrl,
                                             String tokenUrl,
                                             String sandboxAuthorizeUrl,
                                             String sandboxTokenUrl,
                                             ClientHttpRequestFactory clientHttpRequestFactory)
    {
        this.productionSalesforceOAuth2Template = new SalesforceOAuth2Template(clientId,
                                                                               clientSecret,
                                                                               authorizeUrl,
                                                                               tokenUrl,
                                                                               clientHttpRequestFactory);
        this.sandboxSalesforceOAuth2Template = new SalesforceOAuth2Template(clientId,
                                                                            clientSecret,
                                                                            sandboxAuthorizeUrl,
                                                                            sandboxTokenUrl,
                                                                            clientHttpRequestFactory);
    }

    @Override
    public String buildAuthorizeUrl(OAuth2Parameters parameters)
    {
        return getSalesforceOAuth2Template().buildAuthorizeUrl(parameters);
    }

    @Override
    public String buildAuthorizeUrl(GrantType grantType, OAuth2Parameters parameters)
    {
        return getSalesforceOAuth2Template().buildAuthorizeUrl(grantType, parameters);
    }

    @Override
    public String buildAuthenticateUrl(OAuth2Parameters parameters)
    {
        return getSalesforceOAuth2Template().buildAuthenticateUrl(parameters);
    }

    @Override
    public String buildAuthenticateUrl(GrantType grantType, OAuth2Parameters parameters)
    {
        return getSalesforceOAuth2Template().buildAuthenticateUrl(grantType, parameters);
    }

    @Override
    public AccessGrant exchangeForAccess(String authorizationCode,
                                         String redirectUri,
                                         MultiValueMap<String, String> additionalParameters)
    {
        return getSalesforceOAuth2Template().exchangeForAccess(authorizationCode, redirectUri, additionalParameters);
    }

    @Override
    public AccessGrant exchangeCredentialsForAccess(String username,
                                                    String password,
                                                    MultiValueMap<String, String> additionalParameters)
    {
        return getSalesforceOAuth2Template().exchangeCredentialsForAccess(username, password, additionalParameters);
    }

    @SuppressWarnings("deprecation")
    @Override
    public AccessGrant refreshAccess(String refreshToken,
                                     String scope,
                                     MultiValueMap<String, String> additionalParameters)
    {
        return getSalesforceOAuth2Template().refreshAccess(refreshToken, scope, additionalParameters);
    }

    @Override
    public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters)
    {
        return getSalesforceOAuth2Template().refreshAccess(refreshToken, additionalParameters);
    }

    @Override
    public AccessGrant authenticateClient()
    {
        return getSalesforceOAuth2Template().authenticateClient();
    }

    @Override
    public AccessGrant authenticateClient(String scope)
    {
        return getSalesforceOAuth2Template().authenticateClient(scope);
    }

    public SalesforceOAuth2Template getSalesforceOAuth2TemplateFromContext()
    {
        return getSalesforceOAuth2Template();
    }

    private SalesforceOAuth2Template getSalesforceOAuth2Template()
    {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        if (request.getParameter(SALESFORCE_SANDBOX_PARAMETER) != null) {
            servletRequestAttributes.setAttribute(SALESFORCE_SANDBOX_SESSION_PARAMETER,
                                                  new Object(),
                                                  RequestAttributes.SCOPE_SESSION);
            return sandboxSalesforceOAuth2Template;
        }

        if (request.getParameter("code") != null
                && servletRequestAttributes.getAttribute(SALESFORCE_SANDBOX_SESSION_PARAMETER,
                                                         RequestAttributes.SCOPE_SESSION) != null) {
            return sandboxSalesforceOAuth2Template;
        }

        servletRequestAttributes.removeAttribute(SALESFORCE_SANDBOX_SESSION_PARAMETER, RequestAttributes.SCOPE_SESSION);
        return productionSalesforceOAuth2Template;
    }
}
